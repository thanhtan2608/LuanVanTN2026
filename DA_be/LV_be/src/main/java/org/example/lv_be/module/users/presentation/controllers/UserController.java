package org.example.lv_be.module.users.presentation.controllers;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.module.users.application.dto.UserProfileResponse;
// import org.example.lv_be.module.users.application.interfaces.in.IStaffQueryUseCase;
import org.example.lv_be.module.users.application.interfaces.in.IStaffQueryUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

     private final IStaffQueryUseCase staffQueryUseCase;

    /**
     * [DÀNH CHO FRONTEND] API Lấy danh sách thợ cắt tóc đang làm việc tại 1 chi nhánh
     * - Method: GET
     * - Params: ?branchId=1
     * - Note: API này không cần token (Public) để khách hàng chưa đăng nhập
     * vẫn xem được thợ trước khi đặt lịch.
     */
    @GetMapping("/staffs")
    public ResponseEntity<List<UserProfileResponse>> getActiveStaffsByBranch(
            @RequestParam(name = "branchId", required = false) Long branchId) {

        List<UserProfileResponse> staffs = staffQueryUseCase.execute(branchId);
        return ResponseEntity.ok(staffs);
    }
}