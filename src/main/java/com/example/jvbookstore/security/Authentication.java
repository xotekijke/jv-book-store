package com.example.jvbookstore.security;

public interface Authentication {
    Object getPrincipal();
    Object getCredentials();
}
