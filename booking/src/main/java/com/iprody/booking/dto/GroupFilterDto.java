package com.iprody.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupFilterDto {
  private UUID groupRefId;
  private Integer availablePlaces;
  private Integer page = 1;
  private Integer pageSize = 25;
}
