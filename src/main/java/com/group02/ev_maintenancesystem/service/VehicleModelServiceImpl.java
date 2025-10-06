package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.request.VehicleModelRequest;
import com.group02.ev_maintenancesystem.dto.request.VehicleModelUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.VehicleModelCreationResponse;
import com.group02.ev_maintenancesystem.dto.response.VehicleModelGetResponse;
import com.group02.ev_maintenancesystem.entity.VehicleModel;
import com.group02.ev_maintenancesystem.exception.AppException;
import com.group02.ev_maintenancesystem.exception.ErrorCode;
import com.group02.ev_maintenancesystem.repository.VehicleModelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehicleModelServiceImpl implements  VehicleModelService {

    @Autowired
    VehicleModelRepository vehicleModelRepository;

    @Autowired
    ModelMapper modelMapper;


    @Override
    public VehicleModelGetResponse createVehicleModel(VehicleModelRequest request) {
        //check xem có bị trùng tên hay không
        String modelName = request.getName().trim();
        List<VehicleModel> existModelName = vehicleModelRepository.findAll();

        //So sánh model name
        boolean exists = existModelName.stream()
                .anyMatch(m -> m.getName().trim().equalsIgnoreCase(modelName));

        if(exists){
            throw new AppException(ErrorCode.VEHICLE_MODEL_NAME_DUPLICATE);
        }
        VehicleModel newVehicleModel = new VehicleModel();
        newVehicleModel.setName(request.getName());
        newVehicleModel.setModelYear(request.getModelYear());
        newVehicleModel.setBasicMaintenance(request.getBasicMaintenance());
        newVehicleModel.setComprehensiveMaintenance(request.getComprehensiveMaintenance());
        newVehicleModel.setBasicMaintenanceTime(request.getBasicMaintenanceTime());
        newVehicleModel.setComprehensiveMaintenanceTime(request.getComprehensiveMaintenanceTime());


        VehicleModel savedModel = vehicleModelRepository.save(newVehicleModel);


//        VehicleModelCreationResponse vehicleModelResponse = VehicleModelCreationResponse.builder()
//                .id(savedModel.getId())
//                .name(savedModel.getName())
//                .modelYear(savedModel.getModelYear())
//                .basicMaintenance(savedModel.getBasicMaintenance())
//                .comprehensiveMaintenance(savedModel.getComprehensiveMaintenance())
//                .basicMaintenanceTime(savedModel.getBasicMaintenanceTime())
//                .comprehensiveMaintenanceTime(savedModel.getComprehensiveMaintenanceTime())
//                .build();


        return modelMapper.map(savedModel, VehicleModelGetResponse.class);
    }

    @Override
    public VehicleModelGetResponse getVehicleModelById(Long vehicleModelId) {
        VehicleModel vehicleModel = vehicleModelRepository.findById(vehicleModelId)
                .orElseThrow(()->new AppException(ErrorCode.VEHICLE_NOT_FOUND));
        return modelMapper.map(vehicleModel, VehicleModelGetResponse.class);
    }

    @Override
    public List<VehicleModelGetResponse> getAllVehicleModel() {
        List<VehicleModel> vehicleModelList = vehicleModelRepository.findAll();
        if(vehicleModelList.isEmpty()){
            throw new AppException(ErrorCode.VEHICLE_MODEL_NOT_FOUND);
        }
        return vehicleModelList.stream()
                .map(vehicleModelList1-> modelMapper.map(vehicleModelList1, VehicleModelGetResponse.class)).toList();
    }

    @Override
    public Page<VehicleModelGetResponse> searchVehicleModelByName(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<VehicleModel> vehicleModelList = vehicleModelRepository.findByNameContainingIgnoreCase(keyword,pageable);
        if(vehicleModelList.isEmpty()){
            throw new AppException(ErrorCode.VEHICLE_MODEL_NOT_FOUND);
        }
        return vehicleModelList
                .map(vehicleModelList1 -> modelMapper.map(vehicleModelList1, VehicleModelGetResponse.class));
    }

    @Override
    public VehicleModelGetResponse updateVehicleMode(Long vehicleModelId, VehicleModelUpdateRequest request) {
        VehicleModel vehicleModel = vehicleModelRepository.findById(vehicleModelId)
                .orElseThrow(()->new AppException(ErrorCode.VEHICLE_NOT_FOUND));

        //check name có duplicate ko
        boolean exist = vehicleModelRepository.existsByName(request.getName());
        if(exist){
            throw new AppException(ErrorCode.VEHICLE_MODEL_NAME_DUPLICATE);
        }

        //không duplicate thì set
        if(request.getName() != null && !request.getName().trim().isEmpty()){
            vehicleModel.setName(request.getName());
        }
        if(!request.getModelYear().trim().isEmpty()) {
            vehicleModel.setModelYear(request.getModelYear());
        }
        if(request.getBasicMaintenance() != null) {
            vehicleModel.setBasicMaintenance(request.getBasicMaintenance());
        }
        if(request.getComprehensiveMaintenance() != null) {
            vehicleModel.setComprehensiveMaintenance(request.getComprehensiveMaintenance());
        }
        if(request.getBasicMaintenanceTime() != null) {
            vehicleModel.setBasicMaintenanceTime(request.getBasicMaintenanceTime());
        }
        if(request.getComprehensiveMaintenanceTime() != null) {
            vehicleModel.setComprehensiveMaintenanceTime(request.getComprehensiveMaintenanceTime());
        }

        VehicleModel savedVehicleModel = vehicleModelRepository.save(vehicleModel);
        return modelMapper.map(savedVehicleModel, VehicleModelGetResponse.class);
    }

    @Override
    public VehicleModelGetResponse deleteVehicleModelById(Long vehicleId) {
        VehicleModel vehicleModel = vehicleModelRepository.findById(vehicleId)
                .orElseThrow(()->new AppException(ErrorCode.VEHICLE_NOT_FOUND));
        vehicleModelRepository.delete(vehicleModel);
        return modelMapper.map(vehicleModel, VehicleModelGetResponse.class);
    }

}
