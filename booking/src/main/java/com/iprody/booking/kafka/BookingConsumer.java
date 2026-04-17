package com.iprody.booking.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iprody.booking.dto.InquiryRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingConsumer {
  private static final String INVENTORY_REQUEST_TOPIC = "inventory.request";
  private static final String BOOKING_GROUP_ID = "booking-service-group";
  private final ObjectMapper objectMapper = new ObjectMapper();

  @KafkaListener(topics = INVENTORY_REQUEST_TOPIC, groupId = BOOKING_GROUP_ID)
  public void consumeInventoryRequest(String message) {
    try {
      var inquiryDo = objectMapper.readValue(message, InquiryRequestDto.class);
      log.info("Message in {} successfully received inquiryId", INVENTORY_REQUEST_TOPIC, inquiryDo.getInquiryId());
    } catch (Exception e) {
        log.error("Can not parse inquiry message in {}", INVENTORY_REQUEST_TOPIC, e);
      }
    }
}
