package com.iprody.orders.repository;

import com.iprody.orders.repository.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface InquiryRepository extends JpaRepository<Inquiry, UUID>,
    JpaSpecificationExecutor<Inquiry> {
}
