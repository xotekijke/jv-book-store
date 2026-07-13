package com.example.jvbookstore.security;

public class SecurityContextHolder {
    private static ThreadLocal<SecurityContext> securityContext = ThreadLocal.withInitial(SecurityContext::new);

    public static SecurityContext getSecurityContext() {
        return securityContext.get();
    }
}
