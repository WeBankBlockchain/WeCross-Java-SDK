package com.webank.wecrosssdk.rpc.service;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationManager {
    private AuthenticationManager() {}

    public static final ThreadLocal<String> runtimeAuthType = new ThreadLocal<>();

    public static String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return null;
        }
        if (authentication instanceof AnonymousAuthenticationToken) {
            return "anonymousUser";
        }
        return (String) authentication.getPrincipal();
    }

    public static String getCredentialByUsername(String username) {
        if (username.equals(getCurrentUser())) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication instanceof AnonymousAuthenticationToken) {
                return null;
            }
            return (String) authentication.getCredentials();
        } else {
            return null;
        }
    }

    public static String getCurrentUserCredential() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        if (authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        return (String) authentication.getCredentials();
    }

    public static void setCurrentUser(String username, String token) {
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(username, token);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }

    public static void clearCurrentUser() {
        SecurityContextHolder.clearContext();
    }
}
