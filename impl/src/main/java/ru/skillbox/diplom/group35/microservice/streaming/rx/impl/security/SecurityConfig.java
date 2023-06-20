package ru.skillbox.diplom.group35.microservice.streaming.rx.impl.security;

import com.nimbusds.jose.JWSAlgorithm;
import java.nio.charset.StandardCharsets;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;

/**
 * SecurityConfig
 *
 * @author Marat Safagareev
 */
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

  @Value("${app.public-routes}")
  private String[] publicRoutes;


  @Bean
  public SecurityWebFilterChain filterChain(
      ServerHttpSecurity http,
      UnauthorizedAuthenticationEntryPoint entryPoint,
      StreamingAuthenticationConverter converter) {
    http
        .securityContextRepository(securityContextRepository())
        .exceptionHandling()
        .authenticationEntryPoint(entryPoint)
        .and()
        .authorizeExchange()
        .pathMatchers(publicRoutes).permitAll()
        .anyExchange().authenticated()
        .and()
        .httpBasic().disable()
        .formLogin().disable()
        .csrf().disable()
        .logout().disable()
        .oauth2ResourceServer().bearerTokenConverter(converter).jwt();
    return http.build();
  }

  @Bean
  public WebSessionServerSecurityContextRepository securityContextRepository() {
    return new WebSessionServerSecurityContextRepository();
  }

  @Bean
  public ReactiveJwtDecoder reactiveJwtDecoder() {
    return NimbusReactiveJwtDecoder.withSecretKey(
        new SecretKeySpec("SecretSpecialKeyOauth2.0Jwt256Bites".getBytes(StandardCharsets.UTF_8),
            JWSAlgorithm.RS512.getName())).build();
  }

  @Bean
  public JwtDecoder jwtDecoder() {
    return NimbusJwtDecoder.withSecretKey(
        new SecretKeySpec("SecretSpecialKeyOauth2.0Jwt256Bites".getBytes(StandardCharsets.UTF_8),
            JWSAlgorithm.RS512.getName())).build();
  }
}
