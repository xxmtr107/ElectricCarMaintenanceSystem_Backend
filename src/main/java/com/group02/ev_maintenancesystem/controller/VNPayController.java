    package com.group02.ev_maintenancesystem.controller;

    import com.group02.ev_maintenancesystem.configuration.VNPayConfig;
    import com.group02.ev_maintenancesystem.dto.request.VNPayRequest;
    import com.group02.ev_maintenancesystem.dto.response.ApiResponse;
    import com.group02.ev_maintenancesystem.dto.response.VNPayResponse;
    import com.group02.ev_maintenancesystem.enums.PaymentStatus;
    import com.group02.ev_maintenancesystem.service.VNPayServiceImpl;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.io.IOException;
    import java.net.URLEncoder;
    import java.nio.charset.StandardCharsets;
    import java.util.Map;
    import java.util.stream.Collectors;

    @RestController
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
        /**
         * [THAY ĐỔI] Endpoint này VNPay gọi để redirect trình duyệt khách hàng.
         * Nhiệm vụ: Chuyển hướng (redirect) khách hàng về trang frontend.
         */
        @GetMapping("/return")
        public void handleVNPayReturn(@RequestParam Map<String, String> params, HttpServletResponse response) throws IOException {
            // Lấy URL frontend từ config
            String frontendReturnUrl = vnPayConfig.getVnp_ReturnUrl();

            // Tạo chuỗi query string từ các tham số VNPay trả về
            String queryString = params.entrySet().stream()
                    .map(entry -> entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                    .collect(Collectors.joining("&"));

            // Redirect về frontend
            response.sendRedirect(frontendReturnUrl + "?" + queryString);
        }

        /**
         * [THÊM MỚI] Endpoint này VNPay Server tự động gọi (IPN).
         * Nhiệm vụ: Xử lý logic thanh toán, cập nhật CSDL và trả về mã cho VNPay.
         */
        @GetMapping("/ipn")
        public ResponseEntity<String> handleVNPayIpn(@RequestParam Map<String, String> params) {
            String response = service.processIpnCallback(params);
            return ResponseEntity.ok(response);
        }

        /**
         * [THÊM MỚI] Endpoint này để frontend gọi kiểm tra trạng thái thanh toán.
         */
        @GetMapping("/status/{transactionCode}")
        public ApiResponse<PaymentStatus> getPaymentStatus(@PathVariable String transactionCode) {
            return ApiResponse.<PaymentStatus>builder()
                    .message("Payment status fetched")
                    .result(service.getPaymentStatusByTransactionCode(transactionCode))
                    .build();
        }
    }
