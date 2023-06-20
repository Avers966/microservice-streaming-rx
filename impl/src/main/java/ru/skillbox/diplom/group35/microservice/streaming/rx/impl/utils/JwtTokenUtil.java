package ru.skillbox.diplom.group35.microservice.streaming.rx.impl.utils;

import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

/**
 * JwtTokenUtils
 *
 * @author Marat Safagareev
 */
@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

  private final JwtDecoder decoder;

  public UUID getAccountIdFromToken(String token) {
    return UUID.fromString(getAllClaimsFromToken(token).get("id").toString());
  }

  private Map<String, Object> getAllClaimsFromToken(String token) {
    Jwt jwt = decoder.decode(token);
    return jwt.getClaims();
  }

}
