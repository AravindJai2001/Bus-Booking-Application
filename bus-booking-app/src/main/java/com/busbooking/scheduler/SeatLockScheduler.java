package com.busbooking.scheduler;

import com.busbooking.repository.BookingRepository;
import com.busbooking.repository.SeatLockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class SeatLockScheduler {
    @Autowired
    private SeatLockRepository repo;

    @Autowired
    private BookingRepository bookRepo;

    // This method will run every 5 minutes to clean up expired seat locks
    @Scheduled(fixedRate = 60000) // 60000 milliseconds = 1 minutes
    @Transactional
    public void cleanUpExpiredLocks() {
        LocalTime expiredTime = LocalTime.now().minusMinutes(5);
        repo.deleteExpiredLocks(expiredTime);
        System.out.println("Expired seat locks cleaned up at " + LocalTime.parse(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
    }

    @Scheduled(cron = "0 0 9 * * ?") // every day at 9AM
    @Transactional
    public void cleanUpPendingBookings() {
        LocalTime expiredTime = LocalTime.now().minusHours(24);
        bookRepo.deletePendingBookings(expiredTime);
        System.out.println("Deleted pending bookings at " + LocalTime.now());
    }
}
