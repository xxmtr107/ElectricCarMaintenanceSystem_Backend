package com.group02.ev_maintenancesystem.service;


import com.group02.ev_maintenancesystem.dto.request.UserCreationRequest;
import com.group02.ev_maintenancesystem.dto.request.UserUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.UserResponse;
import com.group02.ev_maintenancesystem.entity.User;
import com.group02.ev_maintenancesystem.enums.Role;
import com.group02.ev_maintenancesystem.exception.AppException;
import com.group02.ev_maintenancesystem.exception.ErrorCode;
import com.group02.ev_maintenancesystem.mapper.UserMapper;
import com.group02.ev_maintenancesystem.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.HashSet;
import java.util.List;

/**
 * Service xử lý các operations liên quan đến User
 * Implement CRUD operations với security và validation
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {


    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserCreationRequest request) {


        if(userRepository.existsByUsername(request.getUsername())){
            throw new AppException(ErrorCode.USER_EXISTED);
        }


        User user = userMapper.toUser(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Set role mặc định cho user mới - mọi user đều có role USER
//        HashSet<String> roles = new HashSet<>();
//        roles.add(Role.USER.name()); // Convert enum thành string
//        user.setRoles(roles);

        // Lưu user vào DB và convert Entity thành Response DTO
        return userMapper.toUserResponse(userRepository.save(user));
    }


    public List<UserResponse> getAllUsers(){
        log.info("Get all users - Admin access");

        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserResponse)
                .toList();
    }


    public UserResponse getUserByID(Long id){
        log.info("Get user by id - Admin access");

        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND)));
    }


    public UserResponse updateUser(Long id, UserUpdateRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND));

        userMapper.updateUser(user, request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));
//        user.setRoles(new HashSet<>(roleRepository.findAllById(request.getRoles())));

        return userMapper.toUserResponse(userRepository.save(user));
    }


    public void deleteById(Long id){

        userRepository.deleteById(id);
    }


//    public UserResponse getMyInfo(){
//
//        var context = SecurityContextHolder.getContext();
//
//
//        String name = context.getAuthentication().getName();
//
//
//        User user = userRepository.findByUsername(name).orElseThrow(()->
//                new AppException(ErrorCode.USER_NOT_FOUND));
//
//        return userMapper.toUserResponse(user);
//    }
}