package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.request.SparePartCreateRequest;
import com.group02.ev_maintenancesystem.dto.request.SparePartUpdateRequest;
import com.group02.ev_maintenancesystem.dto.request.StockUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.SparePartResponse;
import com.group02.ev_maintenancesystem.entity.SparePart;
import org.springframework.data.domain.Page;

public interface SparePartService {
    Page<SparePartResponse> getAllSparePart(int page, int size);
    SparePartResponse getSparePartById(Long id);
    SparePartResponse createSparePart(SparePartCreateRequest sparePart);
    SparePartResponse deleteSparePartById(Long id);
    SparePartResponse updateSparePart(Long id, SparePartUpdateRequest request);
    SparePartResponse updateStock(Long id, StockUpdateRequest request);
}
