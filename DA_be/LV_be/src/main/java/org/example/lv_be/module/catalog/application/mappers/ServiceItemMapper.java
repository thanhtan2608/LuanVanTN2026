package org.example.lv_be.module.catalog.application.mappers;
import org.example.lv_be.module.catalog.application.dto.service.*;
import org.example.lv_be.module.catalog.domain.entity.ServiceItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ServiceItemMapper {
    ServiceItem toDomain(CreateServiceRequest request);
    ServiceResponse toResponse(ServiceItem domain);
}