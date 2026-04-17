package com.iprody.orders.kafka;

import com.iprody.orders.dto.InventoryRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InquiryProducer {
  private static final String TOPIC_INVENTORY_REQUEST = "inventory.request";

  private final KafkaTemplate<String, InventoryRequestDto> kafkaTemplate;

  public void sendMessage(UUID inquiryId, UUID groupId) {
    var messageDto = new InventoryRequestDto();
    messageDto.setInquiryId(inquiryId);
    messageDto.setGroupRefId(groupId);

    kafkaTemplate.send(TOPIC_INVENTORY_REQUEST, inquiryId.toString(), messageDto).whenComplete((result, ex) -> {
      if (Objects.isNull(ex)) {
        log.info("Message inventory.request successfully send {} {}", inquiryId, groupId);
      } else {
        log.error("Message inventory.request fail send {} {}", inquiryId, groupId, ex);
      }
    });
  }
}
