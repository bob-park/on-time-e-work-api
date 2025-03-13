package com.malgn.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public interface AuthUtils {

    static String getCurrentUserId() {
        Jwt jwt = (Jwt)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return jwt.getSubject();
    }

}
