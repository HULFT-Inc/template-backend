/*
 * Copyright (c) 2025 Saison Technology International
 * https://saison-technology-intl.com/
 * All rights reserved.
 */
package com.saisontechnologyintl.template.service;

import com.saisontechnologyintl.template.entity.ChangeRecord;
import com.saisontechnologyintl.template.entity.DocumentType;
import com.saisontechnologyintl.template.entity.FileType;
import com.saisontechnologyintl.template.entity.Organization;
import com.saisontechnologyintl.template.entity.StorageSystem;
import com.saisontechnologyintl.template.repository.ChangeRecordRepository;
import com.saisontechnologyintl.template.repository.OrganizationRepository;
import jakarta.inject.Singleton;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
public class ChangeRecordService {
  private final ChangeRecordRepository changeRecordRepository;
  private final OrganizationRepository organizationRepository;

  public ChangeRecord recordChange(
      String orgShortcode,
      DocumentType documentType,
      String documentId,
      String fieldName,
      String oldValue,
      String newValue,
      String changedBy,
      String fileName,
      FileType fileType,
      String fileLocation,
      StorageSystem storageSystem,
      String systemDetail) {
    Organization org =
        organizationRepository
            .findByShortcode(orgShortcode)
            .orElseThrow(
                () -> new IllegalArgumentException("Organization not found: " + orgShortcode));

    ChangeRecord record = new ChangeRecord();
    record.setOrganization(org);
    record.setDocumentType(documentType);
    record.setDocumentId(documentId);
    record.setFieldName(fieldName);
    record.setOldValue(oldValue);
    record.setNewValue(newValue);
    record.setChangedBy(changedBy);
    record.setFileName(fileName);
    record.setFileType(fileType);
    record.setFileLocation(fileLocation);
    record.setStorageSystem(storageSystem);
    record.setSystemDetail(systemDetail);

    return changeRecordRepository.save(record);
  }

  public List<ChangeRecord> getChangesByOrg(String shortcode) {
    return changeRecordRepository.findByOrganizationShortcode(shortcode);
  }

  public List<ChangeRecord> getChangesByDocumentType(DocumentType documentType) {
    return changeRecordRepository.findByDocumentType(documentType);
  }

  public List<ChangeRecord> getChangesByOrgAndType(String shortcode, DocumentType documentType) {
    return changeRecordRepository.findByOrganizationShortcodeAndDocumentType(
        shortcode, documentType);
  }

  public List<ChangeRecord> getChangesByDocumentId(String documentId) {
    return changeRecordRepository.findByDocumentId(documentId);
  }
}
