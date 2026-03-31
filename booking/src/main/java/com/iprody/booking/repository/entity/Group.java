package com.iprody.booking.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "groups")
public class Group {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "group_ref_id", nullable = false)
  private UUID groupRefId;

  @Column(name = "current_count")
  private Integer currentCount;

  @Column(name = "limit")
  private Integer limit;
}
