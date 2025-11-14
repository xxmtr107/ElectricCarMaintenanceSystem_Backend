package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.request.SparePartCreateRequest;
import com.group02.ev_maintenancesystem.dto.request.SparePartUpdateRequest;
import com.group02.ev_maintenancesystem.dto.request.StockUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.SparePartResponse;
import com.group02.ev_maintenancesystem.entity.SparePart;
import com.group02.ev_maintenancesystem.exception.AppException;
import com.group02.ev_maintenancesystem.exception.ErrorCode;
import com.group02.ev_maintenancesystem.repository.ModelPackageItemRepository;
import com.group02.ev_maintenancesystem.repository.SparePartRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SparePartServiceImpl implements  SparePartService {

    @Autowired
    SparePartRepository sparePartRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ModelPackageItemRepository modelPackageItemRepository;

    //Lấy tất cả phụ tùng theo trang
    @Override
    public Page<SparePartResponse> getAllSparePart(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SparePart> sparePartPage = sparePartRepository.findAll(pageable);
        if(sparePartPage.isEmpty()) {
            throw new AppException(ErrorCode.SPARE_PART_NOT_FOUND);
        }
        return sparePartPage.map(sparePartResponse -> modelMapper.map(sparePartResponse, SparePartResponse.class));
    }


    //Lấy phụ tùng theo id
    @Override
    public SparePartResponse getSparePartById(Long id) {
        SparePart sparePart = sparePartRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SPARE_PART_NOT_FOUND));
        return modelMapper.map(sparePart, SparePartResponse.class);
    }

    @Override
    @Transactional
    public SparePartResponse createSparePart(SparePartCreateRequest sparePart) {
        if(sparePartRepository.existsByPartNumber(sparePart.getPartNumber())){
            throw new AppException(ErrorCode.SPARE_PART_NUMBER_DUPLICATE);
        }
        SparePart saveSparePart = new SparePart();
        saveSparePart.setPartNumber(sparePart.getPartNumber());
        saveSparePart.setName(sparePart.getName());
        saveSparePart.setUnitPrice(sparePart.getUnitPrice());
        saveSparePart.setMinimumStockLevel(sparePart.getMinimumStockLevel());
        saveSparePart.setQuantityInStock(sparePart.getQuantityInStock());
        saveSparePart.setCategoryName(sparePart.getCategoryName());
        saveSparePart.setCategoryCode(sparePart.getCategoryCode());

        sparePartRepository.save(saveSparePart);

        return modelMapper.map(sparePart, SparePartResponse.class);
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

        String newNameFromRequest = request.getName();
        if (newNameFromRequest != null) {
            String trimmedNewName = newNameFromRequest.trim();
            if (!trimmedNewName.isBlank() && !trimmedNewName.equalsIgnoreCase(sparePart.getName())) {
                Optional<SparePart> existingPart = sparePartRepository.findByNameIgnoreCaseAndIdNot(trimmedNewName, id);
                if (existingPart.isPresent()) {
                    throw new AppException(ErrorCode.SPARE_PART_NAME_DUPLICATE);
                } else {
                    sparePart.setName(trimmedNewName);
                }
            }
        }

        BigDecimal newPrice = request.getUnitPrice();
        if (newPrice != null) {
            if (newPrice.compareTo(BigDecimal.ZERO) > 0) {
                sparePart.setUnitPrice(newPrice);
            }
        }

        Integer newMinStock = request.getMinimumStockLevel();
        if (newMinStock != null) {
            if (newMinStock >= 0) {
                sparePart.setMinimumStockLevel(newMinStock);
            }
        }

        if (request.getCategoryName() != null) {
            sparePart.setCategoryName(request.getCategoryName());
        }
        if (request.getCategoryCode() != null) {
            sparePart.setCategoryCode(request.getCategoryCode());
        }

        SparePart updatedSparePart = sparePartRepository.save(sparePart);
        return modelMapper.map(updatedSparePart, SparePartResponse.class);
    }

    @Override
    @Transactional
    public SparePartResponse updateStock(Long id, StockUpdateRequest request) {
        SparePart sparePart = sparePartRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SPARE_PART_NOT_FOUND));

        int currentStock = sparePart.getQuantityInStock();
        int stockChange = request.getChangeQuantity();
        int newStock = currentStock + stockChange;

        if(newStock <0){
            throw new AppException(ErrorCode.STOCK_QUANTITY_INVALID);
        }
        sparePart.setQuantityInStock(newStock);
        SparePart updateSparePart = sparePartRepository.save(sparePart);
        return modelMapper.map(updateSparePart, SparePartResponse.class);
    }

    @Override
    public List<SparePartResponse> getSparePartsByModelId(Long modelId) {
        // Gọi Query DISTINCT đã viết trong Repository
        List<SparePart> parts = modelPackageItemRepository.findDistinctSparePartsByModelId(modelId);

        return parts.stream()
                .map(part -> modelMapper.map(part, SparePartResponse.class))
                .collect(Collectors.toList());
    }

}
