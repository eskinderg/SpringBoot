package com.project.api.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Map;
import java.util.UUID;

public class CurrentAuthContext {
    private static Map<String, Object> extractClaim() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        Map<String, Object> claims = ((Jwt) principal).getClaims();
        return claims;
    }

    public static String getUserEmail() {
        String mail = (String) extractClaim().get("email");
        return (mail != null) ? mail : "";
    }

    public static UUID getUserId() {
        return UUID.fromString((String) extractClaim().get("sub"));
    }

    public static String getName() {
        return extractClaim().get("name") != null ? (String) extractClaim().get("name") : (String) extractClaim().get("preferred_username");
    }

    public static boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + role));
    }

}
