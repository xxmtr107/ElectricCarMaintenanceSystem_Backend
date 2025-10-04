package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.request.TechnicianRegistrationRequest;
import com.group02.ev_maintenancesystem.dto.request.TechnicianUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.TechnicianResponse;

import java.util.List;

public interface TechnicianService {
    TechnicianResponse registerTechnician(TechnicianRegistrationRequest request);
    TechnicianResponse updateTechnician(Long technicianId,TechnicianUpdateRequest request);
    TechnicianResponse getTechnicianById(Long technicianId);
    List<TechnicianResponse> getAllTechnicians();
    void deleteTechnician(Long technicianId);
}
