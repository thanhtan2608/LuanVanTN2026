package org.example.lv_be.module.branches.presentation.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.lv_be.core.response.ApiResponse;
import org.example.lv_be.module.branches.application.dto.BranchResponse;
import org.example.lv_be.module.branches.application.dto.CreateBranchRequest;
import org.example.lv_be.module.branches.application.dto.UpdateBranchRequest;
import org.example.lv_be.module.branches.application.interfaces.ICreateBranchUseCase;
import org.example.lv_be.module.branches.application.interfaces.IDeleteBranchUseCase;
import org.example.lv_be.module.branches.application.interfaces.IUpdateBranchUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * =========================================================================
 * 🔐 LƯU Ý BẢO MẬT CHO FRONTEND (ADMIN BRANCH SECURITY CONTEXT)
 * =========================================================================
 * Toàn bộ các API quản lý chi nhánh này đều yêu cầu quyền Quản trị tối cao.
 * Frontend khi thực hiện gọi API bắt buộc phải đính kèm Header:
 * 🔹 Authorization: Bearer [Token của tài khoản có quyền ADMIN]
 * Nếu không truyền Token hoặc đăng nhập tài khoản Khách, Server sẽ chặn quyền (403 Forbidden).
 * =========================================================================
 */
@RestController
@RequestMapping("/api/v1/admin/branches")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminBranchController {

    private final ICreateBranchUseCase createBranchUseCase;
    private final IUpdateBranchUseCase updateBranchUseCase;
    private final IDeleteBranchUseCase deleteBranchUseCase;

    /**
     * 📘 FRONTEND NOTE: THÊM MỚI CHI NHÁNH SALON (HOẶC TỰ ĐỘNG KHÔI PHỤC CHI NHÁNH CŨ)
     * ------------------------------------------------------------------------
     * 🔹 METHOD: POST
     * 🔹 URL CHUẨN: /api/v1/admin/branches
     * 🔹 HEADERS: Content-Type = application/json, Authorization = Bearer [Admin_Token]
     * 🔹 REQUEST BODY (JSON):
     * {
     * "branchName": "Hair Studio Quận 1",
     * "address": "123 Nguyễn Huệ, Phường Bến Nghé, Quận 1, TP. HCM",
     * "phone": "02877778888",
     * "hotline": "19001234" // Có thể truyền thêm tùy thuộc vào cấu hình DTO thực tế của bạn
     * }
     * 🔹 RESPONSE EX (success): Trả về chi tiết chi nhánh vừa khởi tạo thành công kèm ID hệ thống
     * {
     * "success": true,
     * "code": 200,
     * "message": "Thêm mới chi nhánh thành công!",
     * "data": {
     * "id": 5,
     * "branchName": "Hair Studio Quận 1",
     * "address": "123 Nguyễn Huệ, Phường Bến Nghé, Quận 1, TP. HCM",
     * "phone": "02877778888",
     * "isActive": true
     * }
     * }
     */
    @PostMapping
    public ResponseEntity<ApiResponse<BranchResponse>> createBranch(
            @Valid @RequestBody CreateBranchRequest request) {
        BranchResponse data = createBranchUseCase.execute(request);
        return ResponseEntity.ok(ApiResponse.success(data, "Thêm mới chi nhánh thành công!"));
    }

    /**
     * 📘 FRONTEND NOTE: CẬP NHẬT THÔNG TIN HOẶC TRẠNG THÁI ĐÓNG/MỞ CHI NHÁNH
     * ------------------------------------------------------------------------
     * 🔹 METHOD: PUT
     * 🔹 URL CHUẨN: /api/v1/admin/branches/{id} (Ví dụ: /api/v1/admin/branches/5)
     * 🔹 HEADERS: Content-Type = application/json, Authorization = Bearer [Admin_Token]
     * 🔹 REQUEST BODY (JSON):
     * {
     * "branchName": "Hair Studio Premium Quận 1",
     * "address": "123 Nguyễn Huệ, Quận 1, TP. HCM (Cập nhật)",
     * "phone": "02877779999",
     * "isActive": true // Gửi 'false' nếu muốn tạm dừng hoạt động chi nhánh (Đóng cửa sửa chữa...)
     * }
     * 🔹 RESPONSE EX (success): Trả về dữ liệu chi nhánh sau khi đã được cập nhật thay đổi
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BranchResponse>> updateBranch(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBranchRequest request) {
        BranchResponse data = updateBranchUseCase.execute(id, request);
        return ResponseEntity.ok(ApiResponse.success(data, "Cập nhật dữ liệu chi nhánh thành công!"));
    }

    /**
     * 📘 FRONTEND NOTE: XÓA MỀM CHI NHÁNH KHỎI HỆ THỐNG HIỂN THỊ
     * ------------------------------------------------------------------------
     * 🔹 METHOD: DELETE
     * 🔹 URL CHUẨN: /api/v1/admin/branches/{id} (Ví dụ: /api/v1/admin/branches/5)
     * 🔹 HEADERS: Authorization = Bearer [Admin_Token]
     * 🔹 BODY: Trống (Empty)
     * 🔹 CƠ CHẾ NGẦM: Lệnh này không xóa cứng mất dữ liệu trong DB mà chuyển 'is_deleted = true'.
     * Frontend nhận phản hồi thành công thì cập nhật xóa chi nhánh này ra khỏi State hiển thị UI.
     * 🔹 RESPONSE EX (success):
     * {
     * "success": true,
     * "code": 200,
     * "message": "Xóa chi nhánh ra khỏi hệ thống thành công!",
     * "data": null
     * }
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBranch(@PathVariable Long id) {
        deleteBranchUseCase.execute(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Xóa chi nhánh ra khỏi hệ thống thành công!"));
    }
}