package org.example.lv_be.module.users.application.services;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.common.enums.Role;
import org.example.lv_be.module.users.application.dto.UserProfileResponse;
import org.example.lv_be.module.users.application.interfaces.in.IGetUserProfileUseCase;
import org.example.lv_be.module.users.domain.entity.User;
import org.example.lv_be.module.users.domain.exception.UserDomainException;
import org.example.lv_be.module.users.domain.repository.ICustomerRepository;
import org.example.lv_be.module.users.domain.repository.IEmployeeRepository;
import org.example.lv_be.module.users.domain.repository.IUserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserProfileUseCaseImpl implements IGetUserProfileUseCase {

    private final IUserRepository userRepository;
    private final ICustomerRepository customerRepository;
    private final IEmployeeRepository employeeRepository;

    @Override
    public UserProfileResponse execute(String phone) {
        // 1. Tìm thông tin gốc ở bảng users
        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new UserDomainException("Không tìm thấy thông tin tài khoản!"));

        // 2. Khởi tạo Builder cho DTO Response với các thông tin chung
        UserProfileResponse.UserProfileResponseBuilder responseBuilder = UserProfileResponse.builder()
                .id(user.getId())
                .phone(user.getPhone())
                .fullName(user.getFullName())
                .role(user.getRole())
                .isActive(user.isActive());

        // 3. Rẽ nhánh logic để lấy thông tin mở rộng tùy theo Role
        if (user.getRole() == Role.CUSTOMER) {
            // Nếu là khách hàng -> Query bảng customers lấy điểm và hạng
            customerRepository.findByUserId(user.getId()).ifPresent(customer -> {
                responseBuilder.points(customer.getPoints());
                responseBuilder.memberTier(customer.getMemberTier() != null ? customer.getMemberTier().name() : null);
            });
        } else {
            // Nếu là nội bộ (STAFF, MANAGER, ADMIN...) -> Query bảng employees lấy lương và chi nhánh
            employeeRepository.findByUserId(user.getId()).ifPresent(employee -> {
                responseBuilder.branchId(employee.getBranchId());
                responseBuilder.baseSalary(employee.getBaseSalary());
                responseBuilder.commissionRate(employee.getCommissionRate());
            });
        }

        // 4. Build thành object hoàn chỉnh và trả về
        return responseBuilder.build();
    }
}