package com.iprody.payments.kafka;

import com.iprody.payments.dto.InquiryStatus;
import com.iprody.payments.dto.InquiryStatusMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentProducer {
  private static final String TOPIC_PAYMENT_STATUS = "payments.status";
  private final KafkaTemplate<String, InquiryStatusMessageDto> kafkaTemplate;

  public void sendPaymentStatusMessage(UUID orderId, InquiryStatus status) {
    var dto = new InquiryStatusMessageDto();
    dto.setOrder(orderId);
    dto.setStatus(status);

    kafkaTemplate.send(TOPIC_PAYMENT_STATUS, orderId.toString(), dto).whenComplete((result, ex) -> {
      if (Objects.isNull(ex)) {
        log.info("Message payments.status successfully send {} {}", dto.getStatus(), dto.getOrder());
      } else {
        log.error("Message payments.status fail send {} {}", dto.getStatus(), dto.getOrder(), ex);
      }
    });
  }
}
