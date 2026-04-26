package com.iprody.orders.kafka;

import com.iprody.orders.dto.InventoryRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class InquiryProducer {
  private static final String TOPIC_INVENTORY_REQUEST = "inventory.request";
  private static final String TOPIC_CANCELLATION_REQUEST = "cancellation.request";

  private final KafkaTemplate<String, InventoryRequestDto> kafkaTemplate;

  public void sendInventoryMessage(UUID inquiryId, UUID groupId) {
    var messageDto = createRequestDto(inquiryId, groupId);

    kafkaTemplate.send(TOPIC_INVENTORY_REQUEST, inquiryId.toString(), messageDto).whenComplete((result, ex) -> {
      if (Objects.isNull(ex)) {
        log.info("Message inventory.request successfully send {} {}", inquiryId, groupId);
      } else {
        log.error("Message inventory.request fail send {} {}", inquiryId, groupId, ex);
      }
    });
  }

  public void sendCancellationRequestMessage(UUID inquiryId, UUID groupId) {
    var messageDto = createRequestDto(inquiryId, groupId);

    kafkaTemplate.send(TOPIC_CANCELLATION_REQUEST, inquiryId.toString(), messageDto).whenComplete((result, ex) -> {
      if (Objects.isNull(ex)) {
        log.info("Message cancellation.request successfully send {} {}", inquiryId, groupId);
      } else {
        log.error("Message cancellation.request fail send {} {}", inquiryId, groupId, ex);
      }
    });
  }

  private InventoryRequestDto createRequestDto(UUID inquiryId, UUID groupId) {
    var messageDto = new InventoryRequestDto();
    messageDto.setInquiryId(inquiryId);
    messageDto.setGroupRefId(groupId);
    return messageDto;
  }
}
