package ru.skillbox.diplom.group35.microservice.streaming.rx.impl.config;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.WebSocketService;
import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import org.springframework.web.reactive.socket.server.upgrade.ReactorNettyRequestUpgradeStrategy;
import ru.skillbox.diplom.group35.microservice.streaming.rx.impl.websocket.StreamingHandler;

/**
 * WebSocketConfig
 *
 * @author Marat Safagareev
 */
@Configuration
@RequiredArgsConstructor
public class WebSocketConfig {

  private final StreamingHandler streamingHandler;

  @Bean
  public HandlerMapping handlerMapping() {
    Map<String, WebSocketHandler> map = new HashMap<>();
    map.put("/api/v1/streaming/ws", streamingHandler);
    SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
    mapping.initApplicationContext();
    mapping.setUrlMap(map);
    mapping.setOrder(Ordered.HIGHEST_PRECEDENCE);
    return mapping;
  }

  @Bean
  public WebSocketService webSocketService(){
    return new HandshakeWebSocketService(new ReactorNettyRequestUpgradeStrategy());
  }

  @Bean
  public WebSocketHandlerAdapter handlerAdapter() {
    return new WebSocketHandlerAdapter(webSocketService());
  }
}
