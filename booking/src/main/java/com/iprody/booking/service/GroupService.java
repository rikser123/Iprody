package com.iprody.booking.service;

import com.iprody.booking.dto.CreateGroupDto;
import com.iprody.booking.dto.GroupFilterDto;
import com.iprody.booking.dto.UpdateGroupDto;
import com.iprody.booking.repository.entity.Group;
import org.springframework.data.domain.Page;

import java.util.UUID;

/**
 * Service interface for managing group-related operations.
 * Provides business logic for creating, updating, and retrieving groups.
 */
public interface GroupService {

  /**
   * Creates a new group based on the provided data.
   *
   * @param dto the data transfer object containing group creation information
   * @return the created Group entity
   */
  Group createGroup(CreateGroupDto dto);

  /**
   * Updates an existing group identified by the given ID.
   *
   * @param id the unique identifier of the group to update
   * @param dto the data transfer object containing updated group information
   * @return the updated Group entity
   */
  Group updateGroup(UUID id, UpdateGroupDto dto);

  /**
   * Retrieves a paginated list of groups based on the provided filter criteria.
   *
   * @param dto the filter data transfer object containing pagination parameters
   *            and filtering conditions
   * @return a Page object containing groups matching the filter criteria
   */
  Page<Group> findAll(GroupFilterDto dto);
}
