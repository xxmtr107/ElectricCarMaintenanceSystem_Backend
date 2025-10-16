package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.request.ServiceItemCreationRequest;
import com.group02.ev_maintenancesystem.dto.request.ServiceItemUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.ServiceItemResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ServiceItemService {
    ServiceItemResponse createServiceItem(ServiceItemCreationRequest request);
    ServiceItemResponse getServiceItemById(Long serviceItemId);
    Page<ServiceItemResponse> getAllServiceItem(int page, int size);
//    List<ServiceItemResponse> getServiceItemBySerivePackageId(Long servicePackageId);
    Page<ServiceItemResponse> searchServiceItemByName(String itemName, int page, int size);
    ServiceItemResponse updateServiceItemById(Long serviceItemId, ServiceItemUpdateRequest request);
    ServiceItemResponse deleteServiceItemById(Long serviceItemId);
}
