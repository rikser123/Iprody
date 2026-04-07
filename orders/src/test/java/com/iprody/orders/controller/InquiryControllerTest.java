package com.iprody.orders.controller;

import com.iprody.orders.dto.InquiryCreateRequestDto;
import com.iprody.orders.repository.InquiryRepository;
import com.iprody.orders.repository.entity.Inquiry;
import com.iprody.orders.repository.entity.InquirySource;
import com.iprody.orders.repository.entity.InquiryStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;
import lombok.SneakyThrows;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@Testcontainers
@ActiveProfiles("test")
public class InquiryControllerTest {
  @Container
  @ServiceConnection
  private static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16-alpine");

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private InquiryRepository inquiryRepository;

  private ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void init() {
    inquiryRepository.deleteAll();
  }

  @Test
  @SneakyThrows
  void shouldCreateBook() {
    var dto = new InquiryCreateRequestDto();
    dto.setProductRefId(UUID.randomUUID());
    dto.setCustomerRefId(UUID.randomUUID());
    dto.setSource(InquirySource.TELEGRAM);
    dto.setComment("comment");
    dto.setNote("note");

    mockMvc.perform(post("/api/v1/inquiries")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.source").value("TELEGRAM"))
        .andExpect(jsonPath("$.comment").value("comment"))
        .andExpect(jsonPath("$.note").value("note"));
  }

  @Test
  @SneakyThrows
  void shouldCreateBookFailOnWrongParams() {
    var dto = new InquiryCreateRequestDto();
    dto.setSource(InquirySource.TELEGRAM);
    dto.setComment("comment");
    dto.setNote("note");

    mockMvc.perform(post("/api/v1/inquiries")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isBadRequest()); // TODO проверить формат ошибок после глобального обработчика ошибок
  }

  @Test
  @SneakyThrows
  void shouldFilterData() {
    saveInquiry();

    mockMvc.perform(get("/api/v1/inquiries")
            .contentType(MediaType.APPLICATION_JSON)
            .param("status", String.valueOf(InquiryStatus.IN_PROGRESS)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].status").value("IN_PROGRESS"))
        .andExpect(jsonPath("$.content[0].id").exists())
        .andExpect(jsonPath("$.content[0].source").value("TELEGRAM"));
  }

  @Test
  @SneakyThrows
  void shouldGetById() {
    var inquiry = saveInquiry();

    mockMvc.perform(get("/api/v1/inquiries/" + inquiry.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .param("status", String.valueOf(InquiryStatus.IN_PROGRESS)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(inquiry.getId().toString()))
        .andExpect(jsonPath("$.source").value("TELEGRAM"));
  }

  private Inquiry saveInquiry() {
    var inquiry = new Inquiry();
    inquiry.setStatus(InquiryStatus.IN_PROGRESS);
    inquiry.setProductRefId(UUID.randomUUID());
    inquiry.setCustomerRefId(UUID.randomUUID());
    inquiry.setSource(InquirySource.TELEGRAM);
    return inquiryRepository.save(inquiry);
  }
}
