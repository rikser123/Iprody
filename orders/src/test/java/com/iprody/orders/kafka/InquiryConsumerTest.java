package com.iprody.orders.kafka;

import com.iprody.orders.dto.InquiryStatusMessageDto;
import com.iprody.orders.repository.InquiryRepository;
import com.iprody.orders.repository.entity.Inquiry;
import com.iprody.orders.repository.entity.InquirySource;
import com.iprody.orders.repository.entity.InquiryStatus;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.Duration;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@Testcontainers
@ActiveProfiles("test")
@EmbeddedKafka(
  topics = {"payments.status"},
  partitions = 1
)
public class InquiryConsumerTest {
  @Container
  @ServiceConnection
  private static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16-alpine");

  @Autowired
  private KafkaTemplate<String, InquiryStatusMessageDto> kafkaTemplate;

  @Autowired
  private InquiryRepository inquiryRepository;

  @BeforeEach
  void init() {
    inquiryRepository.deleteAll();
  }

  @Test
  @SneakyThrows
  void shouldChangeInquiryStatus() {
    var inquiry = createInquiry();
    var requestDto = new InquiryStatusMessageDto();
    requestDto.setOrder(inquiry.getId());
    requestDto.setStatus(InquiryStatus.PAYMENT);

    kafkaTemplate.send("payments.status", requestDto).get();

    Awaitility.await()
      .atMost(Duration.ofSeconds(5))
      .pollInterval(Duration.ofMillis(500))
      .untilAsserted(() -> {
        var inc = inquiryRepository.findById(inquiry.getId()).get();
        assertThat(inc).isNotNull();
        assertThat(inc.getStatus()).isEqualTo(InquiryStatus.PAYMENT);
      });
  }

  private Inquiry createInquiry() {
    var inquiry = new Inquiry();
    inquiry.setProductRefId(UUID.randomUUID());
    inquiry.setCustomerRefId(UUID.randomUUID());
    inquiry.setManagerRefId(UUID.randomUUID());
    inquiry.setSource(InquirySource.TELEGRAM);
    inquiry.setStatus(InquiryStatus.NEW);
    return inquiryRepository.save(inquiry);
  }
}
