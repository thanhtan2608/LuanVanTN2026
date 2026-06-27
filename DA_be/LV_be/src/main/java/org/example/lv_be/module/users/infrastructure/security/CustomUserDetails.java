package org.example.lv_be.module.users.infrastructure.security;

import org.example.lv_be.module.users.domain.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    // CHỈ CHỨA CÁC KIỂU DỮ LIỆU CƠ BẢN (Primitive Types)
    private final String phone;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean isAccountNonLocked;

    // Hàm khởi tạo đóng vai trò như một Mapper
    public CustomUserDetails(User user) {
        this.phone = user.getPhone();
        this.password = user.getPassword();
        // Spring Security bắt buộc quyền phải có chữ "ROLE_" đứng trước
        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

        // Dùng cờ isActive để kiểm soát việc tài khoản có bị khóa hay không
        this.isAccountNonLocked = user.isActive();
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
        // Vì đã bỏ isDeleted, tài khoản mặc định luôn enable (tồn tại),
        // việc chặn đăng nhập sẽ do hàm isAccountNonLocked() đảm nhiệm.
        return true;
    }
}