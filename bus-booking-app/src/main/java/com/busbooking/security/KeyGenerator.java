package com.busbooking.security;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.SecureRandom;
import java.util.Base64;

public class KeyGenerator {

    public static void main(String[] args){
//        byte[] keyBytes = new byte[32];
//        new SecureRandom().nextBytes(keyBytes);
//        String base64Key = Base64.getEncoder().encodeToString(keyBytes);
//        System.out.println("Generated Key: " + base64Key);
        var key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());
        System.out.println("Generated Key : " +base64Key);
    }
}
