package ru.skillbox.diplom.group35.microservice.streaming.rx.impl.kafka;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import ru.skillbox.diplom.group35.library.core.dto.streaming.StreamingMessageDto;

/**
 * ReactiveKafcaConsumerConfig
 *
 * @author Marat Safagareev
 */

@Configuration
@RequiredArgsConstructor
public class KafkaReactiveConsumerConfig {

  private final KafkaConstConfig kafkaConstConfig;
  @Value("${kafka.topic.request-dialog}")
  private String requestDialog;
  @Value("${kafka.topic.request-notification}")
  private String requestNotification;

  @Bean
  public ReceiverOptions<String, StreamingMessageDto<?>> kafkaReceiverOptions() {
    Map<String, Object> properties = new HashMap<>();
    properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConstConfig.getBootstrapAddress());
    properties.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConstConfig.getGroupId());
    properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
    properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
    properties.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG,
        kafkaConstConfig.getMaxPollInterval());
    properties.put(JsonDeserializer.TRUSTED_PACKAGES, kafkaConstConfig.getTrustPackages());
    ReceiverOptions<String, StreamingMessageDto<?>> receiverOptions = ReceiverOptions.create(
        properties);
    return receiverOptions.subscription(Arrays.asList(requestDialog, requestNotification));
  }

  @Bean
  public KafkaReceiver<String, StreamingMessageDto<?>> kafkaReceiver(
      ReceiverOptions<String, StreamingMessageDto<?>> kafkaReceiverOptions) {
    return KafkaReceiver.create(kafkaReceiverOptions);
  }
}
