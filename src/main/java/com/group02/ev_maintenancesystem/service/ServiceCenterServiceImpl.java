package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.request.ServiceCenterRequest;
import com.group02.ev_maintenancesystem.dto.response.ServiceCenterResponse;
import com.group02.ev_maintenancesystem.entity.ServiceCenter;
import com.group02.ev_maintenancesystem.exception.AppException;
import com.group02.ev_maintenancesystem.exception.ErrorCode;
import com.group02.ev_maintenancesystem.mapper.ServiceCenterMapper;
import com.group02.ev_maintenancesystem.repository.ServiceCenterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServiceCenterServiceImpl implements ServiceCenterService {

    private final ServiceCenterRepository serviceCenterRepository;
    private final ServiceCenterMapper serviceCenterMapper;

    @Override
    @Transactional
    public ServiceCenterResponse createServiceCenter(ServiceCenterRequest request) {
        if (serviceCenterRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.SERVICE_CENTER_NAME_DUPLICATE);
        }
        if (serviceCenterRepository.existsByPhone(request.getPhone())) {
            throw new AppException(ErrorCode.SERVICE_CENTER_PHONE_DUPLICATE);
        }

        ServiceCenter serviceCenter = serviceCenterMapper.toServiceCenter(request);
        ServiceCenter savedCenter = serviceCenterRepository.save(serviceCenter);
        return serviceCenterMapper.toServiceCenterResponse(savedCenter);
    }

    @Override
    @Transactional
    public ServiceCenterResponse updateServiceCenter(Long id, ServiceCenterRequest request) {
        ServiceCenter serviceCenter = serviceCenterRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_CENTER_NOT_FOUND));

        if (serviceCenterRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new AppException(ErrorCode.SERVICE_CENTER_NAME_DUPLICATE);
        }
        if (serviceCenterRepository.existsByPhoneAndIdNot(request.getPhone(), id)) {
            throw new AppException(ErrorCode.SERVICE_CENTER_PHONE_DUPLICATE);
        }

        serviceCenterMapper.updateServiceCenter(request, serviceCenter);
        ServiceCenter updatedCenter = serviceCenterRepository.save(serviceCenter);
        return serviceCenterMapper.toServiceCenterResponse(updatedCenter);
    }

    @Override
    @Transactional
    public void deleteServiceCenter(Long id) {
        ServiceCenter serviceCenter = serviceCenterRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_CENTER_NOT_FOUND));

        // Check for related entities
        if (serviceCenter.getUsers() != null && !serviceCenter.getUsers().isEmpty()) {
            throw new AppException(ErrorCode.CANNOT_DELETE_SERVICE_CENTER);
        }
        if (serviceCenter.getAppointments() != null && !serviceCenter.getAppointments().isEmpty()) {
            throw new AppException(ErrorCode.CANNOT_DELETE_SERVICE_CENTER);
        }
        if (serviceCenter.getInvoices() != null && !serviceCenter.getInvoices().isEmpty()) {
            throw new AppException(ErrorCode.CANNOT_DELETE_SERVICE_CENTER);
        }

        serviceCenterRepository.delete(serviceCenter);
    }

    @Override
    public ServiceCenterResponse getServiceCenterById(Long id) {
        ServiceCenter serviceCenter = serviceCenterRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_CENTER_NOT_FOUND));
        return serviceCenterMapper.toServiceCenterResponse(serviceCenter);
    }

    @Override
    public Page<ServiceCenterResponse> getAllServiceCenters(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<ServiceCenter> centerPage = serviceCenterRepository.findAll(pageable);
        return centerPage.map(serviceCenterMapper::toServiceCenterResponse);
    }

    @Override
    public Page<ServiceCenterResponse> searchServiceCenters(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ServiceCenter> centerPage = serviceCenterRepository.findByNameContainingIgnoreCase(keyword, pageable);
        return centerPage.map(serviceCenterMapper::toServiceCenterResponse);
    }

    // Trong ServiceCenterServiceImpl.java
    @Override
    @Transactional
    public ServiceCenterResponse updateStatus(Long id, Boolean isActive) {
        ServiceCenter center = serviceCenterRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_CENTER_NOT_FOUND));

        center.setActive(isActive);
        return serviceCenterMapper.toServiceCenterResponse(serviceCenterRepository.save(center));
    }
}