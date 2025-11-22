package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.request.VehicleModelRequest;
import com.group02.ev_maintenancesystem.dto.request.VehicleModelUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.VehicleModelResponse;
import com.group02.ev_maintenancesystem.entity.VehicleModel;
import com.group02.ev_maintenancesystem.exception.AppException;
import com.group02.ev_maintenancesystem.exception.ErrorCode;
import com.group02.ev_maintenancesystem.repository.VehicleModelRepository;
import lombok.RequiredArgsConstructor; // Sử dụng Constructor Injection
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
// import org.springframework.beans.factory.annotation.Autowired; // Bỏ Autowired
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Thêm nếu cần

import java.util.List;
import java.util.stream.Collectors; // Thêm import

@Service
@RequiredArgsConstructor // Sử dụng Constructor Injection thay cho @Autowired
@Slf4j
public class VehicleModelServiceImpl implements VehicleModelService {

    // final thay cho @Autowired
    private final VehicleModelRepository vehicleModelRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional // Thêm Transactional cho create/update/delete
    public VehicleModelResponse createVehicleModel(VehicleModelRequest request) {
        String modelName = request.getName().trim();
        // Bỏ kiểm tra bằng findAll, dùng existsByName cho hiệu quả
        if (vehicleModelRepository.existsByName(modelName)) {
            throw new AppException(ErrorCode.VEHICLE_MODEL_NAME_DUPLICATE);
        }

        VehicleModel newVehicleModel = new VehicleModel();
        newVehicleModel.setName(modelName); // Dùng tên đã trim
        newVehicleModel.setModelYear(request.getModelYear());

        // --- LOẠI BỎ SET CÁC TRƯỜNG CHU KỲ ---
        // newVehicleModel.setBasicCycleKm(request.getBasicCycleKm());
        // ... (các trường khác) ...
        // --- KẾT THÚC LOẠI BỎ ---

        VehicleModel savedModel = vehicleModelRepository.save(newVehicleModel);
        return modelMapper.map(savedModel, VehicleModelResponse.class);
    }

    @Override
    public VehicleModelResponse getVehicleModelById(Long vehicleModelId) {
        VehicleModel vehicleModel = vehicleModelRepository.findById(vehicleModelId)
                .orElseThrow(() -> new AppException(ErrorCode.VEHICLE_MODEL_NOT_FOUND)); // Sửa ErrorCode
        return modelMapper.map(vehicleModel, VehicleModelResponse.class);
    }

    @Override
    public List<VehicleModelResponse> getAllVehicleModel() {
        List<VehicleModel> vehicleModelList = vehicleModelRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        // Không cần kiểm tra empty vì findAll trả về list rỗng nếu không có
        // if(vehicleModelList.isEmpty()){
        //     throw new AppException(ErrorCode.VEHICLE_MODEL_NOT_FOUND);
        // }
        return vehicleModelList.stream()
                .map(vehicleModel -> modelMapper.map(vehicleModel, VehicleModelResponse.class))
                .collect(Collectors.toList()); // Sử dụng collect
    }

    @Override
    public Page<VehicleModelResponse> searchVehicleModelByName(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<VehicleModel> vehicleModelPage = vehicleModelRepository.findByNameContainingIgnoreCase(keyword, pageable);
        // Không cần kiểm tra empty vì Page có thông tin đó
        // if(vehicleModelPage.isEmpty()){
        //     throw new AppException(ErrorCode.VEHICLE_MODEL_NOT_FOUND);
        // }
        return vehicleModelPage
                .map(vehicleModel -> modelMapper.map(vehicleModel, VehicleModelResponse.class));
    }

    // Đổi tên hàm cho khớp Controller
    @Override
    @Transactional
    public VehicleModelResponse updateVehicleModel(Long vehicleModelId, VehicleModelUpdateRequest request) {
        VehicleModel vehicleModel = vehicleModelRepository.findById(vehicleModelId)
                .orElseThrow(() -> new AppException(ErrorCode.VEHICLE_MODEL_NOT_FOUND)); // Sửa ErrorCode

        // Kiểm tra trùng tên CHỈ KHI tên được cung cấp và khác tên hiện tại
        if (request.getName() != null && !request.getName().trim().isEmpty() &&
                !request.getName().trim().equalsIgnoreCase(vehicleModel.getName())) {
            String newName = request.getName().trim();
            if (vehicleModelRepository.existsByName(newName)) {
                throw new AppException(ErrorCode.VEHICLE_MODEL_NAME_DUPLICATE);
            }
            vehicleModel.setName(newName);
        }

        // Cập nhật modelYear nếu có
        if (request.getModelYear() != null && !request.getModelYear().trim().isEmpty()) {
            vehicleModel.setModelYear(request.getModelYear().trim());
        }

        // --- LOẠI BỎ CẬP NHẬT CÁC TRƯỜNG CHU KỲ ---
        // if(request.getBasicCycleKm() != null) { ... }
        // ... (các trường khác) ...
        // --- KẾT THÚC LOẠI BỎ ---

        VehicleModel savedVehicleModel = vehicleModelRepository.save(vehicleModel);
        return modelMapper.map(savedVehicleModel, VehicleModelResponse.class);
    }

    @Override
    @Transactional
    public void deleteVehicleModelById(Long vehicleModelId) { // Đổi kiểu trả về thành void
        VehicleModel vehicleModel = vehicleModelRepository.findById(vehicleModelId)
                .orElseThrow(() -> new AppException(ErrorCode.VEHICLE_MODEL_NOT_FOUND)); // Sửa ErrorCode

        // Thêm kiểm tra ràng buộc trước khi xóa nếu cần (ví dụ: còn xe nào thuộc model này không?)
        // if (!vehicleModel.getVehicles().isEmpty()) {
        //     throw new AppException(ErrorCode.CANNOT_DELETE_MODEL_WITH_VEHICLES); // Tạo ErrorCode này
        // }
        // Hoặc xử lý cascade delete (cẩn thận!)

        vehicleModelRepository.delete(vehicleModel);
        // Không cần trả về response từ service
    }

    // --- CẦN CẬP NHẬT INTERFACE VehicleModelService ---
    /* Trong VehicleModelService.java:
    VehicleModelResponse updateVehicleModel(Long vehicleModelId, VehicleModelUpdateRequest request); // Đổi tên hàm
    void deleteVehicleModelById(Long vehicleModelId); // Đổi kiểu trả về
    */
}