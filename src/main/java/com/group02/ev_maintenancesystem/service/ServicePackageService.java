package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.request.ServicePackageCreationRequest;
import com.group02.ev_maintenancesystem.dto.request.ServicePackageUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.ServicePackageResponse;
import com.group02.ev_maintenancesystem.entity.ServicePackage;
import org.springframework.data.domain.Page;

public interface ServicePackageService {
    ServicePackageResponse createServicePackage(ServicePackageCreationRequest request);
    Page<ServicePackageResponse> getAllServicePackage(int page, int size);
    ServicePackageResponse getServicePackageById(Long servicePackageId);
    ServicePackageResponse updateServicePackageById(Long servicePackageId, ServicePackageUpdateRequest request);
    ServicePackageResponse deleteServicePackageById(Long servicePackageId);

}
