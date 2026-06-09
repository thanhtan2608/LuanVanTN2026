package org.example.lv_be.module.branches.application.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class BranchResponse {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private LocalTime openTime;
    private LocalTime closeTime;
    private boolean isActive;
    private LocalDateTime createdAt;
}