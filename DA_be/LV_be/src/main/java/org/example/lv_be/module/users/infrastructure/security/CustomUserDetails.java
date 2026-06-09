package org.example.lv_be.module.users.infrastructure.security;

import org.example.lv_be.module.users.domain.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    // CHỈ CHỨA CÁC KIỂU DỮ LIỆU CƠ BẢN (Primitive Types)
    // Tuyệt đối không lưu giữ trực tiếp đối tượng User (Domain Entity) ở đây
    private final String phone;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean isAccountNonLocked;
    private final boolean isEnabled;

    // Hàm khởi tạo đóng vai trò như một Mapper
    // Biến dữ liệu từ Domain User thành ngôn ngữ mà Spring Security hiểu
    public CustomUserDetails(User user) {
        this.phone = user.getPhone();
        this.password = user.getPassword();
        // Spring Security bắt buộc quyền phải có chữ "ROLE_" đứng trước
        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        this.isAccountNonLocked = user.isActive();
        this.isEnabled = !user.isDeleted();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return phone; // Dùng số điện thoại làm Username đăng nhập
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Mặc định không hết hạn tài khoản
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Mặc định không hết hạn mật khẩu
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}