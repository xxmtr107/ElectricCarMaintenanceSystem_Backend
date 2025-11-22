package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.request.UserRegistrationRequest;
import com.group02.ev_maintenancesystem.dto.request.UserUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.UserResponse;
import com.group02.ev_maintenancesystem.entity.ServiceCenter;
import com.group02.ev_maintenancesystem.entity.User;
import com.group02.ev_maintenancesystem.enums.Role;
import com.group02.ev_maintenancesystem.exception.AppException;
import com.group02.ev_maintenancesystem.exception.ErrorCode;
import com.group02.ev_maintenancesystem.mapper.UserMapper;
import com.group02.ev_maintenancesystem.repository.ServiceCenterRepository;
import com.group02.ev_maintenancesystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class TechnicianServiceImpl implements TechnicianService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;
    ServiceCenterRepository serviceCenterRepository;

    @Override
    public UserResponse registerTechnician(UserRegistrationRequest request) {
        User technician = userMapper.toUser(request);
        technician.setPassword(passwordEncoder.encode(request.getPassword()));

        technician.setRole(Role.TECHNICIAN);
        if (request.getServiceCenterId() != null) {
            ServiceCenter center = serviceCenterRepository.findById(request.getServiceCenterId())
                    .orElseThrow(() -> new AppException(ErrorCode.SERVICE_CENTER_NOT_FOUND));
            technician.setServiceCenter(center);
        }

        try {
            technician = userRepository.save(technician);
        } catch (DataIntegrityViolationException e) {
            String message = e.getMostSpecificCause().getMessage();
            if (message.contains("UK_username")) {
                throw new AppException(ErrorCode.USERNAME_EXISTED);
            }
            else if(message.contains("UK_email")) {
                throw new AppException(ErrorCode.EMAIL_EXISTED);
            }
        }
        return userMapper.toUserResponse(technician);
    }

    @Override
    public UserResponse updateTechnician(Long technicianId, UserUpdateRequest request) {
        User technician = userRepository.findByIdAndRole(technicianId, Role.TECHNICIAN)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (request.getServiceCenterId() != null) {
            ServiceCenter center = serviceCenterRepository.findById(request.getServiceCenterId())
                    .orElseThrow(() -> new AppException(ErrorCode.SERVICE_CENTER_NOT_FOUND));
            technician.setServiceCenter(center);
        }
        userMapper.updateUser(request, technician);

        return userMapper.toUserResponse(userRepository.save(technician));
    }


    @Override
    public UserResponse getTechnicianById(Long technicianId) {
        User technician = userRepository.findByIdAndRole(technicianId, Role.TECHNICIAN)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserResponse(technician);
    }



    @Override
    public List<UserResponse> getAllTechnicians(Authentication authentication) { // Thêm Authentication
        User currentUser = getAuthenticatedUser(authentication);

        if (currentUser.isAdmin()) {
            // 1. ADMIN: Lấy tất cả KTV
            return userRepository.findAllByRoleOrderByCreatedAtDesc(Role.TECHNICIAN).stream()
                    .map(userMapper::toUserResponse)
                    .toList();
        }
        else if (currentUser.isStaff()) {
            // 2. STAFF: Lọc theo trung tâm của Staff
            ServiceCenter center = currentUser.getServiceCenter();
            if (center == null) {
                log.warn("Staff {} has no assigned service center.", currentUser.getUsername());
                return Collections.emptyList(); // Staff không có trung tâm
            }
            Long centerId = center.getId();
            return userRepository.findAllByRoleAndServiceCenterIdOrderByCreatedAtDesc(Role.TECHNICIAN, centerId)
                    .stream()
                    .map(obj -> userMapper.toUserResponse((User) obj))
                    .toList();
        }

        // 3. Các vai trò khác (Customer, Tech) không được xem danh sách này
        throw new AppException(ErrorCode.UNAUTHORIZED);
    }

    @Override
    public void deleteTechnician(Long technicianId) {

        User technician = userRepository.findByIdAndRole(technicianId, Role.TECHNICIAN)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        userRepository.delete(technician);
    }

    private User getAuthenticatedUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

}
