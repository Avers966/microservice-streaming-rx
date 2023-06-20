package ru.skillbox.diplom.group35.microservice.streaming.rx.impl.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;
import ru.skillbox.diplom.group35.microservice.streaming.rx.impl.utils.JwtTokenUtil;
import ru.skillbox.diplom.group35.microservice.streaming.rx.impl.service.StreamingService;

/**
 * StreamingHandler
 *
 * @author Marat Safagareev
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StreamingHandler implements WebSocketHandler {

  private final JwtTokenUtil tokenUtil;
  private final StreamingService streamingService;

  @NotNull
  @Override
  public Mono<Void> handle(@NotNull WebSocketSession session) {
    defineUser(session);
    Mono<Void> input = streamingService.getInputMessageMono(session);
    Mono<Void> output = streamingService.getOutputMessageMono(session);
    return Mono.zip(input, output).then().log();
  }

  private void defineUser(WebSocketSession session) {
    String token = session.getHandshakeInfo().getCookies().get("jwt").get(0).getValue();
    session.getAttributes().put(StreamingService.ACCOUNT_ID_FIELD, tokenUtil.getAccountIdFromToken(token));
  }
}
