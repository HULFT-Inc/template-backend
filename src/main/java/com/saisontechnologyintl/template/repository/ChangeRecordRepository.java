/*
 * Copyright (c) 2025 Saison Technology International
 * https://saison-technology-intl.com/
 * All rights reserved.
 */
package com.saisontechnologyintl.template.repository;

import com.saisontechnologyintl.template.entity.ChangeRecord;
import com.saisontechnologyintl.template.entity.DocumentType;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import java.util.List;

@Repository
public interface ChangeRecordRepository extends JpaRepository<ChangeRecord, Long> {
  List<ChangeRecord> findByOrganizationShortcode(String shortcode);

  List<ChangeRecord> findByDocumentType(DocumentType documentType);

  List<ChangeRecord> findByOrganizationShortcodeAndDocumentType(
      String shortcode, DocumentType documentType);

  List<ChangeRecord> findByDocumentId(String documentId);
}
