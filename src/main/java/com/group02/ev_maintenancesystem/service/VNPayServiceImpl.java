package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.configuration.VNPayConfig;
import com.group02.ev_maintenancesystem.dto.request.VNPayRequest;
import com.group02.ev_maintenancesystem.dto.response.VNPayResponse;
import com.group02.ev_maintenancesystem.entity.Appointment;
import com.group02.ev_maintenancesystem.entity.Invoice;
import com.group02.ev_maintenancesystem.entity.MaintenanceRecord;
import com.group02.ev_maintenancesystem.entity.Payment;
import com.group02.ev_maintenancesystem.enums.PaymentStatus;
import com.group02.ev_maintenancesystem.exception.AppException;
import com.group02.ev_maintenancesystem.exception.ErrorCode;
import com.group02.ev_maintenancesystem.repository.AppointmentRepository;
import com.group02.ev_maintenancesystem.repository.InvoiceRepository;
import com.group02.ev_maintenancesystem.repository.PaymentRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class VNPayServiceImpl implements  VNPayService {
    @Autowired
    VNPayConfig vnPayConfig;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    InvoiceRepository invoiceRepository;
    @Autowired
    AppointmentRepository appointmentRepository;


    @Override
    public VNPayResponse createPayment(VNPayRequest request, HttpServletRequest httpServletRequest) {
        Invoice invoice = invoiceRepository.findById(request.getInVoiceId())
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_FOUND));
        try {

            // Kiểm tra maintenance record
            MaintenanceRecord maintenanceRecord = invoice.getMaintenanceRecord();

            if(maintenanceRecord == null){
                throw new AppException(ErrorCode.MAINTENANCE_RECORD_NOT_FOUND);
            }

            Appointment appointment = maintenanceRecord.getAppointment();

            if(appointment == null){
                throw new AppException(ErrorCode.APPOINTMENT_NOT_FOUND);
            }

            //Kiểm tra trạng thái của hóa đơn
            if ("PAID".equalsIgnoreCase(invoice.getStatus())) {
                throw new AppException(ErrorCode.INVOICE_ALREADY_PAID);
            }

            if(maintenanceRecord == null){
                throw new AppException(ErrorCode.MAINTENANCE_RECORD_NOT_FOUND);
            }

            if(appointment == null){
                throw new AppException(ErrorCode.APPOINTMENT_NOT_FOUND);
            }

            BigDecimal totalAmount = invoice.getTotalAmount();

            // Tạo transactionCode duy nhất
            String transactionCode = "APP" + request.getInVoiceId() + "_" + System.currentTimeMillis();
            String ipAddress = getIpAddress(httpServletRequest);
            //Lưu vào db trước khi điều hướng qua VNPay
            Payment payment = Payment.builder()
                    .transactionCode(transactionCode)
                    .amount(totalAmount)
                    .status(PaymentStatus.UN_PAID)
                    .invoice(invoice)
                    .method("VNPay")
                    .build();
            paymentRepository.save(payment);
//            payment.setPaymentDate(LocalDateTime.now());
//            payment.setStatus(PaymentStatus.UN_PAID);
//            payment.setAmount(request.getAmount());


            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", "2.1.0");
            vnp_Params.put("vnp_Command", "pay");
            vnp_Params.put("vnp_TmnCode", vnPayConfig.getVnp_TmnCode());

            //Chuyển kiểu dữ liệu từ bigdecimal về long
            long amountVNP = totalAmount.multiply(BigDecimal.valueOf(100)).longValue();
            vnp_Params.put("vnp_Amount", String.valueOf(amountVNP));

            vnp_Params.put("vnp_CurrCode", "VND");

            if (request.getBankCode() == null || request.getBankCode().isEmpty()) {
                throw new AppException(ErrorCode.NOT_BLANK);
            }
            vnp_Params.put("vnp_BankCode", request.getBankCode());
            vnp_Params.put("vnp_TxnRef", transactionCode);
            vnp_Params.put("vnp_OrderType", "other");
            vnp_Params.put("vnp_OrderInfo", "Pay for invoice with id+ " + invoice.getId());
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_IpAddr", ipAddress);
            vnp_Params.put("vnp_ReturnUrl", vnPayConfig.getVnp_ReturnUrl());
            //Tạo thời gian trùng với múi giờ GM+7 của VNPay
            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String createDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_CreateDate", createDate);

            //Sort các field trong để ký
            List<String> fieldName = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldName);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();

            for (int i = 0; i < fieldName.size(); i++) {
                String name = fieldName.get(i);
                String value = vnp_Params.get(name);
                if (value != null && !value.trim().isEmpty()) {
                    hashData.append(name).append("=").append(URLEncoder.encode(value, StandardCharsets.US_ASCII));
                    query.append(URLEncoder.encode(name, StandardCharsets.US_ASCII)).append("=").append(URLEncoder.encode(value, StandardCharsets.US_ASCII));
                    if (i < fieldName.size() - 1) {
                        hashData.append("&");
                        query.append("&");
                    }
                }
            }

            //Tạo chữ ký vnp_SecretKey
            String secretKey = vnPayConfig.getVnp_SecretKey();
            String vnp_SecureHash = hmacSHA512(secretKey, hashData.toString());
            query.append("&vnp_SecureHash=").append(vnp_SecureHash);

            //Tạo URL thanh toán
            String paymentURL = vnPayConfig.getVnp_PayUrl() + "?" + query.toString();
            VNPayResponse response = new VNPayResponse();
            response.setPaymentUrl(paymentURL);
            response.setTransactionCode(transactionCode);
            return response;


        }catch (AppException ae) {
            // Bắt lại AppException của chính bạn
            log.error("Lỗi khi tạo thanh toán: {}", ae.getErrorCode().getMessage());
            throw ae; // Ném lại lỗi
        } catch (Exception e) {
            // Bắt các lỗi chung khác (NullPointerException, etc.)
            log.error("Lỗi không xác định khi tạo thanh toán: {}", e.getMessage(), e); // THÊM LOG NÀY
            throw new AppException(ErrorCode.CREATE_PAYMENT_FAILED);
        }
    }

//    @Override
//    public boolean vnpayCallBack(Map<String, String> param) {
//        // Chỉ validate chữ ký đơn giản, không cập nhật CSDL
//        // Logic thật sự sẽ nằm trong processIpnCallback
//        String receivedHash = param.get("vnp_SecureHash");
//        param.remove("vnp_SecureHash");
//
//        List<String> fieldNames = new ArrayList<>(param.keySet());
//        Collections.sort(fieldNames);
//        StringBuilder hashData = new StringBuilder();
//        for (String name : fieldNames) {
//            String value = param.get(name);
//            if (value != null && !value.isEmpty()) {
//                hashData.append(name).append("=").append(URLEncoder.encode(value, StandardCharsets.US_ASCII));
//                hashData.append("&");
//            }
//        }
//        if (hashData.length() > 0) {
//            hashData.deleteCharAt(hashData.length() - 1); // Xóa dấu & cuối
//        }
//
//        String computedHash = hmacSHA512(vnPayConfig.getVnp_SecretKey(), hashData.toString());
//
//        return computedHash.equals(receivedHash); // Chỉ trả về true/false nếu chữ ký hợp lệ
//    }
//
//    /**
//     * [THÊM MỚI] Xử lý IPN do VNPay Server gọi
//     * @return Chuỗi String (RspCode) để phản hồi cho VNPay
//     */
//    @Transactional
//    public String processIpnCallback(Map<String, String> params) {
//        try {
//            String receivedHash = params.get("vnp_SecureHash");
//            params.remove("vnp_SecureHash");
//
//            List<String> fieldNames = new ArrayList<>(params.keySet());
//            Collections.sort(fieldNames);
//            StringBuilder hashData = new StringBuilder();
//            for (String name : fieldNames) {
//                String value = params.get(name);
//                if (value != null && !value.isEmpty()) {
//                    hashData.append(name).append("=").append(URLEncoder.encode(value, StandardCharsets.US_ASCII));
//                    hashData.append("&");
//                }
//            }
//            if (hashData.length() > 0) {
//                hashData.deleteCharAt(hashData.length() - 1); // Xóa dấu & cuối
//            }
//
//            String computedHash = hmacSHA512(vnPayConfig.getVnp_SecretKey(), hashData.toString());
//
//            // 1. Kiểm tra chữ ký
//            if (!computedHash.equals(receivedHash)) {
//                log.warn("VNPay IPN Signature Invalid. TransactionCode: {}", params.get("vnp_TxnRef"));
//                return "RspCode=97&Message=InvalidSignature"; // Mã 97: Chữ ký không hợp lệ
//            }
//
//            String transactionCode = params.get("vnp_TxnRef");
//            String responseCode = params.get("vnp_ResponseCode");
//
//            // 2. Tìm Payment
//            Payment payment = paymentRepository.findByTransactionCode(transactionCode)
//                    .orElse(null);
//
//            if (payment == null) {
//                log.warn("VNPay IPN Payment not found. TransactionCode: {}", transactionCode);
//                return "RspCode=01&Message=OrderNotFound"; // Mã 01: Đơn hàng không tồn tại
//            }
//
//            // 3. Kiểm tra trạng thái (tránh xử lý lại)
//            if (payment.getStatus() == PaymentStatus.PAID) {
//                log.info("VNPay IPN duplicate callback. TransactionCode: {}", transactionCode);
//                return "RspCode=00&Message=ConfirmSuccess"; // Vẫn trả về thành công
//            }
//
//            // 4. Kiểm tra số tiền (an toàn)
//            long vnpAmount = Long.parseLong(params.get("vnp_Amount"));
//            long dbAmount = payment.getAmount().multiply(BigDecimal.valueOf(100)).longValue();
//            if (vnpAmount != dbAmount) {
//                log.error("VNPay IPN Amount mismatch. TransactionCode: {}. VNP: {}, DB: {}", transactionCode, vnpAmount, dbAmount);
//                return "RspCode=04&Message=InvalidAmount"; // Mã 04: Số tiền không hợp lệ
//            }
//
//            // 5. Cập nhật trạng thái
//            if ("00".equals(responseCode)) {
//                payment.setStatus(PaymentStatus.PAID);
//                payment.setPaymentDate(LocalDateTime.now()); // [THÊM MỚI] Ghi lại ngày thanh toán
//
//                Invoice invoice = payment.getInvoice();
//                invoice.setStatus("PAID");
//                invoiceRepository.save(invoice);
//            } else {
//                payment.setStatus(PaymentStatus.FAILED);
//            }
//            paymentRepository.save(payment);
//
//            log.info("VNPay IPN processed successfully. TransactionCode: {}", transactionCode);
//            return "RspCode=00&Message=ConfirmSuccess"; // Mã 00: Thành công
//
//        } catch (Exception e) {
//            log.error("Error processing VNPay IPN", e);
//            return "RspCode=99&Message=UnknownError"; // Mã 99: Lỗi không xác định
//        }
//    }
//
//    /**
//     * [THÊM MỚI] Lấy trạng thái thanh toán để frontend kiểm tra
//     */
//    public PaymentStatus getPaymentStatusByTransactionCode(String transactionCode) {
//        Payment payment = paymentRepository.findByTransactionCode(transactionCode)
//                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));
//        return payment.getStatus();
//    }


    @Override
    public boolean vnpayCallBack(Map<String, String> param) {
        //Bỏ vnp_SecureHash trước khi hash lại để tránh hash nhầm vnp_secureHash do vnpay trả về
        String receivedHash = param.remove("vnp_SecureHash");

        //Sort lại và tạo chuỗi hashData để so sánh với data do vnpay trả về
        List<String> fieldNames = new ArrayList<>(param.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        for(int i=0; i<fieldNames.size(); i++){
            String name = fieldNames.get(i);
            String value = param.get(name);
            hashData.append(name).append("=");
            hashData.append(URLEncoder.encode(value, StandardCharsets.US_ASCII));
            if(i < fieldNames.size()-1 ){
                hashData.append("&");
            }
        }

        //Hash lại bằng secret key để so sánh với vnp_SecureHash do vnPay trả về
        String computedHash = hmacSHA512(vnPayConfig.getVnp_SecretKey(), hashData.toString());

        //kiểm tra tính hợp lệ của chữ ký
        if(!computedHash.equals(receivedHash)){
            throw new AppException(ErrorCode.SIGNATURE_INVALID);
        }

        //Lấy mã phản hồi từ VNPay
        String responseCode = param.get("vnp_ResponseCode");
        String transactionCode = param.get("vnp_TxnRef");

        //Tìm payment trong DataBase
        Payment payment = paymentRepository.findByTransactionCode(transactionCode)
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));

        //Tránh xử lý lại giao dịch đã thành công
        if(payment.getStatus() == PaymentStatus.PAID){
            return true;
        }

        //Cập nhập trạng thái các mã phản hổi
        if("00".equals(responseCode)){
            //payment set status
            payment.setStatus(PaymentStatus.PAID);
            paymentRepository.save(payment);

            //invoice set Status
            Invoice invoice = payment.getInvoice();
            invoice.setStatus("PAID");
            invoiceRepository.save(invoice);
            return true;
        }
        if("24".equals(responseCode)){
            payment.setStatus(PaymentStatus.CANCELLED);
            paymentRepository.save(payment);

            Invoice invoice = payment.getInvoice();
            invoice.setStatus("CANCELLED");
            invoiceRepository.save(invoice);
            return false;
        }
        else{
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);

            Invoice invoice = payment.getInvoice();
            invoice.setStatus("FAIL");
            invoiceRepository.save(invoice);
            return false;
        }
    }

    //HmacSHA512 để tạo chữ ký khi kết hợp data và key
    private String hmacSHA512(String key, String data){
        try {
            //Tạo máy băm mac sử dụng thuật toán HmacSHA512
            Mac hmac512 = Mac.getInstance("HmacSHA512");

            //Chuyển chuỗi key thành mảng byte bằng UTF-8, rồi tạo một đối tượng khóa bí mật(SecretKeyPec) tương thích với thuật toán HmacSHA512
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");

            //Gắn khóa bí mật secretKey vào máy băm hmac512
            hmac512.init(secretKey);

            //Thực hiện băm dữ liệu data khi đã chuyển đổi sang dạng byte UTF_8
            byte[] bytes = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));

            //chuyển sang chuỗi hex rồi trả về
            StringBuilder hash = new StringBuilder();
            for(byte b : bytes){
                hash.append(String.format("%02x", b));
            }
            return hash.toString();
        }catch (Exception e){
            throw new AppException(ErrorCode.HASH_DATA_FAIL);
        }
    }
    //Lấy địa chỉ IP của khách hàng
    private String getIpAddress(HttpServletRequest request){
        String ipAddress = request.getHeader("X-FORWARDED-FOR");

        if(ipAddress == null || ipAddress.isEmpty() || ipAddress.equalsIgnoreCase("unknown")){
            ipAddress= request.getHeader("Proxy-Client-IP");
        }
        if(ipAddress == null || ipAddress.isEmpty() || ipAddress.equalsIgnoreCase("unknown")){
            ipAddress= request.getHeader("WL-Proxy-Client-IP");
        }
        if(ipAddress == null || ipAddress.isEmpty() || ipAddress.equalsIgnoreCase("unknown")){
            ipAddress= request.getRemoteAddr();
            //Đối với localhost thì trả về IPv6 ::1
            if("127.0.0.1".equals(ipAddress)){
                return "127.0.0.1";
            }
        }
        //Xử lý trường trường hợp có nhiều IP (chọn IP đầu tiên)
        if(ipAddress != null && ipAddress.contains(",")){
            ipAddress = ipAddress.split(",")[0].trim();
        }
        return ipAddress;
    }
}
