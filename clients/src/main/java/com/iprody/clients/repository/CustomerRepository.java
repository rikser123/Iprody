package com.iprody.clients.repository;

import com.iprody.clients.repository.entity.Customer;
import com.iprody.clients.repository.specification.CustomerSpecification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID>,
    JpaSpecificationExecutor<CustomerSpecification> {
}
