package org.example.lv_be.module.hairstyles.domain.repository;

import org.example.lv_be.common.enums.FaceShape;
import org.example.lv_be.module.hairstyles.domain.entity.Hairstyle;
import java.util.List;
import java.util.Optional;

public interface IHairstyleRepository {
    Optional<Hairstyle> findById(Long id);
    List<Hairstyle> findByFaceShapeAndActiveTrue(FaceShape faceShape); // Phục vụ luồng AI gợi ý kiểu tóc theo dáng mặt
    List<Hairstyle> findAllActive(); // Lấy tất cả các kiểu tóc phục vụ danh sách quản trị hoặc catalogue công khai
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
    Hairstyle sourceSave(Hairstyle hairstyle);
}