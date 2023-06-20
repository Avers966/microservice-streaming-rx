package ru.skillbox.diplom.group35.microservice.streaming.rx.impl.kafka;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * KafkaConstConfig
 *
 * @author Marat Safagareev
 */
@Data
@Configuration
public class KafkaConstConfig {
  @Value(value = "${kafka.bootstrap-servers}")
  private String bootstrapAddress;

  @Value(value = "${kafka.consumer.group-id}")
  private String groupId;

  @Value(value = "${kafka.consumer.max-poll-interval-ms}")
  private Integer maxPollInterval;

  @Value(value = "${kafka.consumer.spring-json-trusted-packages}")
  private String trustPackages;

  @Value(value = "${kafka.consumer.concurrency}")
  private Integer concurrency;

  @Value(value = "${kafka.topic.request-dialog}")
  private String requestDialogTopic;

  @Value(value = "${kafka.topic.request-notification}")
  private String requestNotificationTopic;

  @Value(value = "${kafka.topic.reply-account}")
  private String replyAccountTopic;

  @Value(value = "${kafka.topic.reply-dialog}")
  private String replyDialogTopic;

  @Value(value = "${kafka.topic.partition-count}")
  private Integer partitionCount;

  @Value(value = "${kafka.topic.replication-factor}")
  private Short replicationFactor;

  @Value(value = "${kafka.producer.retries}")
  private Integer retries;
}