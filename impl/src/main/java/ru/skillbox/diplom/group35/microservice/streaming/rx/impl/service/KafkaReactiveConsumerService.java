package ru.skillbox.diplom.group35.microservice.streaming.rx.impl.service;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverRecord;
import ru.skillbox.diplom.group35.library.core.dto.streaming.StreamingMessageDto;

/**
 * KafkaReactiveConsumerService
 *
 * @author Marat Safagareev
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaReactiveConsumerService {

  private final KafkaReceiver<String, StreamingMessageDto<?>> receiver;

  public Flux<ReceiverRecord<String, StreamingMessageDto<?>>> consumeMessage() {
    return receiver.receive()
        .delayElements(Duration.ofMillis(1500))
        .doOnNext(consumerRecord -> log.info("Received {}", consumerRecord))
        .doOnError(err -> log.error(err.getMessage()));
  }
}
