package org.example.lv_be.module.users.application.interfaces.in;

import org.example.lv_be.module.users.application.dto.CreateStaffRequest;

public interface ICreateStaffUseCase {
    /**
     * Thực thi luồng tạo tài khoản nhân sự nội bộ (Do Admin/Manager thực hiện)
     *
     * @param request Chứa thông tin cá nhân, chức vụ, mức lương, hoa hồng và chi nhánh làm việc
     */
    void execute(CreateStaffRequest request);
}