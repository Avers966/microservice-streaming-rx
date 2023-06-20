package ru.skillbox.diplom.group35.microservice.streaming.rx.impl.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.ReceiverRecord;
import ru.skillbox.diplom.group35.library.core.dto.streaming.MessageDto;
import ru.skillbox.diplom.group35.library.core.dto.streaming.StreamingMessageDto;
import ru.skillbox.diplom.group35.microservice.streaming.rx.api.dto.AccountOnlineDto;

/**
 * StreamingService
 *
 * @author Marat Safagareev
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StreamingService {

  public static final String ACCOUNT_ID_FIELD = "accountId";
  private static final String TYPE_FIELD = "type";
  private static final String MESSAGE_TYPE = "MESSAGE";
  private static final String MESSAGE_STATUS_SENT = "SENT";
  private final KafkaProducerService kafkaProducerService;
  private final KafkaReactiveConsumerService kafkaConsumerService;
  private final ObjectMapper objectMapper;
  private final JavaType type;

  public Mono<Void> getInputMessageMono(WebSocketSession session) {
    UUID accountId = getAccountId(session);

    return session.receive()
        .doOnSubscribe(subscription -> {
          log.info("Connection established with accountId: {}", accountId);
          changeAccountOnline(new AccountOnlineDto(accountId, null, true));
        })
        .doOnNext(message -> handleMessage(accountId, message))
        .doOnComplete(() -> {
          log.info("Connection closed with accountId: {}", accountId);
          changeAccountOnline(new AccountOnlineDto(accountId, ZonedDateTime.now(), false));
        })
        .then();
  }

  public Mono<Void> getOutputMessageMono(WebSocketSession session) {
    UUID accountId = getAccountId(session);
    return session.send(createMessage(accountId).map(s -> {
          log.info("Sending message: {} to user {}", s, accountId);
          return session.textMessage(s);
        }))

        .doOnError(err -> log.error(err.getMessage()))
        .doOnTerminate(() -> log.info("Session terminated with user: {}", getAccountId(session)))
        .log();
  }

  private void handleMessage(UUID authorId, WebSocketMessage message) {
    String payload = message.getPayloadAsText();
    log.info("Received message from user: {}", payload);
    try {
      JsonNode jsonNode = objectMapper.readTree(payload);
      if (jsonNode.get(TYPE_FIELD).textValue().equals(MESSAGE_TYPE)) {
        StreamingMessageDto<MessageDto> streamingMessageDto = objectMapper.readValue(payload, type);
        streamingMessageDto.getData().setReadStatus(MESSAGE_STATUS_SENT);
        streamingMessageDto.getData().setTime(ZonedDateTime.now());
        transmitMessage(reconstructStreamingMessage(authorId, streamingMessageDto));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private StreamingMessageDto<MessageDto> reconstructStreamingMessage(UUID authorId,
      StreamingMessageDto<MessageDto> dto) {
    if (dto.getData().getConversationPartner2().equals(authorId)) {
      dto.getData().setConversationPartner2(dto.getData().getConversationPartner1());
    }
    dto.getData().setConversationPartner1(authorId);
    if (dto.getRecipientId().equals(authorId)) {
      dto.setRecipientId(dto.getData().getConversationPartner2());
    }
    return dto;
  }

  private void transmitMessage(StreamingMessageDto<MessageDto> streamingMessageDto) {
    log.info("Transmitting message: {}", streamingMessageDto);
    kafkaProducerService.send(streamingMessageDto);
  }

  private Flux<String> createMessage(UUID accountId) {
    return kafkaConsumerService.consumeMessage()
        .filter(r -> r.value().getRecipientId().equals(accountId))
        .mapNotNull(this::getTopicRecord);
  }

  private String getTopicRecord(ReceiverRecord<String, StreamingMessageDto<?>> record) {
    StreamingMessageDto<?> streamingMessageDto = record.value();
    try {
      String lastMessage = objectMapper.writeValueAsString(streamingMessageDto);
      record.receiverOffset().acknowledge();
      return lastMessage;
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }

  private void changeAccountOnline(AccountOnlineDto accountOnlineDto) {
    log.info("Change user with id: {} status to: {}", accountOnlineDto.getId(),
        accountOnlineDto.getIsOnline());
    kafkaProducerService.send(accountOnlineDto);
  }

  private UUID getAccountId(WebSocketSession session) {
    return UUID.fromString(session.getAttributes().get(ACCOUNT_ID_FIELD).toString());
  }
}
