package com.group02.ev_maintenancesystem.controller;

import com.group02.ev_maintenancesystem.dto.request.SparePartCreateRequest;
import com.group02.ev_maintenancesystem.dto.request.SparePartUpdateRequest;
import com.group02.ev_maintenancesystem.dto.request.StockUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.ApiResponse;
import com.group02.ev_maintenancesystem.dto.response.SparePartResponse;
import com.group02.ev_maintenancesystem.service.SparePartServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
@RequestMapping("/spareParts")
public class SparePartController {
    @Autowired
    SparePartServiceImpl sparePartService;

    @PostMapping("/create")
    public ApiResponse<SparePartResponse> createSparePart(@Valid @RequestBody SparePartCreateRequest sparePartCreateRequest) {
        return ApiResponse.<SparePartResponse>builder()
                .message("Create spare part successfully")
                .result(sparePartService.createSparePart(sparePartCreateRequest))
                .build();
    }

    @GetMapping
    public ApiResponse<Page<SparePartResponse>> getAllSparePart(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return ApiResponse.<Page<SparePartResponse>>builder()
                .message("Get all spare part successfully")
                .result(sparePartService.getAllSparePart(page, size))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<SparePartResponse> getSparePartById(@PathVariable Long id){
        return ApiResponse.<SparePartResponse>builder()
                .message("Get spare part successfully")
                .result(sparePartService.getSparePartById(id))
                .build();
    }


    @DeleteMapping("/{id}")
    public ApiResponse<SparePartResponse> deleteSparePart(@PathVariable Long id){
        return ApiResponse.<SparePartResponse>builder()
                .message("Delete spare part successfully")
                .result(sparePartService.deleteSparePartById(id))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<SparePartResponse> updateSparePart(@PathVariable Long id, @Valid @RequestBody SparePartUpdateRequest sparePartUpdateRequest){
        return ApiResponse.<SparePartResponse>builder()
                .message("Update spare part successfully")
                .result(sparePartService.updateSparePart(id, sparePartUpdateRequest))
                .build();
    }


    @GetMapping("/model/{modelId}")
    public ApiResponse<List<SparePartResponse>> getSparePartsByModel(@PathVariable Long modelId){
        return ApiResponse.<List<SparePartResponse>>builder()
                .message("Get spare parts by vehicle model successfully")
                .result(sparePartService.getSparePartsByModelId(modelId))
                .build();
    }
}
