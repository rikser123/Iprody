package com.iprody.orders.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iprody.orders.dto.InquiryStatusMessageDto;
import com.iprody.orders.service.InquiryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InquiryConsumer {
  private static final String PAYMENT_STATUS_TOPIC = "payments.status";
  private static final String GROUP_ID = "inquiry-service-group";
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final InquiryService inquiryService;

  @KafkaListener(topics = PAYMENT_STATUS_TOPIC, groupId = GROUP_ID)
  public void consumePaymentStatusMessage(String message) {
    try {
      var inquiryDo = objectMapper.readValue(message, InquiryStatusMessageDto.class);
      log.info("Message in {} successfully received inquiryId", PAYMENT_STATUS_TOPIC, inquiryDo.getOrder());
      inquiryService.changeStatus(inquiryDo.getOrder(), inquiryDo.getStatus());
      log.info("Status  successfully applied for inquiry {}", inquiryDo.getStatus(), inquiryDo.getOrder());
    } catch (Exception e) {
      log.error("Can not parse inquiry message in {}", PAYMENT_STATUS_TOPIC, e);
    }
  }
}
