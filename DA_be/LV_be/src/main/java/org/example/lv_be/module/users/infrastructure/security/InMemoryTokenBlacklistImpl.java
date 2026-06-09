package org.example.lv_be.module.users.infrastructure.security;

import org.example.lv_be.core.security.ITokenBlacklistRepository;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryTokenBlacklistImpl implements ITokenBlacklistRepository {

    // Sử dụng ConcurrentHashMap để đảm bảo an toàn khi nhiều user truy cập cùng lúc
    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();

    @Override
    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
    }

    @Override
    public boolean isBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}
//(Để hệ thống chạy nhanh và không phải tạo thêm bảng rườm rà, mình sẽ dùng bộ nhớ tạm In-Memory cho Blacklist.
// Ở dự án thực tế quy mô lớn, bạn chỉ cần thay ruột file này bằng Redis là xong, không cần sửa tầng khác).