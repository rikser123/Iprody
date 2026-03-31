package com.iprody.clients.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor@NoArgsConstructor
public class ClientFilterDto {
  private String fullName;
  private int pageSize;
  private int pageNumber;
}
