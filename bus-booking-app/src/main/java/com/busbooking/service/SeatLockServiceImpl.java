package com.busbooking.service;

import com.busbooking.EventHandler.RedisSeatPublisher;
import com.busbooking.dto.SeatLock;
import com.busbooking.dto.SeatLockRequest;
import com.busbooking.dto.SeatUpdateEvent;
import com.busbooking.repository.PassengerRepository;
import com.busbooking.security.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SeatLockServiceImpl implements SeatLockService{

    @Resource(name = "seatLockRedisTemplate")
    private RedisTemplate<String, SeatLock> template;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private PassengerRepository passengerRepository;

    private static final long LOCK_DURATION = 5;
    @Autowired
    private RedisSeatPublisher redisSeatPublisher;

    @jakarta.annotation.PostConstruct
    public void checkInjection() {
        if (this.template == null) {
            System.err.println("CRITICAL: RedisTemplate failed to inject into SeatLockService!");
        } else {
            System.out.println("SUCCESS: RedisTemplate is ready to use.");
        }
    }

    @Override
    public boolean seatLock(SeatLockRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();

        System.out.println("Current User ID : " + userId);

        Set<String> bookedSeats = passengerRepository.findBookedSeats(request.getBusId(), request.getDate());

        System.out.println("Booked seats : " + bookedSeats);

        if(userId == null){
            throw new RuntimeException("User not Authenticated");
        }
        List<String> lockedKeys = new ArrayList<>();

        try{
            for(String seatNo : request.getSeatNumbers()){

                String key = buildKey(request.getBusId(), request.getDate(), seatNo);

                SeatLock seatLock = new SeatLock(
                        userId,
                        LocalDateTime.now()
                );

                Boolean success = template.opsForValue()
                        .setIfAbsent(key, seatLock, Duration.ofMinutes(LOCK_DURATION));

                lockedKeys.add(key);

                if(bookedSeats!=null && bookedSeats.contains(seatNo)){
                    template.delete(lockedKeys);
                    stringRedisTemplate.delete(lockedKeys);
                    System.out.println("Booked Seats are there");
                    return false;
                }

                if(!Boolean.TRUE.equals(success)){
                    template.delete(lockedKeys);
                    stringRedisTemplate.delete(lockedKeys);
                    return false;
                }

                redisSeatPublisher.publish(new SeatUpdateEvent(
                        request.getBusId(),
                        request.getDate(),
                        seatNo,
                        "LOCKED",
                        userId
                ));

                System.out.println("Seat got Locked : " + seatNo);

                String indexKey = "active_locks" + ":" + request.getBusId() + ":" + request.getDate();

                stringRedisTemplate.opsForSet().add(indexKey, seatNo);

                stringRedisTemplate.expire(indexKey, Duration.ofHours(24));

            }
            return true;
        }catch (Exception e){
            template.delete(lockedKeys);
            throw e;
        }

    }

    public void unlockSeat(long busId, LocalDate date, Set<String> seatNumbers) {
        Long currentUserId = SecurityUtils.getCurrentUserId();

        for(String seatNo : seatNumbers){
            String key = buildKey(busId, date, seatNo);
            SeatLock lock = template.opsForValue().get(key);

            if(lock == null) {
                continue;
            }

            if(!lock.userId().equals(currentUserId)){
                throw new RuntimeException("You are not authorized to unlock this seat");
            }
            template.delete(key);

            redisSeatPublisher.publish(new SeatUpdateEvent(
                    busId,
                    date,
                    seatNo,
                    "UNLOCKED",
                    currentUserId
            ));
        }

    }

    @Override
    public boolean extendLock(SeatLockRequest request) {
        Long currentUserId = SecurityUtils.getCurrentUserId();

        List<String> extendedKeys = new ArrayList<>();

        for(String seatNo : request.getSeatNumbers()){

            String key = buildKey(request.getBusId(), request.getDate(), seatNo);
            SeatLock lock = template.opsForValue().get(key);

            // Lock doesn't exist or belongs to someone else
            if(lock == null || !lock.userId().equals(currentUserId)){
                template.delete(extendedKeys);
                return false;
            }

            template.expire(key, Duration.ofMinutes(LOCK_DURATION));

            extendedKeys.add(key);

        }

        return true;
    }

    public boolean isLockValidForUser(Long busId, LocalDate date, String seatNumber){
        Long currentUserId = SecurityUtils.getCurrentUserId();
        String key = buildKey(busId, date, seatNumber);
        SeatLock lock = template.opsForValue().get(key);
        if (lock == null) return false; // Lock doesn't exist or expired

        // Check if the user owning the lock is the one trying to book
        return lock.userId().equals(currentUserId);
    }

    public Set<String> getActiveLockedSeats(Long busId, LocalDate date){
        String indexKey = "active_locks" + ":" + busId + ":" + date;

        Set<String> potentialLocks = stringRedisTemplate.opsForSet().members(indexKey);

        if (potentialLocks == null) return Collections.emptySet();

        return potentialLocks.stream()
                .filter(seatNum -> {
                        String lockKey = "seat_lock:" + busId + ":" + date + ":" + seatNum;
                        return template.hasKey(lockKey); // Only return if the 5-min lock still exists
                })
                .collect(Collectors.toSet());
    }

    private String buildKey(Long busId, LocalDate date ,String seatNumber) {
        return "seat_lock:" + busId + ":" + date + ":" + seatNumber;
    }

}
