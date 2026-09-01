package com.busbooking.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

    private SecurityUtils(){
        //private constructor to prevent instantiation
    }
    public static Long getCurrentUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        if(principal instanceof UserPrincipal){
            return ((UserPrincipal) principal).getUserId();
        }

        return null;

    }
}
