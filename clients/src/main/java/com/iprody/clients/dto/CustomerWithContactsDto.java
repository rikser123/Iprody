package com.iprody.clients.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerWithContactsDto {
  private UUID id;
  private String fullName;
  private String email;
  private String phoneNumber;
  private Instant createdAt;
  private Instant updatedAt;
}
