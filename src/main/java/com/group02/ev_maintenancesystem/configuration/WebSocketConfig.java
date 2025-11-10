package com.group02.ev_maintenancesystem.configuration;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    JwtAuthChannelInterceptor jwt;

    //Cho client đăng kí để chat 1 1 và nhận tin
    @Override
    public void configureMessageBroker(MessageBrokerRegistry r) {
        r.setApplicationDestinationPrefixes("/app");
        r.enableSimpleBroker("/topic", "/queue");
        r.setUserDestinationPrefix("/user");
    }


    // Endpoint mà React sẽ kết nối ban đầ
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    // Đăng ký jwtChanel để bảo mật kết nối
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration){
        registration.interceptors(jwt);
    }
}
