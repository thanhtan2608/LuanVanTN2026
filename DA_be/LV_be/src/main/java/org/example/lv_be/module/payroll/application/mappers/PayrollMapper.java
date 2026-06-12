package org.example.lv_be.module.payroll.application.mappers;

import org.example.lv_be.module.payroll.application.dto.response.AttendanceResponse;
import org.example.lv_be.module.payroll.application.dto.response.PayrollResponse;
import org.example.lv_be.module.payroll.domain.entity.Attendance;
import org.example.lv_be.module.payroll.domain.entity.Payroll;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PayrollMapper {

    /**
     * Chuyển đổi từ Entity Chấm công sang DTO trả về cho Controller
     */
    AttendanceResponse toAttendanceResponse(Attendance attendance);

    /**
     * Chuyển đổi từ Entity Bảng lương sang DTO.
     * @param payroll Entity Bảng lương lấy từ DB
     * @param staffName Tên nhân viên (Được truyền từ Service sau khi gọi sang module Users)
     */
    @Mapping(target = "staffName", source = "staffName")
    PayrollResponse toPayrollResponse(Payroll payroll, String staffName);
}