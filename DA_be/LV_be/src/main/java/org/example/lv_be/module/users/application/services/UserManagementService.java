package org.example.lv_be.module.users.application.services;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.common.enums.Role;
import org.example.lv_be.core.exception.AppException;
import org.example.lv_be.module.users.application.dto.CreateStaffRequest;
import org.example.lv_be.module.users.application.dto.UpdateStaffRequest;
import org.example.lv_be.module.users.application.dto.UserProfileResponse;
import org.example.lv_be.module.users.application.interfaces.IPasswordHasher;
import org.example.lv_be.module.users.application.mappers.UserMapper;
import org.example.lv_be.module.users.domain.entity.User;
import org.example.lv_be.module.users.domain.repository.IUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserManagementService {

    private final IUserRepository userRepository;
    private final IPasswordHasher passwordHasher;
    private final UserMapper userMapper;

    // 1. LẤY DANH SÁCH NHÂN VIÊN (Chỉ lấy MANAGER và STAFF)
    public List<UserProfileResponse> getAllStaffs() {
        List<User> staffs = userRepository.findByRoleIn(List.of(Role.MANAGER, Role.STAFF, Role.ADMIN));
        return staffs.stream()
                .map(userMapper::toProfileResponse)
                .toList();
    }

    // 2. TẠO MỚI NHÂN VIÊN
    @Transactional
    public UserProfileResponse createStaff(CreateStaffRequest request) {
        // 1. Kiểm tra bảo mật
        if (request.getRole() == Role.ADMIN) {
            throw new AppException(HttpStatus.FORBIDDEN, "Không được phép tạo tài khoản ADMIN ở đây");
        }

        // 2. Tìm kiếm User (Bao gồm cả người đã bị xóa mềm)
        Optional<User> existingUserOpt = userRepository.findByPhoneIncludingDeleted(request.getPhone());

        User staffToSave;

        if (existingUserOpt.isPresent()) {
            staffToSave = existingUserOpt.get();

            if (!staffToSave.isDeleted()) {
                // Đã tồn tại và đang hoạt động -> Báo lỗi
                throw new AppException(HttpStatus.CONFLICT, "Số điện thoại này đã tồn tại trong hệ thống!");
            } else {
                // Đã bị xóa mềm -> Phục hồi (Restore) tài khoản
                staffToSave.setDeleted(false);
                staffToSave.setActive(true); // Mở khóa lại nếu trước đó bị khóa
            }
        } else {
            // Chưa từng tồn tại -> Khởi tạo mới
            staffToSave = new User();
            staffToSave.setPhone(request.getPhone());
        }

        // 3. Cập nhật các thông tin mới (Dùng chung cho cả luồng Tạo mới và Phục hồi)
        staffToSave.setFullName(request.getFullName());
        staffToSave.setPassword(passwordHasher.encode(request.getPassword()));
        staffToSave.setRole(request.getRole());
        staffToSave.setBranchId(request.getBranchId());

        // 4. Lưu xuống DB
        User savedStaff = userRepository.save(staffToSave);
        return userMapper.toProfileResponse(savedStaff);
    }

    // 3. CẬP NHẬT NHÂN VIÊN
    @Transactional
    public UserProfileResponse updateStaff(Long id, UpdateStaffRequest request) {
        User staff = userRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy nhân viên!"));

        // Cập nhật thông tin
        staff.setFullName(request.getFullName());
        staff.setBranchId(request.getBranchId());
        if (request.getIsActive() != null) {
            if (request.getIsActive()) {
                staff.setActive(true); // Nếu Entity chưa có hàm setActive thì bạn tự thêm vào class User ở Domain nhé
            } else {
                staff.lockAccount(); // Dùng hàm lockAccount đã viết ở Domain
            }
        }

        User updatedStaff = userRepository.save(staff);
        return userMapper.toProfileResponse(updatedStaff);
    }

    // 4. XÓA (SOFT DELETE) NHÂN VIÊN
    @Transactional
    public void deleteStaff(Long id) {
        User staff = userRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Không tìm thấy nhân viên!"));

        // Không cho phép Admin xóa chính mình hoặc Admin khác
        if (staff.getRole() == Role.ADMIN) {
            throw new AppException(HttpStatus.FORBIDDEN, "Không được phép xóa tài khoản ADMIN!");
        }

        staff.setDeleted(true);
        userRepository.save(staff);
    }
}