package com.iprody.booking.service.impl;

import com.iprody.booking.dto.CreateGroupDto;
import com.iprody.booking.dto.GroupFilterDto;
import com.iprody.booking.dto.UpdateGroupDto;
import com.iprody.booking.mapper.GroupMapper;
import com.iprody.booking.repository.GroupRepository;
import com.iprody.booking.repository.entity.Group;
import com.iprody.booking.repository.specification.GroupSpecification;
import com.iprody.booking.service.GroupService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupServiceImpl implements GroupService {
  private final GroupRepository groupRepository;
  private final GroupMapper groupMapper;

  @Override
  public Group createGroup(CreateGroupDto dto) {
    var group = groupMapper.mapToEntity(dto);
    group = groupRepository.save(group);

    return group;
  }

  @Override
  public Group updateGroup(UUID id, UpdateGroupDto dto) {
    var currentGroup = findById(id);

    if (dto.getCurrentCount() > currentGroup.getLimit()) {
      throw new IllegalStateException("Group is full");
    }

    currentGroup.setCurrentCount(dto.getCurrentCount());
    groupRepository.save(currentGroup);

    return currentGroup;
  }

  @Override
  public Page<Group> findAll(GroupFilterDto dto) {
    var specification = new GroupSpecification(dto);
    var pageRequest = PageRequest.of(dto.getPage(), dto.getPageSize());

    return groupRepository.findAll(specification, pageRequest);
  }

  private Group findById(UUID id) {
    return groupRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Group with id" + id + " not found"));
  }
}
