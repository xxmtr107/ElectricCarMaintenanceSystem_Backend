package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.constant.PredefinedRole;
import com.group02.ev_maintenancesystem.dto.request.TechnicianRegistrationRequest;
import com.group02.ev_maintenancesystem.dto.request.TechnicianUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.TechnicianResponse;
import com.group02.ev_maintenancesystem.entity.Role;
import com.group02.ev_maintenancesystem.entity.User;
import com.group02.ev_maintenancesystem.exception.AppException;
import com.group02.ev_maintenancesystem.exception.ErrorCode;
import com.group02.ev_maintenancesystem.mapper.TechnicianMapper;
import com.group02.ev_maintenancesystem.repository.RoleRepository;
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
    UserRepository technicianRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    TechnicianMapper technicianMapper;

    @Override
    public TechnicianResponse registerTechnician(TechnicianRegistrationRequest request) {
        User technician = technicianMapper.toTechnician(request);
        technician.setPassword(passwordEncoder.encode(request.getPassword()));

        Role role = roleRepository.findByName(PredefinedRole.TECHNICIAN)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        technician.setRole(role);
        try {
            technician = technicianRepository.save(technician);
        } catch (DataIntegrityViolationException e) {
            String message = e.getMostSpecificCause().getMessage();
            if (message.contains("UK_username")) {
                throw new AppException(ErrorCode.USERNAME_EXISTED);
            }
            else if(message.contains("UK_email")) {
                throw new AppException(ErrorCode.EMAIL_EXISTED);
            }
        }
        return technicianMapper.toTechnicianResponse(technician);
    }

    @Override
    public TechnicianResponse updateTechnician(Long technicianId, TechnicianUpdateRequest request) {
        User technician = technicianRepository.findByIdAndRoleName(technicianId, PredefinedRole.TECHNICIAN)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        technicianMapper.updateTechnician(request, technician);
        technician.setPassword(passwordEncoder.encode(request.getPassword()));


        return technicianMapper.toTechnicianResponse(technicianRepository.save(technician));
    }


    @Override
    public TechnicianResponse getTechnicianById(Long technicianId) {
        User technician = technicianRepository.findByIdAndRoleName(technicianId, PredefinedRole.TECHNICIAN)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return technicianMapper.toTechnicianResponse(technician);
    }



    @Override
    public List<TechnicianResponse> getAllTechnicians() {
        Role technicianRole = roleRepository.findByName(PredefinedRole.TECHNICIAN)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        return technicianRepository.findAllByRole(technicianRole).stream()
                .map(technicianMapper::toTechnicianResponse)
                .toList();
    }

    @Override
    public void deleteTechnician(Long technicianId) {

        User technician = technicianRepository.findByIdAndRoleName(technicianId, PredefinedRole.TECHNICIAN)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        technicianRepository.delete(technician);
    }
}
