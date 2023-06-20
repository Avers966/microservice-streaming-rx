package ru.skillbox.diplom.group35.microservice.streaming.rx.impl.kafka;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

/**
 * KafkaTopicConfig
 *
 * @author Marat Safagareev
 */
@Configuration
@RequiredArgsConstructor
public class KafkaTopicConfig {

  private final KafkaConstConfig kafkaConstConfig;

  @Bean
  public Map<String, Object> adminConfig() {
    Map<String, Object> configuration = new HashMap<>();
    configuration.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,
        kafkaConstConfig.getBootstrapAddress());
    return configuration;
  }

  @Bean
  public KafkaAdmin kafkaAdmin() {
    return new KafkaAdmin(adminConfig());
  }

  @Bean
  public NewTopic replyAccountTopic() {
    return new NewTopic(kafkaConstConfig.getReplyAccountTopic(),
        kafkaConstConfig.getPartitionCount(),
        kafkaConstConfig.getReplicationFactor());
  }

  @Bean
  public NewTopic replyDialogTopic() {
    return new NewTopic(kafkaConstConfig.getReplyDialogTopic(),
        kafkaConstConfig.getPartitionCount(),
        kafkaConstConfig.getReplicationFactor());
  }

  @Bean
  public NewTopic requestDialogTopic() {
    return new NewTopic(kafkaConstConfig.getRequestDialogTopic(),
        kafkaConstConfig.getPartitionCount(),
        kafkaConstConfig.getReplicationFactor());
  }

  @Bean
  public NewTopic requestNotificationTopic() {
    return new NewTopic(kafkaConstConfig.getRequestNotificationTopic(),
        kafkaConstConfig.getPartitionCount(),
        kafkaConstConfig.getReplicationFactor());
  }
}