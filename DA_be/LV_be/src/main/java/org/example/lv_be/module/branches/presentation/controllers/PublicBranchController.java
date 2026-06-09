package org.example.lv_be.module.branches.presentation.controllers;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.core.response.ApiResponse;
import org.example.lv_be.module.branches.application.dto.BranchResponse;
import org.example.lv_be.module.branches.application.interfaces.IGetAllActiveBranchesUseCase;
import org.example.lv_be.module.branches.application.interfaces.IGetBranchByIdUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * =========================================================================
 * 🌍 LƯU Ý QUAN TRỌNG CHO FRONTEND (PUBLIC ENDPOINTS)
 * =========================================================================
 * Các API trong Controller này là CÔNG KHAI (PUBLIC).
 * 🔹 KHÔNG YÊU CẦU ĐĂNG NHẬP (Không cần đính kèm Header Authorization).
 * Phục vụ cho việc hiển thị danh sách salon ngoài Trang chủ, trang giới thiệu
 * hoặc màn hình thả xuống (Dropdown) để khách chọn chi nhánh khi Đặt lịch.
 * =========================================================================
 */
@RestController
@RequestMapping("/api/v1/public/branches")
@RequiredArgsConstructor
public class PublicBranchController {

    private final IGetAllActiveBranchesUseCase getAllActiveBranchesUseCase;
    private final IGetBranchByIdUseCase getBranchByIdUseCase;

    /**
     * 📘 FRONTEND NOTE: LẤY DANH SÁCH TOÀN BỘ CHI NHÁNH ĐANG HOẠT ĐỘNG
     * ------------------------------------------------------------------------
     * 🔹 METHOD: GET
     * 🔹 URL CHUẨN: /api/v1/public/branches
     * 🔹 HEADERS: Không yêu cầu (Bỏ trống Authorization)
     * 🔹 BODY: Trống (Empty)
     * 🔹 RESPONSE EX (success): Trả về mảng danh sách các salon sạch đang mở cửa
     * {
     * "success": true,
     * "code": 200,
     * "message": "Lấy danh sách chi nhánh thành công!",
     * "data": [
     * {
     * "id": 1,
     * "branchName": "Hair Studio Quận 1 - Premium",
     * "address": "123 Nguyễn Huệ, Quận 1, TP. HCM",
     * "phone": "02877778888",
     * "isActive": true
     * },
     * {
     * "id": 2,
     * "branchName": "Hair Studio Bình Thạnh",
     * "address": "456 Điện Biên Phủ, Quận Bình Thạnh, TP. HCM",
     * "phone": "02877779999",
     * "isActive": true
     * }
     * ]
     * }
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<BranchResponse>>> getAllActiveBranches() {
        List<BranchResponse> data = getAllActiveBranchesUseCase.execute();
        return ResponseEntity.ok(ApiResponse.success(data, "Lấy danh sách chi nhánh thành công!"));
    }

    /**
     * 📘 FRONTEND NOTE: XEM CHI TIẾT THÔNG TIN CỦA 1 CHI NHÁNH THEO ID
     * ------------------------------------------------------------------------
     * 🔹 METHOD: GET
     * 🔹 URL CHUẨN: /api/v1/public/branches/{id} (Ví dụ: /api/v1/public/branches/1)
     * 🔹 HEADERS: Không yêu cầu (Bỏ trống Authorization)
     * 🔹 RESPONSE EX (success): Trả về Object chi tiết của duy nhất chi nhánh đó
     * {
     * "success": true,
     * "code": 200,
     * "message": "Lấy chi tiết thông tin chi nhánh thành công!",
     * "data": {
     * "id": 1,
     * "branchName": "Hair Studio Quận 1 - Premium",
     * "address": "123 Nguyễn Huệ, Quận 1, TP. HCM",
     * "phone": "02877778888",
     * "isActive": true
     * }
     * }
     * 🔹 RESPONSE EX (fail - ID không tồn tại hoặc đã bị xóa mềm): 400 Bad Request
     * {
     * "success": false,
     * "code": 400,
     * "message": "Chi nhánh không tồn tại hoặc đã tạm dừng hoạt động!",
     * "data": null
     * }
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BranchResponse>> getBranchById(@PathVariable Long id) {
        BranchResponse data = getBranchByIdUseCase.execute(id);
        return ResponseEntity.ok(ApiResponse.success(data, "Lấy chi tiết thông tin chi nhánh thành công!"));
    }
}