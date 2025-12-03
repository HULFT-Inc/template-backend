/*
 * Copyright (c) 2025 Saison Technology International
 * https://saison-technology-intl.com/
 * All rights reserved.
 */
package com.saisontechnologyintl.template.entity;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Data;

@Entity
@Table(name = "change_records")
@Data
@Serdeable
public class ChangeRecord {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "organization_id", nullable = false)
  private Organization organization;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private DocumentType documentType;

  @Column(nullable = false)
  private String documentId;

  @Column(nullable = false)
  private String fieldName;

  @Column(columnDefinition = "TEXT")
  private String oldValue;

  @Column(columnDefinition = "TEXT")
  private String newValue;

  @Column(nullable = false)
  private String changedBy;

  @Column(nullable = false)
  private LocalDateTime changedAt;

  private String fileName;

  @Enumerated(EnumType.STRING)
  private FileType fileType;

  private String fileLocation;

  @Enumerated(EnumType.STRING)
  private StorageSystem storageSystem;

  @Column(columnDefinition = "TEXT")
  private String systemDetail;

  @PrePersist
  protected void onCreate() {
    changedAt = LocalDateTime.now();
  }
}
