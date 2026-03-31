package com.iprody.orders.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "inquiry")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Inquiry {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "product_ref_id", nullable = false)
  private UUID productRefId;

  @Column(name = "customer_ref_id", nullable = false)
  private UUID customerRefId;

  @Column(name = "group_ref_id")
  private UUID groupRefId;

  @Column(name = "manager_ref_id", nullable = false)
  private UUID managerRefId;

  @Column(name = "source", length = 100, nullable = false)
  private String source;

  @Column(name = "comment")
  private String comment;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  private InquiryStatus status;

  @Column(name = "note")
  private String note;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false, nullable = false)
  private Instant createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", insertable = false)
  private Instant updatedAt;
}
