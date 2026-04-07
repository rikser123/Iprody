package com.iprody.clients.controller;

import com.iprody.clients.dto.CreateCustomerRequestDto;
import com.iprody.clients.repository.CustomerRepository;
import com.iprody.clients.repository.entity.ContactDetails;
import com.iprody.clients.repository.entity.Customer;
import jakarta.persistence.EntityManager;
import lombok.SneakyThrows;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@Testcontainers
@ActiveProfiles("test")
public class CustomerControllerTest {
  @Container
  @ServiceConnection
  private static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16-alpine");

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private EntityManager entityManager;

  private ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void init() {
    customerRepository.deleteAll();
  }

  @Test
  @SneakyThrows
  void shouldCreate() {
    var customerDto = new CreateCustomerRequestDto();
    customerDto.setEmail("rar@rur.ru");
    customerDto.setFullName("fullName");

    mockMvc.perform(post("/api/v1/customers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(customerDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.fullName").value("fullName"))
        .andExpect(jsonPath("$.email").value("rar@rur.ru"));
  }

  @Test
  @SneakyThrows
  void shouldCreateFailIfMissingParams() {
    var customerDto = new CreateCustomerRequestDto();
    customerDto.setFullName("fullName");

    mockMvc.perform(post("/api/v1/customers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(customerDto)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Invalid request"));
  }

  @Test
  @SneakyThrows
  void shouldUpdate() {
    var customer = createCustomer();
    var customerDto = new CreateCustomerRequestDto();
    customerDto.setEmail("rar1@rur.ru");
    customerDto.setFullName("fullName1");

    mockMvc.perform(put("/api/v1/customers/" + customer.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(customerDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.fullName").value("fullName1"))
        .andExpect(jsonPath("$.email").value("rar1@rur.ru"));
  }

  @Test
  @SneakyThrows
  void shouldGetById() {
    var customer = createCustomer();

    mockMvc.perform(get("/api/v1/customers/" + customer.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.fullName").value(customer.getFullName()))
        .andExpect(jsonPath("$.email").value(customer.getContactDetails().getEmail()));
  }

  @Test
  @SneakyThrows
  void shouldGetList() {
    var customer = createCustomer();

    mockMvc.perform(get("/api/v1/customers")
            .param("fullName", customer.getFullName())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].id").value(customer.getId().toString()))
        .andExpect(jsonPath("$.content[0].fullName").value(customer.getFullName()))
        .andExpect(jsonPath("$.content[0].email").value(customer.getContactDetails().getEmail()));
  }

  private Customer createCustomer() {
    var customer = new Customer();
    customer.setFullName("fullName");

    var details = new ContactDetails();
    details.setEmail("rar" + UUID.randomUUID() + "@rar.ru");
    details.setPhoneNumber("79165678954");
    customer.setContactDetails(details);
    return customerRepository.save(customer);
  }
}
