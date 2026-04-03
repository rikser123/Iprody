package com.iprody.clients.mapper;

import com.iprody.clients.dto.CreateCustomerDto;
import com.iprody.clients.dto.CustomerWithContactsDto;
import com.iprody.clients.repository.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
  @Mapping(source = "email", target = "contactDetails.email")
  @Mapping(source = "phoneNumber", target = "contactDetails.phoneNumber")
  Customer mapToEntity(CreateCustomerDto dto);

  @Mapping(source = "contactDetails.email", target = "email")
  @Mapping(source = "contactDetails.phoneNumber", target = "phoneNumber")
  CustomerWithContactsDto mapToDto(Customer entity);

  @Mapping(source = "email", target = "contactDetails.email")
  @Mapping(source = "phoneNumber", target = "contactDetails.phoneNumber")
  void update(@MappingTarget Customer customer, CreateCustomerDto dto);
}
