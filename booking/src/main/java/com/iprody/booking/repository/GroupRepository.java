package com.iprody.booking.repository;

import com.iprody.booking.repository.entity.Group;
import com.iprody.booking.repository.specification.GroupSpecification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface GroupRepository extends JpaRepository<Group, UUID>,
    JpaSpecificationExecutor<GroupSpecification> {
}
