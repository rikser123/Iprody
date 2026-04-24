package com.iprody.booking.service;

import com.iprody.booking.dto.CreateGroupDto;
import com.iprody.booking.dto.GroupFilterDto;
import com.iprody.booking.exception.InquiryGroupException;
import com.iprody.booking.mapper.GroupMapper;
import com.iprody.booking.repository.GroupRepository;
import com.iprody.booking.repository.entity.Group;
import com.iprody.booking.repository.entity.GroupInquiry;
import com.iprody.booking.repository.specification.GroupSpecification;
import com.iprody.booking.service.impl.GroupServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;
import java.util.UUID;

public class GroupServiceTest {
  private GroupRepository groupRepository;
  private GroupMapper mapper = Mappers.getMapper(GroupMapper.class);
  private GroupService groupService;

  @BeforeEach
  void init() {
    groupRepository = Mockito.mock(GroupRepository.class);
    groupService = new GroupServiceImpl(groupRepository, mapper);
  }

  @Test
  void shouldCreateGroup() {
    var dto = createGroupDto();

    when(groupRepository.save(any())).thenReturn(createGroup());
    var result = groupService.createGroup(dto);

    assertThat(result.getCurrentCount()).isEqualTo(dto.getCurrentCount());
    assertThat(result.getLimit()).isEqualTo(dto.getLimit());
  }

  @Test
  void shouldIncreaseCount() {
    var group = createGroup();

    when(groupRepository.findById(any())).thenReturn(Optional.of(group));
    when(groupRepository.save(argThat(g -> {
      assertThat(g.getCurrentCount()).isEqualTo(4);
      return true;
    }))).thenReturn(group);

    var result = groupService.increaseCount(UUID.randomUUID(), UUID.randomUUID());

    assertThat(result.getCurrentCount()).isEqualTo(4);
    assertThat(result.getLimit()).isEqualTo(5);
    assertThat(result.getInquiries().size()).isEqualTo(2);
  }

  @Test
  void shouldThrowErrorThanInquiryInGroup() {
    var group = createGroup();
    when(groupRepository.findById(any())).thenReturn(Optional.of(group));

    assertThatThrownBy(() -> groupService.increaseCount(UUID.randomUUID(),
      group.getInquiries().stream().toList().get(0).getInquiryId()))
      .isInstanceOf(InquiryGroupException.class);
  }

  @Test
  void updateThrowErrorThenCountGreaterLimit() {
    var group = createGroup();
    group.setLimit(1);

    when(groupRepository.findById(any())).thenReturn(Optional.of(group));

    assertThatThrownBy(() -> groupService.increaseCount(UUID.randomUUID(), UUID.randomUUID()))
    .isInstanceOf(IllegalStateException.class);
  }

  @Test
  void shouldDecreaseCount() {
    var group = createGroup();

    when(groupRepository.findById(any())).thenReturn(Optional.of(group));
    when(groupRepository.save(argThat(g -> {
      assertThat(g.getCurrentCount()).isEqualTo(2);
      return true;
    }))).thenReturn(group);

    var result = groupService.decreaseCount(UUID.randomUUID(),
      group.getInquiries().stream().toList().get(0).getInquiryId());

    assertThat(result.getCurrentCount()).isEqualTo(2);
    assertThat(result.getLimit()).isEqualTo(5);
    assertThat(group.getInquiries().size()).isEqualTo(0);
  }

  @Test
  void updateThrowErrorThenCountIsNegative() {
    var group = createGroup();
    group.setCurrentCount(0);

    when(groupRepository.findById(any())).thenReturn(Optional.of(group));

    assertThatThrownBy(() -> groupService.decreaseCount(UUID.randomUUID(),
      group.getInquiries().stream().toList().get(0).getInquiryId()))
      .isInstanceOf(IllegalStateException.class);
  }

  @Test
  void shouldThrowErrorThanInquiryIsNotInGroup() {
    var group = createGroup();
    when(groupRepository.findById(any())).thenReturn(Optional.of(group));

    assertThatThrownBy(() -> groupService.decreaseCount(UUID.randomUUID(), UUID.randomUUID()))
      .isInstanceOf(InquiryGroupException.class);
  }

  @Test
  void findAll() {
    var filterDto = new GroupFilterDto();
    filterDto.setGroupRefId(UUID.randomUUID());
    filterDto.setPage(0);
    groupService.findAll(filterDto);

    verify(groupRepository).findAll(any(GroupSpecification.class), any(PageRequest.class));
  }

  private static Group createGroup() {
    var group = new Group();
    group.setId(UUID.randomUUID());
    group.setLimit(5);
    group.setCurrentCount(3);

    var groupInquiry = new GroupInquiry();
    groupInquiry.setGroup(group);
    groupInquiry.setInquiryId(UUID.randomUUID());
    group.getInquiries().add(groupInquiry);

    return group;
  }

  private static CreateGroupDto createGroupDto() {
    var groupDto = new CreateGroupDto();
    groupDto.setGroupRefId(UUID.randomUUID());
    groupDto.setLimit(5);
    groupDto.setCurrentCount(3);

    return groupDto;
  }
}
