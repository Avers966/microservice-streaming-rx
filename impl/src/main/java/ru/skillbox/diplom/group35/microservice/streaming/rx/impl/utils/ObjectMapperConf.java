package ru.skillbox.diplom.group35.microservice.streaming.rx.impl.utils;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import ru.skillbox.diplom.group35.library.core.dto.streaming.MessageDto;
import ru.skillbox.diplom.group35.library.core.dto.streaming.StreamingMessageDto;

/**
 * New
 *
 * @author Marat Safagareev
 */
@Configuration
public class ObjectMapperConf {

  @Bean
  @Primary
  public ObjectMapper objectMapper() {
    return JsonMapper.builder()
        .configure(MapperFeature.USE_ANNOTATIONS, false)
        .findAndAddModules()
        .build();
  }

  @Bean
  public JavaType mappedType() {
    return new ObjectMapper()
        .getTypeFactory()
        .constructParametricType(StreamingMessageDto.class, MessageDto.class);
  }
}