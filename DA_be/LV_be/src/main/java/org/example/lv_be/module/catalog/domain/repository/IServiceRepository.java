package org.example.lv_be.module.catalog.domain.repository;

import org.example.lv_be.module.catalog.domain.entity.ServiceItem;
import java.util.List;
import java.util.Optional;

public interface IServiceRepository {
    Optional<ServiceItem> findById(Long id);
    List<ServiceItem> findByCategoryIdAndActiveTrue(Long categoryId);
    List<ServiceItem> findAllActive(); // Lấy dịch vụ có active = true và deleted = false
    boolean existsByName(String name);
    ServiceItem sourceSave(ServiceItem serviceItem);
}