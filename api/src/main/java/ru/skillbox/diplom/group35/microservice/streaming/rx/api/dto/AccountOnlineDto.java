package ru.skillbox.diplom.group35.microservice.streaming.rx.api.dto;

import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.skillbox.diplom.group35.library.core.dto.base.BaseDto;

/**
 * AccountOnlineDto
 *
 * @author Marat Safagareev
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AccountOnlineDto extends BaseDto {

  private UUID id;
  private ZonedDateTime lastOnlineTime;
  private Boolean isOnline;
}
