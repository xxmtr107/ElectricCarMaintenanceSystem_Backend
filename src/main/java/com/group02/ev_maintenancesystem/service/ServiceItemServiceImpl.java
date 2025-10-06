package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.request.ServiceItemCreationRequest;
import com.group02.ev_maintenancesystem.dto.request.ServiceItemUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.ServiceItemResponse;
import com.group02.ev_maintenancesystem.entity.ServiceItem;
import com.group02.ev_maintenancesystem.entity.ServicePackage;
import com.group02.ev_maintenancesystem.exception.AppException;
import com.group02.ev_maintenancesystem.exception.ErrorCode;
import com.group02.ev_maintenancesystem.repository.ServiceItemRepository;
import com.group02.ev_maintenancesystem.repository.ServicePackageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceItemServiceImpl implements  ServiceItemService {
    @Autowired
    ServiceItemRepository serviceItemRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ServicePackageRepository servicePackageRepository;

    @Override
    public ServiceItemResponse createServiceItem(ServiceItemCreationRequest request) {
        //Check xem có trùng tên ko
        String serviceItemName = request.getName().trim();
        List<ServiceItem> exitsServiceItemList = serviceItemRepository.findAll();
        boolean exist = exitsServiceItemList.stream()
                .anyMatch(m -> m.getName().trim().equalsIgnoreCase(serviceItemName));
        if(exist){ // có thì quăng lỗi
            throw new AppException(ErrorCode.SERVICE_ITEM_NAME_DUPLICATE);
        }
        ServiceItem serviceItem = new ServiceItem();
        serviceItem.setName(request.getName());
        serviceItem.setPrice(request.getPrice());

        ServiceItem savedserviceItem = serviceItemRepository.save(serviceItem);
        return modelMapper.map(savedserviceItem, ServiceItemResponse.class);
    }

    @Override
    public ServiceItemResponse getServiceItemById(Long serviceItemId) {
        ServiceItem serviceItem = serviceItemRepository.findById(serviceItemId)
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_ITEM_NOT_FOUND));
        return modelMapper.map(serviceItem, ServiceItemResponse.class);
    }

    @Override
    public Page<ServiceItemResponse> getAllServiceItem(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ServiceItem> serviceItem = serviceItemRepository.findAll(pageable);
        if(serviceItem.isEmpty()){
            throw new AppException(ErrorCode.SERVICE_ITEM_NOT_FOUND);
        }
        return serviceItem
                .map(m-> modelMapper.map(m, ServiceItemResponse.class));
    }

    @Override
    public List<ServiceItemResponse> getServiceItemBySerivePackageId(Long servicePackageId) {
        List<ServiceItem> items;
        try {
            items = serviceItemRepository.findByServicePackages_Id(servicePackageId);
        }catch (DataAccessException e){
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        if(items.isEmpty()){
            throw new AppException(ErrorCode.SERVICE_ITEM_NOT_FOUND);
        }
        return items.stream()
                .map(m-> modelMapper.map(m, ServiceItemResponse.class)).toList();
    }

    @Override
    public Page<ServiceItemResponse> searchServiceItemByName(String itemName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ServiceItem> serviceItem = serviceItemRepository.findByNameContainingIgnoreCase(itemName, pageable);
        if(serviceItem.isEmpty()){
            throw new AppException(ErrorCode.SERVICE_ITEM_NOT_FOUND);
        }
        return serviceItem.map(s -> modelMapper.map(s, ServiceItemResponse.class));
    }

    @Override
    public ServiceItemResponse updateServiceItemById(Long serviceItemId,ServiceItemUpdateRequest request) {
        //check xem có id ko
        ServiceItem serviceItem = serviceItemRepository.findById(serviceItemId)
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_ITEM_NOT_FOUND));
        //check xem có trùng name ko
        boolean exist = serviceItemRepository.existsByName(request.getName());
        if(exist){
            throw new AppException(ErrorCode.SERVICE_ITEM_NAME_DUPLICATE);
        }
        if(request.getName() != null && !request.getName().trim().isEmpty()){
            serviceItem.setName(request.getName());
        }
        if(request.getDescription() != null && !request.getDescription().trim().isEmpty()){
            serviceItem.setDescription(request.getDescription());
        }
        if(request.getPrice() != null ){
            serviceItem.setPrice(request.getPrice());
        }
        ServiceItem savedItem = serviceItemRepository.save(serviceItem);
        return modelMapper.map(savedItem, ServiceItemResponse.class);
    }

    @Override
    public ServiceItemResponse deleteServiceItemById(Long serviceItemId) {
        ServiceItem serviceItem = serviceItemRepository.findById(serviceItemId)
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_ITEM_NOT_FOUND));
        serviceItemRepository.delete(serviceItem);
        return modelMapper.map(serviceItem, ServiceItemResponse.class);
    }


}
