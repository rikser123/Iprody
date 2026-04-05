package com.iprody.payments.repository;

import com.iprody.payments.repository.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID>,
    JpaSpecificationExecutor<Payment> {
}
