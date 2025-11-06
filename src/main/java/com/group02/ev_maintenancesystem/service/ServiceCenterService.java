package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.request.ServiceCenterRequest;
import com.group02.ev_maintenancesystem.dto.response.ServiceCenterResponse;
import org.springframework.data.domain.Page;

public interface ServiceCenterService {
    ServiceCenterResponse createServiceCenter(ServiceCenterRequest request);
    ServiceCenterResponse updateServiceCenter(Long id, ServiceCenterRequest request);
    void deleteServiceCenter(Long id);
    ServiceCenterResponse getServiceCenterById(Long id);
    Page<ServiceCenterResponse> getAllServiceCenters(int page, int size);
    Page<ServiceCenterResponse> searchServiceCenters(String keyword, int page, int size);
}