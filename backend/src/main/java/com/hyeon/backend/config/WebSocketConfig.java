package com.hyeon.backend.config;

import com.hyeon.backend.controller.ws.WebsocketController;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@RequiredArgsConstructor
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

  private final WebsocketController wsController;

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    // TODO: 필요에 따라 허용할 origin을 지정
    registry.addHandler(wsController, "/ws/container").setAllowedOrigins("*");
  }
}
