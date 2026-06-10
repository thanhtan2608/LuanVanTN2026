package org.example.lv_be.module.hairstyles.application.interfaces;

import java.util.List;

public interface ISyncHairstyleServicesUseCase { void execute(Long hairstyleId, List<Long> serviceIds); }
