package com.iprody.booking.mapper;

import com.iprody.booking.dto.CreateGroupDto;
import com.iprody.booking.repository.entity.Group;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GroupMapper {
  Group mapToEntity(CreateGroupDto dto);
}
