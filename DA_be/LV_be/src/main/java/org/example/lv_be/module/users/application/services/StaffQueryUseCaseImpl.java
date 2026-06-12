package org.example.lv_be.module.users.application.services;

import lombok.RequiredArgsConstructor;
import org.example.lv_be.common.enums.Role;
import org.example.lv_be.module.users.application.interfaces.IStaffQueryUseCase;
import org.example.lv_be.module.users.domain.entity.User;
import org.example.lv_be.module.users.domain.repository.IUserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StaffQueryUseCaseImpl implements IStaffQueryUseCase {

    // Tiêm Interface của tầng Domain, tuyệt đối không gọi thẳng JPA Repo ở đây
    private final IUserRepository userRepository;

    @Override
    public BigDecimal getBaseSalary(Long staffId) {
        // CÁCH 3: Thợ làm không có lương cứng, chỉ ăn hoa hồng
        return BigDecimal.ZERO;
    }

    @Override
    public LocalTime getShiftStartTime(Long staffId) {
        // Mặc định ca làm việc bắt đầu lúc 8h sáng
        return LocalTime.of(8, 0);
    }

    @Override
    public Map<Long, String> getAllActiveStaffs() {
        // Lấy danh sách thợ từ DB thông qua Domain Repository
        List<User> activeStaffs = userRepository.findActiveStaffsByRole(Role.STAFF);

        // Chuyển đổi List thành Map<ID, Tên> để trả về
        return activeStaffs.stream()
                .collect(Collectors.toMap(
                        User::getId,
                        User::getFullName
                ));
    }
}
