package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.request.SparePartCreateRequest;
import com.group02.ev_maintenancesystem.dto.request.SparePartUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.SparePartResponse;
import com.group02.ev_maintenancesystem.entity.SparePart;
import com.group02.ev_maintenancesystem.exception.AppException;
import com.group02.ev_maintenancesystem.exception.ErrorCode;
import com.group02.ev_maintenancesystem.repository.ModelPackageItemRepository;
import com.group02.ev_maintenancesystem.repository.SparePartRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SparePartServiceImpl implements SparePartService {

    private final SparePartRepository sparePartRepository;
    private final ModelPackageItemRepository modelPackageItemRepository;
    private final ModelMapper modelMapper;
    // Không cần inject InventoryService nữa

    @Override
    public Page<SparePartResponse> getAllSparePart(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<SparePart> sparePartPage = sparePartRepository.findAll(pageable);
        if(sparePartPage.isEmpty()) {
            return Page.empty(pageable);
        }
        return sparePartPage.map(part -> modelMapper.map(part, SparePartResponse.class));
    }

    @Override
    public SparePartResponse getSparePartById(Long id) {
        SparePart sparePart = sparePartRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SPARE_PART_NOT_FOUND));
        return modelMapper.map(sparePart, SparePartResponse.class);
    }

    @Override
    @Transactional
    public SparePartResponse createSparePart(SparePartCreateRequest request) {
        if (sparePartRepository.existsByPartNumber(request.getPartNumber())) {
            throw new AppException(ErrorCode.SPARE_PART_NUMBER_DUPLICATE);
        }

        SparePart sparePart = new SparePart();
        sparePart.setPartNumber(request.getPartNumber());
        sparePart.setName(request.getName());
        sparePart.setUnitPrice(request.getUnitPrice());
        sparePart.setCategoryName(request.getCategoryName());
        sparePart.setCategoryCode(request.getCategoryCode());

        SparePart savedPart = sparePartRepository.save(sparePart);

        // CHỈ TẠO SPARE PART, KHÔNG TẠO INVENTORY NỮA

        return modelMapper.map(savedPart, SparePartResponse.class);
    }

    @Override
    @Transactional
    public SparePartResponse deleteSparePartById(Long id) {
        SparePart deleteSparePart = sparePartRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SPARE_PART_NOT_FOUND));
        sparePartRepository.delete(deleteSparePart);
        return modelMapper.map(deleteSparePart, SparePartResponse.class);
    }

    @Override
    @Transactional
    public SparePartResponse updateSparePart(Long id, SparePartUpdateRequest request) {
        SparePart sparePart = sparePartRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SPARE_PART_NOT_FOUND));

        if (request.getName() != null && !request.getName().isBlank()) {
            sparePart.setName(request.getName());
        }
        if (request.getUnitPrice() != null && request.getUnitPrice().compareTo(BigDecimal.ZERO) > 0) {
            sparePart.setUnitPrice(request.getUnitPrice());
        }
        if (request.getCategoryName() != null) sparePart.setCategoryName(request.getCategoryName());
        if (request.getCategoryCode() != null) sparePart.setCategoryCode(request.getCategoryCode());

        SparePart updatedSparePart = sparePartRepository.save(sparePart);
        return modelMapper.map(updatedSparePart, SparePartResponse.class);
    }

    @Override
    public List<SparePartResponse> getSparePartsByModelId(Long modelId) {
        List<SparePart> parts = modelPackageItemRepository.findDistinctSparePartsByModelId(modelId);
        return parts.stream()
                .map(part -> modelMapper.map(part, SparePartResponse.class))
                .collect(Collectors.toList());
    }
    // Trong SparePartServiceImpl.java
    @Override
    @Transactional
    public SparePartResponse updateStatus(Long id, Boolean isActive) {
        SparePart part = sparePartRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SPARE_PART_NOT_FOUND));

        part.setActive(isActive);
        return modelMapper.map(sparePartRepository.save(part), SparePartResponse.class);
    }
}