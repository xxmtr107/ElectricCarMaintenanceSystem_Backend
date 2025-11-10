package com.group02.ev_maintenancesystem.configuration;
import com.group02.ev_maintenancesystem.exception.AppException;
import com.group02.ev_maintenancesystem.service.CustomUserDetailService;
import com.group02.ev_maintenancesystem.service.JwtService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtAuthChannelInterceptor implements ChannelInterceptor {

    JwtService jwtService;
    CustomUserDetailService userDetailsService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        // Chỉ kiểm tra khi client gửi lệnh CONNECT
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String jwt = authHeader.substring(7);

                try {
                    // Dùng hàm verifyToken(ném lỗi nếu sai)
                    SignedJWT signedJWT = jwtService.verifyToken(jwt);
                    String username = signedJWT.getJWTClaimsSet().getSubject();

                    //Load user lên
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    //Tạo principal
                    UsernamePasswordAuthenticationToken authenToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    //Gắn principal vào session
                    accessor.setUser(authenToken);
                    log.info("Authenticated WebSocket user: {}", username);

                } catch (AppException ae) {
                    log.warn("WebSocket Auth Failed (AppException): Code={}, Message={}",
                            ae.getErrorCode().getCode(), ae.getErrorCode().getMessage());
                } catch (ParseException | JOSEException e) {
                    // Bắt lỗi từ thư viện Nimbus (Token sai định dạng, sai chữ ký)
                    log.warn("WebSocket Auth Failed (Token Parse/Verify Error): {}", e.getMessage());

                } catch (Exception e) {
                    // Bắt các lỗi không lường trước khác
                    log.error("Unexpected WebSocket Auth Error: {}", e.getMessage());
                }
            } else {
                log.warn("Missing or invalid Authorization header in STOMP CONNECT");
            }
        }

        // Log này rất hữu ích để debug
        if (accessor.getCommand() != null && accessor.getCommand() != StompCommand.CONNECT) {
            log.info("Message type: {}, Principal: {}",
                    accessor.getCommand(),
                    accessor.getUser() != null ? accessor.getUser().getName() : "NULL (Check Interceptor!)"
            );
        }
        return message;
    }
}