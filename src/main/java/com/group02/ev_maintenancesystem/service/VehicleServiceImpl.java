package com.group02.ev_maintenancesystem.service;
import com.group02.ev_maintenancesystem.dto.request.VehicleCreationRequest;
import com.group02.ev_maintenancesystem.dto.request.VehicleUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.VehicleResponse;
import com.group02.ev_maintenancesystem.entity.Appointment;
import com.group02.ev_maintenancesystem.entity.User;
import com.group02.ev_maintenancesystem.entity.Vehicle;
import com.group02.ev_maintenancesystem.entity.VehicleModel;
import com.group02.ev_maintenancesystem.enums.AppointmentStatus;
import com.group02.ev_maintenancesystem.exception.AppException;
import com.group02.ev_maintenancesystem.exception.ErrorCode;
import com.group02.ev_maintenancesystem.repository.AppointmentRepository;
import com.group02.ev_maintenancesystem.repository.UserRepository;
import com.group02.ev_maintenancesystem.repository.VehicleModelRepository;
import com.group02.ev_maintenancesystem.repository.VehicleRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class VehicleServiceImpl implements VehicleService{

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    VehicleModelRepository vehicleModelRepository;

    @Autowired
     UserRepository userRepository;

    @Autowired
    AppointmentRepository appointmentRepository;


    @Override
    public VehicleResponse createVehicle(VehicleCreationRequest vehicleCreationRequest) {
        //check Vin đã tồn tại chưa

        if (vehicleRepository.existsByVin(vehicleCreationRequest.getVin())) {
            throw new AppException(ErrorCode.VIN_ALREADY_EXISTS);
        }

        //check biển số xe đã tồn tại chưa
        if (vehicleRepository.existsByLicensePlate(vehicleCreationRequest.getLicensePlate())) {
            throw new AppException(ErrorCode.LICENSE_PLATE_ALREADY_EXISTS);
        }

        //mapping
        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate(vehicleCreationRequest.getLicensePlate());
        vehicle.setVin(vehicleCreationRequest.getVin());
        vehicle.setCurrentKm(vehicleCreationRequest.getCurrentKm());
        vehicle.setPurchaseYear(vehicleCreationRequest.getPurchaseYear().atDay(1));


        //check xem modelId có tồn tại chưa
        //có mới gán vào
        VehicleModel model = vehicleModelRepository.findById(vehicleCreationRequest.getModelId()).
                orElseThrow(() -> new AppException(ErrorCode.MODEL_NOT_FOUND));
        vehicle.setModel(model);

        //check xem ModelId có tồn tại hay không và modelId có phải là customer không
        // nếu có gán vào
        User user = userRepository.findById(vehicleCreationRequest.getCustomerId())
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND));
        if(!user.isCustomer()){
            throw new AppException(ErrorCode.USER_NOT_CUSTOMER);
        }
        vehicle.setCustomerUser(user);
        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        VehicleResponse response = new VehicleResponse();
        response.setId(savedVehicle.getId());
        response.setLicensePlate(savedVehicle.getLicensePlate());
        response.setVin(savedVehicle.getVin());
        response.setCurrentKm(savedVehicle.getCurrentKm());
        response.setModelId(savedVehicle.getModel().getId());
        response.setCustomerId(savedVehicle.getCustomerUser().getId());
        response.setPurchaseYear(savedVehicle.getPurchaseYear());
        return response;
    }

    @Override
    public List<VehicleResponse> getVehiclesByUserId(Long userId) {
        List<Vehicle> vehicle ;
        try {
            vehicle = vehicleRepository.findByCustomerUserId(userId);
        }catch (DataAccessException e){
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        if(vehicle.isEmpty()){
            throw new AppException(ErrorCode.VEHICLE_NOT_FOUND);
        }
        return vehicle.stream()
                .map(vehicle1 -> modelMapper.map(vehicle1, VehicleResponse.class))
                .toList();
    }

    @Override
    public VehicleResponse getVehiclesByVehicleId(Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId).
                orElseThrow(() -> new AppException(ErrorCode.VEHICLE_NOT_FOUND));
        return modelMapper.map(vehicle, VehicleResponse.class);
    }

    @Override
    public Page<VehicleResponse> getAllVehicle(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Vehicle> vehicleList = vehicleRepository.findAll(pageable);
        if(vehicleList.isEmpty()){
            throw new AppException(ErrorCode.VEHICLE_NOT_FOUND);
        }
        return vehicleList
                .map(vehicleList1 -> modelMapper.map(vehicleList1, VehicleResponse.class));
    }

    @Override
    public VehicleResponse updateVehicle(Long vehicleId, VehicleUpdateRequest request) {
        //Tìm xem id của xe có tồn tại hay không
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new AppException(ErrorCode.VEHICLE_NOT_FOUND));
        if(request.getCurrentKm() != null) {
            vehicle.setCurrentKm(request.getCurrentKm());
        }

        vehicleRepository.save(vehicle);
        return modelMapper.map(vehicle, VehicleResponse.class);
    }

    @Override
    @Transactional
    public VehicleResponse deleteVehicle(Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId).
                orElseThrow(() -> new AppException(ErrorCode.VEHICLE_NOT_FOUND));
        //appointmentRepository.deleteByVehicleId(vehicleId);
        List<Appointment> appointment = appointmentRepository.findByVehicleId(vehicleId);
        //Nếu empty thì xóa
        if(appointment.isEmpty()){
            vehicleRepository.deleteById(vehicleId);
        }

        //CHỈ XÓA NHỮNG XE CÓ TRẠNG THÁI COMPLETED VÀ CANCELLED
        boolean hasActiveAppointment = appointment.stream()
                .anyMatch(app -> app.getStatus() != AppointmentStatus.COMPLETED
                        && app.getStatus() != AppointmentStatus.CANCELLED);
        if(hasActiveAppointment){
            throw new AppException(ErrorCode.CANNOT_DELETE_VEHICLE_WITH_ACTIVE_APPOINTMENT);
        }
        vehicleRepository.deleteById(vehicleId);
        return modelMapper.map(vehicle, VehicleResponse.class);
    }

    public boolean isVehicleOwner(Authentication authentication, Long vehicleId) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return false;
        }

        Jwt jwt = (Jwt) authentication.getPrincipal();
        Long customerId = jwt.getClaim("userId");
        String rolesClaim = jwt.getClaim("scope");

        // Chỉ kiểm tra nếu người dùng là CUSTOMER
        if (customerId == null || rolesClaim == null || !rolesClaim.contains("ROLE_CUSTOMER")) {
            return false;
        }

        // Kiểm tra xem có tồn tại xe với vehicleId và customerId này không
        // (Sử dụng phương thức đã có sẵn trong VehicleRepository)
        Optional<Vehicle> vehicle = vehicleRepository.findByIdAndCustomerUserId(vehicleId, customerId);

        // Nếu vehicle.isPresent() == true, nghĩa là tìm thấy xe -> là chủ sở hữu
        return vehicle.isPresent();
    }
}
