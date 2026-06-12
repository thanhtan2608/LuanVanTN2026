package org.example.lv_be.module.payroll.infrastructure.persistence.mapper;

import org.example.lv_be.module.payroll.domain.entity.Attendance;
import org.example.lv_be.module.payroll.domain.entity.CommissionLog;
import org.example.lv_be.module.payroll.domain.entity.Payroll;
import org.example.lv_be.module.payroll.infrastructure.persistence.entity.AttendanceJpaEntity;
import org.example.lv_be.module.payroll.infrastructure.persistence.entity.CommissionLogJpaEntity;
import org.example.lv_be.module.payroll.infrastructure.persistence.entity.PayrollJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PayrollPersistenceMapper {
    AttendanceJpaEntity toJpaEntity(Attendance domain);
    Attendance toDomainEntity(AttendanceJpaEntity jpaEntity);

    CommissionLogJpaEntity toJpaEntity(CommissionLog domain);
    CommissionLog toDomainEntity(CommissionLogJpaEntity jpaEntity);

    PayrollJpaEntity toJpaEntity(Payroll domain);
    Payroll toDomainEntity(PayrollJpaEntity jpaEntity);
}