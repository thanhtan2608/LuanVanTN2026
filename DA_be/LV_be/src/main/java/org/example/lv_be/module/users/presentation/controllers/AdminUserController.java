package org.example.lv_be.module.users.presentation.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.users.application.dto.CreateStaffRequest;
import org.example.lv_be.module.users.application.dto.UpdateStaffRequest;
import org.example.lv_be.module.users.application.interfaces.in.ICreateStaffUseCase;
// import org.example.lv_be.module.users.application.interfaces.in.IUpdateStaffUseCase;
import org.example.lv_be.module.users.application.interfaces.in.IUpdateStaffUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/users/staffs")
@RequiredArgsConstructor
// @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')") // Bật lên khi bạn config xong Spring Security
public class AdminUserController {

    private final ICreateStaffUseCase createStaffUseCase;
     private final IUpdateStaffUseCase updateStaffUseCase; // Khai báo thêm nếu bạn đã tạo Use Case này

    /**
     * [DÀNH CHO FRONTEND] API Tạo tài khoản Nhân viên nội bộ
     * - Header: Authorization: Bearer <Token_của_Admin>
     * - Body: CreateStaffRequest (Có lương, role, hoa hồng)
     * - Xử lý FE: Khi API trả về 201, hiển thị Toast "Tạo nhân viên thành công",
     * sau đó reload lại table danh sách nhân viên.
     */
    @PostMapping
    public ResponseEntity<Void> createStaff(@Valid @RequestBody CreateStaffRequest request) {
        createStaffUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).build(); // 201 Không cần trả về body
    }

    /**
     * [DÀNH CHO FRONTEND] API Cập nhật tài khoản Nhân viên (Thăng chức, Tăng lương, Khóa)
     * - Path Variable: {staffId} - ID của nhân viên cần sửa
     * - Body: UpdateStaffRequest
     */

    @PutMapping("/{staffId}")
    public ResponseEntity<Void> updateStaff(@PathVariable Long staffId, @Valid @RequestBody UpdateStaffRequest request) {
        updateStaffUseCase.execute(staffId, request);
        return ResponseEntity.ok().build(); // 200 OK
    }

}