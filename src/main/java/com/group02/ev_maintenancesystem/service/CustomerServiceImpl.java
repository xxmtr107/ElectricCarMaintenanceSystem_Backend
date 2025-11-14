package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.request.PasswordUpdateRequest;
import com.group02.ev_maintenancesystem.dto.request.UserRegistrationRequest;
import com.group02.ev_maintenancesystem.dto.request.UserUpdateRequest;
import com.group02.ev_maintenancesystem.dto.response.UserResponse;
import com.group02.ev_maintenancesystem.entity.User;
import com.group02.ev_maintenancesystem.enums.Role;
import com.group02.ev_maintenancesystem.exception.AppException;
import com.group02.ev_maintenancesystem.exception.ErrorCode;
import com.group02.ev_maintenancesystem.mapper.UserMapper;
import com.group02.ev_maintenancesystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CustomerServiceImpl implements CustomerService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;
    EmailServiceImpl emailService;

    @Override
    public UserResponse registerCustomer(UserRegistrationRequest request) {
        User customer = userMapper.toUser(request);
        customer.setPassword(passwordEncoder.encode(request.getPassword()));

        customer.setRole(Role.CUSTOMER);
        try {
            customer = userRepository.save(customer);
            userRepository.flush();
            log.info("Saving customer success, now sending email...");
            emailService.sendAccountCreationEmail(customer, request.getPassword());
        } catch (DataIntegrityViolationException e) {
            String message = e.getMostSpecificCause().getMessage();
            if (message.contains("UK_username")) {
                throw new AppException(ErrorCode.USERNAME_EXISTED);
            }
            else if(message.contains("UK_email")) {
                throw new AppException(ErrorCode.EMAIL_EXISTED);
            }
        }
        return userMapper.toUserResponse(customer);
    }

    @Override
    public UserResponse updateCustomer(Long customerId, UserUpdateRequest request) {
        User customer = userRepository.findByIdAndRole(customerId, Role.CUSTOMER)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        userMapper.updateUser(request, customer);
        return userMapper.toUserResponse(userRepository.save(customer));
    }
    @Override
    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return userMapper.toUserResponse(user);
    }

    @Override
    public void changeMyPassword(PasswordUpdateRequest request) {
        // 1. Lấy thông tin người dùng đang đăng nhập
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)); // Hoặc UNAUTHENTICATED

        // 2. Xác thực mật khẩu cũ
        if (!passwordEncoder.matches(request.getOldPassword(), currentUser.getPassword())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED); // Hoặc tạo ErrorCode.INVALID_OLD_PASSWORD
        }

        // 3. (Tùy chọn) Kiểm tra xem mật khẩu mới có trùng mật khẩu cũ không
        if (passwordEncoder.matches(request.getNewPassword(), currentUser.getPassword())) {
            throw new AppException(ErrorCode.NEW_PASSWORD_SAME_AS_OLD); // Hoặc tạo ErrorCode.NEW_PASSWORD_SAME_AS_OLD
        }

        // 4. Mã hóa và cập nhật mật khẩu mới
        currentUser.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // 5. Lưu lại thông tin người dùng
        userRepository.save(currentUser);

        // Không cần trả về UserResponse vì API chỉ cần báo thành công
    }

    @Override
    public UserResponse updateMyInfo(UserUpdateRequest request) {
        // Lấy username từ context bảo mật
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)); // Hoặc UNAUTHENTICATED

        // Cập nhật thông tin từ request (không cần kiểm tra role nữa vì đã qua @PreAuthorize)
        userMapper.updateUser(request, currentUser);

        User updatedUser = userRepository.save(currentUser);
        return userMapper.toUserResponse(updatedUser);
    }

    @Override
    public UserResponse getCustomerById(Long customerId) {
        User customer = userRepository.findByIdAndRole(customerId, Role.CUSTOMER)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));


        return userMapper.toUserResponse(customer);

    }

    @Override
    public List<UserResponse> getAllCustomers() {
        return userRepository.findAllByRole(Role.CUSTOMER).stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    @Override
    public void deleteCustomer(Long customerId) {
        User customer = userRepository.findByIdAndRole(customerId,Role.CUSTOMER)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        userRepository.delete(customer);
    }
}