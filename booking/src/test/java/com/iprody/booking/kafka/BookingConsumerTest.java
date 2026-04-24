package com.iprody.booking.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iprody.booking.dto.InquiryRequestDto;
import com.iprody.booking.repository.GroupRepository;
import com.iprody.booking.repository.entity.Group;
import com.iprody.booking.repository.entity.GroupInquiry;
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
  topics = {"inventory.request", "cancellation.request"},
  partitions = 1
)
public class BookingConsumerTest {
  @Container
  @ServiceConnection
  private static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16-alpine");

  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  @Autowired
  private GroupRepository groupRepository;

  private ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void init() {
    groupRepository.deleteAll();
  }

  @Test
  @SneakyThrows
  void shouldIncreaseCount() {
    var group = createGroup();
    var requestDto = new InquiryRequestDto();
    requestDto.setGroupRefId(group.getId());
    requestDto.setInquiryId(UUID.randomUUID());

    var message = objectMapper.writeValueAsString(requestDto);
    kafkaTemplate.send("inventory.request", message).get();

    Awaitility.await()
      .atMost(Duration.ofSeconds(5))
      .pollInterval(Duration.ofMillis(500))
      .untilAsserted(() -> {
        var gr = groupRepository.findById(group.getId()).get();
        assertThat(gr).isNotNull();
        assertThat(gr.getCurrentCount()).isEqualTo(1);
        assertThat(gr.getInquiries().stream().toList().get(0).getInquiryId()).isEqualTo(requestDto.getInquiryId());
      });
  }

  @Test
  @SneakyThrows
  void shouldIncreaseCountOnlyOnceForSameInquiry() {
    var group = createGroup();
    var requestDto = new InquiryRequestDto();
    requestDto.setGroupRefId(group.getId());
    requestDto.setInquiryId(UUID.randomUUID());

    var message = objectMapper.writeValueAsString(requestDto);
    kafkaTemplate.send("inventory.request", message).get();
    kafkaTemplate.send("inventory.request", message).get();

    Awaitility.await()
      .atMost(Duration.ofSeconds(5))
      .pollInterval(Duration.ofMillis(500))
      .untilAsserted(() -> {
        var gr = groupRepository.findById(group.getId()).get();
        assertThat(gr).isNotNull();
        assertThat(gr.getCurrentCount()).isEqualTo(1);
        assertThat(gr.getInquiries().stream().toList().get(0).getInquiryId()).isEqualTo(requestDto.getInquiryId());
      });
  }

  @Test
  @SneakyThrows
  void shouldDecreaseCount() {
    var group = createGroupWithInquiry();
    var groupInquiry = group.getInquiries().stream().toList().get(0);
    var requestDto = new InquiryRequestDto();
    requestDto.setGroupRefId(group.getId());
    requestDto.setInquiryId(groupInquiry.getInquiryId());

    var message = objectMapper.writeValueAsString(requestDto);
    kafkaTemplate.send("cancellation.request", message).get();

    Awaitility.await()
      .atMost(Duration.ofSeconds(5))
      .pollInterval(Duration.ofMillis(500))
      .untilAsserted(() -> {
        var gr = groupRepository.findById(group.getId()).get();
        assertThat(gr).isNotNull();
        assertThat(gr.getCurrentCount()).isEqualTo(1);
        assertThat(gr.getInquiries().size()).isEqualTo(0);
      });
  }

  @Test
  @SneakyThrows
  void shouldDecreaseCountOnlyOnce() {
    var group = createGroupWithInquiry();
    var groupInquiry = group.getInquiries().stream().toList().get(0);
    var requestDto = new InquiryRequestDto();
    requestDto.setGroupRefId(group.getId());
    requestDto.setInquiryId(groupInquiry.getInquiryId());

    var message = objectMapper.writeValueAsString(requestDto);
    kafkaTemplate.send("cancellation.request", message).get();
    kafkaTemplate.send("cancellation.request", message).get();

    Awaitility.await()
      .atMost(Duration.ofSeconds(5))
      .pollInterval(Duration.ofMillis(500))
      .untilAsserted(() -> {
        var gr = groupRepository.findById(group.getId()).get();
        assertThat(gr).isNotNull();
        assertThat(gr.getCurrentCount()).isEqualTo(1);
        assertThat(gr.getInquiries().size()).isEqualTo(0);
      });
  }

  private Group createGroup() {
    var group = new Group();
    group.setCurrentCount(0);
    group.setLimit(4);
    return groupRepository.save(group);
  }

  private Group createGroupWithInquiry() {
    var group = new Group();
    group.setCurrentCount(2);
    group.setLimit(4);

    var groupInquiry = new GroupInquiry();
    groupInquiry.setGroup(group);
    groupInquiry.setInquiryId(UUID.randomUUID());
    group.getInquiries().add(groupInquiry);
    return groupRepository.save(group);
  }
}
