    package com.group02.ev_maintenancesystem.controller;

    import com.group02.ev_maintenancesystem.dto.request.VNPayRequest;
    import com.group02.ev_maintenancesystem.dto.response.ApiResponse;
    import com.group02.ev_maintenancesystem.dto.response.VNPayResponse;
    import com.group02.ev_maintenancesystem.service.VNPayServiceImpl;
    import jakarta.servlet.http.HttpServletRequest;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.web.bind.annotation.*;

    import java.util.Map;

    @RestController
    @RequestMapping("/vnPay")
    public class VNPayController {

        @Autowired
        VNPayServiceImpl service;


        @PostMapping("/create")
        public ApiResponse<VNPayResponse> createPayment(@RequestBody VNPayRequest vnPayRequest
        , HttpServletRequest httpServletRequest){
            return ApiResponse.<VNPayResponse>builder()
                    .message("Create payment successfully")
                    .result(service.createPayment(vnPayRequest, httpServletRequest))
                    .build();
        }

        @GetMapping("/return")
        public ApiResponse<Boolean> handleVNPayCallBack(@RequestParam Map<String, String> params){
            return ApiResponse.<Boolean>builder()
                    .message("VNPay callback handled")
                    .result(service.vnpayCallBack(params))
                    .build();
        }
    }
