package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.constant.PredefinedRole;
import com.group02.ev_maintenancesystem.dto.request.UserRegistrationRequest;
import com.group02.ev_maintenancesystem.dto.request.UserUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.UserResponse;
import com.group02.ev_maintenancesystem.entity.Role;
import com.group02.ev_maintenancesystem.entity.ServiceCenter;
import com.group02.ev_maintenancesystem.entity.User;
import com.group02.ev_maintenancesystem.exception.AppException;
import com.group02.ev_maintenancesystem.exception.ErrorCode;
import com.group02.ev_maintenancesystem.mapper.UserMapper;
import com.group02.ev_maintenancesystem.repository.RoleRepository;
import com.group02.ev_maintenancesystem.repository.ServiceCenterRepository;
import com.group02.ev_maintenancesystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class TechnicianServiceImpl implements TechnicianService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;
    ServiceCenterRepository serviceCenterRepository;

    @Override
    public UserResponse registerTechnician(UserRegistrationRequest request) {
        User technician = userMapper.toUser(request);
        technician.setPassword(passwordEncoder.encode(request.getPassword()));

        Role role = roleRepository.findByName(PredefinedRole.TECHNICIAN)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        technician.setRole(role);
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
        User technician = userRepository.findByIdAndRoleName(technicianId, PredefinedRole.TECHNICIAN)
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
        User technician = userRepository.findByIdAndRoleName(technicianId, PredefinedRole.TECHNICIAN)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserResponse(technician);
    }



    @Override
    public List<UserResponse> getAllTechnicians() {
        Role technicianRole = roleRepository.findByName(PredefinedRole.TECHNICIAN)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        return userRepository.findAllByRole(technicianRole).stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    @Override
    public void deleteTechnician(Long technicianId) {

        User technician = userRepository.findByIdAndRoleName(technicianId, PredefinedRole.TECHNICIAN)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        userRepository.delete(technician);
    }
}
