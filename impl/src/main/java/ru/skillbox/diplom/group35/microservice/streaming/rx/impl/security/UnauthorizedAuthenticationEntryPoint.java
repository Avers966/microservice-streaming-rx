package ru.skillbox.diplom.group35.microservice.streaming.rx.impl.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * UnauthorizedAuthenticationEntryPoint
 *
 * @author Marat Safagareev
 */
@Component
public class UnauthorizedAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

  @Override
  public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
    return Mono.fromRunnable(()-> exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED));
  }
}
