package com.example.jvbookstore.security;

public class UsernamePasswordAuthenticationToken implements Authentication {
    private final String username;
    private final String password;

    public UsernamePasswordAuthenticationToken(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public Object getCredentials() {
        return username;
    }

    @Override
    public Object getPrincipal() {
        return password ;
    }
}
