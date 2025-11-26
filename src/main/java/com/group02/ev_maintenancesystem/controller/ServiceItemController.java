package com.group02.ev_maintenancesystem.controller;

import com.group02.ev_maintenancesystem.dto.request.ServiceItemCreationRequest;
import com.group02.ev_maintenancesystem.dto.request.ServiceItemUpdateRequest;
import com.group02.ev_maintenancesystem.dto.request.StatusUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.ApiResponse;
import com.group02.ev_maintenancesystem.dto.response.ServiceItemResponse;
import com.group02.ev_maintenancesystem.entity.ServiceItem;
import com.group02.ev_maintenancesystem.service.ServiceItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/serviceItem")
public class ServiceItemController {
    @Autowired
    ServiceItemService serviceItemService;

    //Tạo mới 1 item
    @PostMapping("/create")
    public ApiResponse<ServiceItemResponse> createServiceItem(@Valid @RequestBody ServiceItemCreationRequest request){
        return ApiResponse.<ServiceItemResponse>builder()
                .message("Create service item successfully")
                .result(serviceItemService.createServiceItem(request))
                .build();
    }

    //Lấy item theo itemId
    @GetMapping("/{serviceItemId}")
    public ApiResponse<ServiceItemResponse> getServiceItemById(@PathVariable Long serviceItemId){
        return ApiResponse.<ServiceItemResponse>builder()
                .message("Get service item successfully")
                .result(serviceItemService.getServiceItemById(serviceItemId))
                .build();
    }

    //Lấy toàn bộ item
    @GetMapping
    public ApiResponse<Page<ServiceItemResponse>> getAllServiceItem(@RequestParam (defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return ApiResponse.<Page<ServiceItemResponse>>builder()
                .message("Get all vehicle successfully")
                .result(serviceItemService.getAllServiceItem(page, size))
                .build();
    }

    //Lấy item theo id của servicePackage
//    @GetMapping("/servicePackage/{servicePackageId}")
//    public ApiResponse<List<ServiceItemResponse>> getServiceItemByServicePackageId(@PathVariable Long servicePackageId){
//        return ApiResponse.<List<ServiceItemResponse>>builder()
//                .message("Get service item successfully")
//                .result(serviceItemService.getServiceItemBySerivePackageId(servicePackageId))
//                .build();
//    }

    @GetMapping("/search")
    public ApiResponse<Page<ServiceItemResponse>> searchServiceItemByKeyWord(@RequestParam String keyword, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return ApiResponse.<Page<ServiceItemResponse>>builder()
                .message("Get service item successfully")
                .result(serviceItemService.searchServiceItemByName(keyword,page,size))
                .build();
    }

    @PutMapping("/{vehicleId}")
    public ApiResponse<ServiceItemResponse> updateServiceItemById(@PathVariable Long vehicleId, @RequestBody ServiceItemUpdateRequest request){
        return ApiResponse.<ServiceItemResponse>builder()
                .message("Update successfully")
                .result(serviceItemService.updateServiceItemById(vehicleId,request))
                .build();
    }

    @DeleteMapping("/{serviceItemId}")
    public ApiResponse<ServiceItemResponse> deleteServiceItemById(@PathVariable Long serviceItemId){
        return ApiResponse.<ServiceItemResponse>builder()
                .message("Delete service item successfully")
                .result(serviceItemService.deleteServiceItemById(serviceItemId))
                .build();
    }
    @PatchMapping("/{id}/status")
// @PreAuthorize("hasRole('ADMIN')") // Nên phân quyền Admin
    public ApiResponse<ServiceItemResponse> updateStatus(
            @PathVariable Long id,
            @RequestBody StatusUpdateRequest request) {

        return ApiResponse.<ServiceItemResponse>builder()
                .message("Service Item status updated")
                .result(serviceItemService.updateStatus(id, request.getIsActive()))
                .build();
    }
}
