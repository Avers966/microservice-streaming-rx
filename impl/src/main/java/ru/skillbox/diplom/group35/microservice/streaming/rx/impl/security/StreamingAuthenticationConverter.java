package ru.skillbox.diplom.group35.microservice.streaming.rx.impl.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.BearerTokenError;
import org.springframework.security.oauth2.server.resource.BearerTokenErrors;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * StreamingAuthenticationConverter
 *
 * @author Marat Safagareev
 */
@Component
@RequiredArgsConstructor
public class StreamingAuthenticationConverter implements ServerAuthenticationConverter {

  @Override
  public Mono<Authentication> convert(ServerWebExchange exchange) {
    return Mono.fromCallable(() -> token(exchange.getRequest())).map((token) -> {
      if (token.isEmpty()) {
        BearerTokenError error = BearerTokenErrors.invalidToken("Bearer token is malformed");
        throw new OAuth2AuthenticationException(error);
      }
      return new BearerTokenAuthenticationToken(token);
    });
  }

  private String token(ServerHttpRequest request) {
    HttpCookie cookie = request.getCookies().get("jwt").get(0);
    return cookie.getValue();
  }
}
