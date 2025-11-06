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
public class StaffServiceImpl implements StaffService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;
    ServiceCenterRepository serviceCenterRepository;
    @Override
    public UserResponse registerStaff(UserRegistrationRequest request) {
        User staff = userMapper.toUser(request);
        staff.setPassword(passwordEncoder.encode(request.getPassword()));

        Role role = roleRepository.findByName(PredefinedRole.STAFF)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        staff.setRole(role);
        if (request.getServiceCenterId() != null) {
            ServiceCenter center = serviceCenterRepository.findById(request.getServiceCenterId())
                    .orElseThrow(() -> new AppException(ErrorCode.SERVICE_CENTER_NOT_FOUND));
            staff.setServiceCenter(center);
        }
        try {
            staff = userRepository.save(staff);
        } catch (DataIntegrityViolationException e) {
            String message = e.getMostSpecificCause().getMessage();
            if (message.contains("UK_username")) {
                throw new AppException(ErrorCode.USERNAME_EXISTED);
            }
            else if(message.contains("UK_email")) {
                throw new AppException(ErrorCode.EMAIL_EXISTED);
            }
        }
        return userMapper.toUserResponse(staff);
    }

    @Override
    public UserResponse updateStaff(Long staffId, UserUpdateRequest request) {
        User staff = userRepository.findByIdAndRoleName(staffId, PredefinedRole.STAFF)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (request.getServiceCenterId() != null) {
            ServiceCenter center = serviceCenterRepository.findById(request.getServiceCenterId())
                    .orElseThrow(() -> new AppException(ErrorCode.SERVICE_CENTER_NOT_FOUND));
            staff.setServiceCenter(center);
        }
        userMapper.updateUser(request, staff);


        return userMapper.toUserResponse(userRepository.save(staff));
    }


    @Override
    public UserResponse getStaffById(Long staffId) {
        User staff = userRepository.findByIdAndRoleName(staffId, PredefinedRole.STAFF)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserResponse(staff);
    }



    @Override
    public List<UserResponse> getAllStaffs() {
        Role staffRole = roleRepository.findByName(PredefinedRole.STAFF)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        return userRepository.findAllByRole(staffRole).stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    @Override
    public void deleteStaff(Long staffId) {

        User staff = userRepository.findByIdAndRoleName(staffId, PredefinedRole.STAFF)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        userRepository.delete(staff);
    }
}
