/*
 * Copyright (c) 2025 Saison Technology International
 * https://saison-technology-intl.com/
 * All rights reserved.
 */
package com.saisontechnologyintl.template;

import com.saisontechnologyintl.template.entity.ChangeRecord;
import com.saisontechnologyintl.template.entity.DocumentType;
import com.saisontechnologyintl.template.entity.FileType;
import com.saisontechnologyintl.template.entity.StorageSystem;
import com.saisontechnologyintl.template.service.ChangeRecordService;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Controller("/change-tracker/changes")
@Tag(name = "Change Management")
@RequiredArgsConstructor
public class ChangeRecordController {
  private final ChangeRecordService changeRecordService;

  @Post
  @Operation(summary = "Record a change")
  public ChangeRecord recordChange(@Body ChangeRequest request) {
    return changeRecordService.recordChange(
        request.orgShortcode,
        request.documentType,
        request.documentId,
        request.fieldName,
        request.oldValue,
        request.newValue,
        request.changedBy,
        request.fileName,
        request.fileType,
        request.fileLocation,
        request.storageSystem,
        request.systemDetail);
  }

  @Get("/org/{shortcode}")
  @Operation(summary = "Get changes by organization")
  public List<ChangeRecord> getByOrg(@PathVariable String shortcode) {
    return changeRecordService.getChangesByOrg(shortcode);
  }

  @Get("/type/{documentType}")
  @Operation(summary = "Get changes by document type")
  public List<ChangeRecord> getByType(@PathVariable DocumentType documentType) {
    return changeRecordService.getChangesByDocumentType(documentType);
  }

  @Get("/org/{shortcode}/type/{documentType}")
  @Operation(summary = "Get changes by org and type")
  public List<ChangeRecord> getByOrgAndType(
      @PathVariable String shortcode, @PathVariable DocumentType documentType) {
    return changeRecordService.getChangesByOrgAndType(shortcode, documentType);
  }

  @Get("/document/{documentId}")
  @Operation(summary = "Get changes by document ID")
  public List<ChangeRecord> getByDocumentId(@PathVariable String documentId) {
    return changeRecordService.getChangesByDocumentId(documentId);
  }

  public record ChangeRequest(
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
      String systemDetail) {}
}
