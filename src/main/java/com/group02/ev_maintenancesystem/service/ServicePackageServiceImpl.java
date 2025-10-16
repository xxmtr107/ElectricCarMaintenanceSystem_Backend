package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.request.ServicePackageCreationRequest;
import com.group02.ev_maintenancesystem.dto.request.ServicePackageUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.ServicePackageResponse;
import com.group02.ev_maintenancesystem.entity.ServicePackage;
import com.group02.ev_maintenancesystem.exception.AppException;
import com.group02.ev_maintenancesystem.exception.ErrorCode;
import com.group02.ev_maintenancesystem.repository.ServicePackageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicePackageServiceImpl implements ServicePackageService{
    @Autowired
    ServicePackageRepository servicePackageRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ServicePackageResponse createServicePackage(ServicePackageCreationRequest request) {
        //check xem có trùng name ko
        String servicePackageName = request.getName().trim();
        List<ServicePackage> servicePackageList =  servicePackageRepository.findAll();
        boolean exits = servicePackageList.stream()
                .anyMatch(m-> m.getName().trim().equalsIgnoreCase(servicePackageName));
        if(exits){
            throw new AppException(ErrorCode.SERVICE_PACKAGE_NAME_DUPLICATE);
        }
        ServicePackage servicePackage = new ServicePackage();
        servicePackage.setName(request.getName());
//        servicePackage.setPrice(request.getPrice());
        servicePackage.setDescription(request.getDescription());

        servicePackageRepository.save(servicePackage);

        return modelMapper.map(servicePackage, ServicePackageResponse.class);
    }

    @Override
    public Page<ServicePackageResponse> getAllServicePackage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ServicePackage> servicePackage = servicePackageRepository.findAll(pageable);
        if(servicePackage.isEmpty()){
            throw new AppException(ErrorCode.SERVICE_PACKAGE_NOT_FOUND);
        }
        return servicePackage.map(servicePackage1 -> modelMapper.map(servicePackage1, ServicePackageResponse.class));

    }

    @Override
    public ServicePackageResponse getServicePackageById(Long servicePackageId) {
        //check xem id có tồn tại hay ko
        ServicePackage servicePackage = servicePackageRepository.findById(servicePackageId).
                orElseThrow(() -> new AppException(ErrorCode.SERVICE_PACKAGE_NOT_FOUND));
        return modelMapper.map(servicePackage, ServicePackageResponse.class);
    }

    @Override
    public ServicePackageResponse updateServicePackageById(Long servicePackageId, ServicePackageUpdateRequest request) {
        // check xem id có tồn tại hay ko
        ServicePackage servicePackage = servicePackageRepository.findById(servicePackageId)
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_PACKAGE_NOT_FOUND));
        //check xem có trùng tên hay ko
        String name = request.getName().trim();
        boolean exits = servicePackageRepository.existsByName(name);
        if(exits){
            throw new AppException(ErrorCode.SERVICE_PACKAGE_NAME_DUPLICATE);
        }
        if(request.getName() != null && !request.getName().trim().isEmpty()){
            servicePackage.setName(request.getName());
        }
        if(request.getDescription() != null && !request.getDescription().trim().isEmpty()){
            servicePackage.setDescription(request.getDescription());
        }
//        if(request.getPrice() != null){
//            servicePackage.setPrice(request.getPrice());
//        }
        ServicePackage savePackage = servicePackageRepository.save(servicePackage);
        return modelMapper.map(servicePackage, ServicePackageResponse.class);
    }

    @Override
    public ServicePackageResponse deleteServicePackageById(Long servicePackageId) {
        ServicePackage servicePackage = servicePackageRepository.findById(servicePackageId)
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_PACKAGE_NOT_FOUND));
        servicePackageRepository.delete(servicePackage);
        return modelMapper.map(servicePackage, ServicePackageResponse.class);
    }
}
