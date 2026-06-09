package org.example.lv_be.core.security;

public interface ITokenBlacklistRepository {
    void blacklistToken(String token);
    boolean isBlacklisted(String token);
}