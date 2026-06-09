package org.example.lv_be.module.users.presentation.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.lv_be.core.response.ApiResponse;
import org.example.lv_be.module.users.application.dto.CreateStaffRequest;
import org.example.lv_be.module.users.application.dto.UpdateStaffRequest;
import org.example.lv_be.module.users.application.dto.UserProfileResponse;
import org.example.lv_be.module.users.application.services.UserManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * =========================================================================
 * 🔐 LƯU Ý TỐI THƯỢNG CHO FRONTEND (SECURITY GUIDELINE)
 * =========================================================================
 * Toàn bộ Controller này đã được khóa bằng quyền "ROLE_ADMIN".
 * Tất cả các lượt gọi API từ Frontend bắt buộc phải đính kèm Header:
 * 🔹 Authorization: Bearer [Token của tài khoản có quyền Admin]
 * Nếu thiếu Token hoặc Token sai quyền, Server sẽ trả về mã lỗi 403 Forbidden.
 * =========================================================================
 */
@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final UserManagementService userManagementService;

    /**
     * 📘 FRONTEND NOTE: LẤY DANH SÁCH TOÀN BỘ NHÂN VIÊN (STAFFS)
     * ------------------------------------------------------------------------
     * 🔹 METHOD: GET
     * 🔹 URL CHUẨN: /api/v1/admin/users/staffs
     * 🔹 HEADERS: Authorization = Bearer [Admin_Token]
     * 🔹 RESPONSE EX (success): Trả về mảng danh sách thông tin nhân viên
     * {
     * "success": true,
     * "code": 200,
     * "message": "Lấy danh sách nhân sự thành công!",
     * "data": [
     * { "id": 2, "fullName": "Nguyễn Văn A", "email": "anv@hair.com", "phone": "0987654321", "role": "STAFF" },
     * { "id": 3, "fullName": "Trần Thị B", "email": "btt@hair.com", "phone": "0123456789", "role": "STAFF" }
     * ]
     * }
     */
    @GetMapping("/staffs")
    public ResponseEntity<ApiResponse<List<UserProfileResponse>>> getAllStaffs() {
        List<UserProfileResponse> staffs = userManagementService.getAllStaffs();
        return ResponseEntity.ok(ApiResponse.success(staffs, "Lấy danh sách nhân sự thành công!"));
    }

    /**
     * 📘 FRONTEND NOTE: TẠO MỚI TÀI KHOẢN NHÂN VIÊN (STAFF)
     * ------------------------------------------------------------------------
     * 🔹 METHOD: POST
     * 🔹 URL CHUẨN: /api/v1/admin/users/staffs
     * 🔹 HEADERS: Content-Type = application/json, Authorization = Bearer [Admin_Token]
     * 🔹 REQUEST BODY (JSON):
     * {
     * "fullName": "Lê Văn C",
     * "email": "clevan@hair.com",
     * "phone": "0912345678",
     * "password": "Password123@",
     * "salary": 7500000.0
     * }
     * 🔹 RESPONSE EX (success): Trả về object nhân viên vừa tạo kèm ID
     * {
     * "success": true,
     * "code": 200,
     * "message": "Tạo nhân viên thành công!",
     * "data": { "id": 4, "fullName": "Lê Văn C", "email": "clevan@hair.com", "phone": "0912345678", "role": "STAFF" }
     * }
     */
    @PostMapping("/staffs")
    public ResponseEntity<ApiResponse<UserProfileResponse>> createStaff(
            @Valid @RequestBody CreateStaffRequest request) {
        UserProfileResponse newStaff = userManagementService.createStaff(request);
        return ResponseEntity.ok(ApiResponse.success(newStaff, "Tạo nhân viên thành công!"));
    }

    /**
     * 📘 FRONTEND NOTE: CẬP NHẬT THÔNG TIN NHÂN VIÊN THEO ID
     * ------------------------------------------------------------------------
     * 🔹 METHOD: PUT
     * 🔹 URL CHUẨN: /api/v1/admin/users/staffs/{id} (Ví dụ: /api/v1/admin/users/staffs/4)
     * 🔹 HEADERS: Content-Type = application/json, Authorization = Bearer [Admin_Token]
     * 🔹 REQUEST BODY (JSON):
     * {
     * "fullName": "Lê Văn C_Cập_Nhật",
     * "phone": "0900000000",
     * "status": "ACTIVE" // Hoặc "INACTIVE" để khóa tài khoản
     * }
     * 🔹 RESPONSE EX (success): Trả về thông tin sau khi sửa đổi thành công
     */
    @PutMapping("/staffs/{id}")
    public ResponseEntity<ApiResponse<UserProfileResponse>> updateStaff(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStaffRequest request) {
        UserProfileResponse updatedStaff = userManagementService.updateStaff(id, request);
        return ResponseEntity.ok(ApiResponse.success(updatedStaff, "Cập nhật nhân viên thành công!"));
    }

    /**
     * 📘 FRONTEND NOTE: XÓA NHÂN VIÊN RA KHỎI HỆ THỐNG
     * ------------------------------------------------------------------------
     * 🔹 METHOD: DELETE
     * 🔹 URL CHUẨN: /api/v1/admin/users/staffs/{id} (Ví dụ: /api/v1/admin/users/staffs/4)
     * 🔹 HEADERS: Authorization = Bearer [Admin_Token]
     * 🔹 BODY: Trống (Empty)
     * 🔹 RESPONSE EX (success):
     * {
     * "success": true,
     * "code": 200,
     * "message": "Đã xóa nhân viên thành công!",
     * "data": null
     * }
     */
    @DeleteMapping("/staffs/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStaff(@PathVariable Long id) {
        userManagementService.deleteStaff(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Đã xóa nhân viên thành công!"));
    }
}