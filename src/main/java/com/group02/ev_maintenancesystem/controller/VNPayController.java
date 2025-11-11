    package com.group02.ev_maintenancesystem.controller;

    import com.group02.ev_maintenancesystem.configuration.VNPayConfig;
    import com.group02.ev_maintenancesystem.dto.request.VNPayRequest;
    import com.group02.ev_maintenancesystem.dto.response.ApiResponse;
    import com.group02.ev_maintenancesystem.dto.response.VNPayResponse;
    import com.group02.ev_maintenancesystem.enums.PaymentStatus;
    import com.group02.ev_maintenancesystem.exception.AppException;
    import com.group02.ev_maintenancesystem.exception.ErrorCode;
    import com.group02.ev_maintenancesystem.service.VNPayServiceImpl;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.io.IOException;
    import java.net.URLEncoder;
    import java.nio.charset.StandardCharsets;
    import java.util.HashMap;
    import java.util.Map;
    import java.util.stream.Collectors;

    @RestController
    @Slf4j
    @RequestMapping("/vnpay")
    public class VNPayController {

        @Autowired
        VNPayServiceImpl service;

        @Autowired
        VNPayConfig vnPayConfig;


        @PostMapping("/create")
        public ApiResponse<VNPayResponse> createPayment(@RequestBody VNPayRequest vnPayRequest
        , HttpServletRequest httpServletRequest){
            return ApiResponse.<VNPayResponse>builder()
                    .message("Create payment successfully")
                    .result(service.createPayment(vnPayRequest, httpServletRequest))
                    .build();
        }

//        @GetMapping("/return")
//        public ApiResponse<Boolean> handleVNPayCallBack(@RequestParam Map<String, String> params){
//            return ApiResponse.<Boolean>builder()
//                    .message("VNPay callback handled")
//                    .result(service.vnpayCallBack(params))
//                    .build();
//        }

//        @GetMapping("/return")
//        public void handleVNPayReturn(@RequestParam Map<String, String> params, HttpServletResponse response) throws IOException {
//            // Lấy URL frontend từ config
//            String frontendReturnUrl = vnPayConfig.getVnp_ReturnUrl();
//
//            // Tạo chuỗi query string từ các tham số VNPay trả về
//            String queryString = params.entrySet().stream()
//                    .map(entry -> entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
//                    .collect(Collectors.joining("&"));
//
//            // Redirect về frontend
//            response.sendRedirect(frontendReturnUrl + "?" + queryString);
//        }
//        @GetMapping("/ipn")
//        public ResponseEntity<Map<String, String>> handleVNPayIpn(@RequestParam Map<String, String> params) {
//            log.info("Received IPN call from VNPay Server: {}", params);
//            Map<String, String> response = new HashMap<>();
//
//            try {
//                // TÁI SỬ DỤNG logic: xác thực, cập nhật DB
//                boolean paymentSuccess = service.vnpayCallBack(params);
//
//                if (paymentSuccess) {
//                    response.put("RspCode", "00");
//                    response.put("Message", "Confirm Success");
//                } else {
//                    response.put("RspCode", "99"); // Lỗi chung
//                    response.put("Message", "Transaction failed or cancelled");
//                }
//            } catch (AppException e) {
//                log.error("IPN Error (AppException): {}", e.getMessage());
//                if (e.getErrorCode() == ErrorCode.SIGNATURE_INVALID) {
//                    response.put("RspCode", "97");
//                    response.put("Message", "Invalid Checksum");
//                } else if (e.getErrorCode() == ErrorCode.PAYMENT_NOT_FOUND) {
//                    response.put("RspCode", "01");
//                    response.put("Message", "Order not Found");
//                } else {
//                    response.put("RspCode", "99");
//                    response.put("Message", "Unknown error");
//                }
//            } catch (Exception e) {
//                log.error("IPN Error (General Exception): {}", e.getMessage(), e);
//                response.put("RspCode", "99");
//                response.put("Message", "Unknown error");
//            }
//
//            log.info("IPN Response: {}", response);
//            return response.; // Trả về JSON cho server VNPay
//        }


//        @GetMapping("/status/{transactionCode}")
//        public ApiResponse<PaymentStatus> getPaymentStatus(@PathVariable String transactionCode) {
//            return ApiResponse.<PaymentStatus>builder()
//                    .message("Payment status fetched")
//                    .result(service.getPaymentStatusByTransactionCode(transactionCode))
//                    .build();
//        }


        @GetMapping("/return")
        public void handleVNPayReturn(
                @RequestParam Map<String, String> params,
                HttpServletResponse response) throws IOException {
            log.info("Received /return call from User Browser: {}", params);

            // 1. Thực thi logic
            boolean paymentSuccess = service.vnpayCallBack(params);

            // 2. Chuyển hướng về frontend
            // (Đây là ví dụ, bạn nên lấy URL frontend từ config)
            String frontendUrl = vnPayConfig.getVnp_PayUrl();

            String redirectUrl;
            if (paymentSuccess){
                redirectUrl = frontendUrl + "?success=true&code=" + params.get("vnp_TxnRef");
            } else {
                redirectUrl = frontendUrl + "?success=false&code=" + params.get("vnp_ResponseCode");
            }

            response.sendRedirect(redirectUrl);
        }

        @GetMapping("/ipn")
        public Map<String, String> handleIPN(@RequestParam Map<String, String> params) {
            log.info("Received IPN call from VNPay Server: {}", params);
            Map<String, String> response = new HashMap<>();

            try {
                boolean paymentSuccess = service.vnpayCallBack(params);

                if (paymentSuccess) {
                    response.put("RspCode", "00");
                    response.put("Message", "Confirm Success");
                } else {
                    response.put("RspCode", "99"); // Lỗi chung
                    response.put("Message", "Transaction failed or cancelled");
                }
            } catch (AppException e) {
                log.error("IPN Error (AppException): {}", e.getMessage());
                if (e.getErrorCode() == ErrorCode.SIGNATURE_INVALID) {
                    response.put("RspCode", "97");
                    response.put("Message", "Invalid Checksum");
                } else if (e.getErrorCode() == ErrorCode.PAYMENT_NOT_FOUND) {
                    response.put("RspCode", "01");
                    response.put("Message", "Order not Found");
                } else {
                    response.put("RspCode", "99");
                    response.put("Message", "Unknown error");
                }
            } catch (Exception e) {
                log.error("IPN Error (General Exception): {}", e.getMessage(), e);
                response.put("RspCode", "99");
                response.put("Message", "Unknown error");
            }

            log.info("IPN Response: {}", response);
            return response; // Trả về JSON cho server VNPay
        }
    }
