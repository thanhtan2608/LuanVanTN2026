package org.example.lv_be.module.users.application.interfaces.in;

import org.example.lv_be.module.users.application.dto.UpdateStaffRequest;

public interface IUpdateStaffUseCase {
    void execute(Long staffId, UpdateStaffRequest request);
}
