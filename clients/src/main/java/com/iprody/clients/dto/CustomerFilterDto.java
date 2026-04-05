package com.iprody.clients.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerFilterDto {
  private String fullName;
  private int pageSize = 25;
  private int pageNumber;
}
