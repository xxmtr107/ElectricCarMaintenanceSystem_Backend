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
import com.group02.ev_maintenancesystem.repository.TechnicianRepository;
import com.group02.ev_maintenancesystem.repository.RoleRepository;
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
    TechnicianRepository TechnicianRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    TechnicianMapper TechnicianMapper;

    @Override
    public TechnicianResponse registerTechnician(TechnicianRegistrationRequest request) {
        User Technician = TechnicianMapper.toTechnician(request);
        Technician.setPassword(passwordEncoder.encode(request.getPassword()));

        Role role = roleRepository.findByName(PredefinedRole.TECHNICIAN)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        Technician.setRole(role);
        try {
            Technician = TechnicianRepository.save(Technician);
        } catch (DataIntegrityViolationException e) {
            String message = e.getMostSpecificCause().getMessage();
            if (message.contains("UK_username")) {
                throw new AppException(ErrorCode.USERNAME_EXISTED);
            }
            else if(message.contains("UK_email")) {
                throw new AppException(ErrorCode.EMAIL_EXISTED);
            }
        }
        return TechnicianMapper.toTechnicianResponse(Technician);
    }

    @Override
    public TechnicianResponse updateTechnician(Long TechnicianId, TechnicianUpdateRequest request) {
        User Technician = TechnicianRepository.findById(TechnicianId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        TechnicianMapper.updateTechnician(request, Technician);
        Technician.setPassword(passwordEncoder.encode(request.getPassword()));

        return TechnicianMapper.toTechnicianResponse(TechnicianRepository.save(Technician));
    }

    @Override
    public TechnicianResponse getTechnicianById(Long TechnicianId) {
        return TechnicianMapper.toTechnicianResponse(
                TechnicianRepository.findById(TechnicianId)
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND))
        );
    }

    @Override
    public List<TechnicianResponse> getAllTechnicians() {
        return TechnicianRepository.findAll().stream()
                .map(TechnicianMapper::toTechnicianResponse)
                .toList();
    }

    @Override
    public void deleteTechnician(Long TechnicianId) {
        TechnicianRepository.deleteById(TechnicianId);
    }
}
