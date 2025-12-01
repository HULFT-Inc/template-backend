/*
 * Copyright (c) 2025 Saison Technology International
 * https://saison-technology-intl.com/
 * All rights reserved.
 */
package com.saisontechnologyintl.template;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.saisontechnologyintl.template.entity.ChangeRecord;
import com.saisontechnologyintl.template.entity.DocumentType;
import com.saisontechnologyintl.template.entity.Organization;
import com.saisontechnologyintl.template.repository.ChangeRecordRepository;
import com.saisontechnologyintl.template.repository.OrganizationRepository;
import com.saisontechnologyintl.template.service.ChangeRecordService;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Map;

@MicronautTest
public class ChangeManagementStepDefinitions {
  @Inject ChangeRecordService changeRecordService;
  @Inject OrganizationRepository organizationRepository;
  @Inject ChangeRecordRepository changeRecordRepository;

  private ChangeRecord lastChange;
  private List<ChangeRecord> changes;

  @Given("the application is running")
  public void applicationIsRunning() {
    assertNotNull(changeRecordService);
  }

  @Given("organizations {string} and {string} exist")
  public void organizationsExist(String org1, String org2) {
    if (organizationRepository.findByShortcode(org1).isEmpty()) {
      Organization o1 = new Organization();
      o1.setShortcode(org1);
      o1.setName(org1 + " Corporation");
      organizationRepository.save(o1);
    }
    if (organizationRepository.findByShortcode(org2).isEmpty()) {
      Organization o2 = new Organization();
      o2.setShortcode(org2);
      o2.setName(org2 + " Corporation");
      organizationRepository.save(o2);
    }
  }

  @When("I record a change for organization {string}")
  public void recordChange(String org, DataTable dataTable) {
    Map<String, String> data = dataTable.asMap(String.class, String.class);
    lastChange =
        changeRecordService.recordChange(
            org,
            DocumentType.valueOf(data.get("documentType")),
            data.get("documentId"),
            data.get("fieldName"),
            data.get("oldValue"),
            data.get("newValue"),
            data.get("changedBy"));
  }

  @Then("the change is recorded successfully")
  public void changeRecordedSuccessfully() {
    assertNotNull(lastChange);
    assertNotNull(lastChange.getId());
  }

  @Given("changes exist for organization {string}")
  public void changesExistForOrg(String org) {
    if (changeRecordRepository.findByOrganizationShortcode(org).isEmpty()) {
      changeRecordService.recordChange(
          org, DocumentType.PO, "PO-TEST", "field", "old", "new", "test");
    }
  }

  @When("I query changes for organization {string}")
  public void queryChangesByOrg(String org) {
    changes = changeRecordService.getChangesByOrg(org);
  }

  @Then("I receive all changes for {string}")
  public void receiveChangesForOrg(String org) {
    assertNotNull(changes);
    assertFalse(changes.isEmpty());
  }

  @Given("changes exist for document type {string}")
  public void changesExistForType(String type) {
    if (changeRecordRepository.findByDocumentType(DocumentType.valueOf(type)).isEmpty()) {
      changeRecordService.recordChange(
          "GPS", DocumentType.valueOf(type), "DOC-TEST", "field", "old", "new", "test");
    }
  }

  @When("I query changes for document type {string}")
  public void queryChangesByType(String type) {
    changes = changeRecordService.getChangesByDocumentType(DocumentType.valueOf(type));
  }

  @Then("I receive all invoice changes")
  public void receiveInvoiceChanges() {
    assertNotNull(changes);
    assertFalse(changes.isEmpty());
  }

  @Given("changes exist for organization {string} and document type {string}")
  public void changesExistForOrgAndType(String org, String type) {
    if (changeRecordRepository
        .findByOrganizationShortcodeAndDocumentType(org, DocumentType.valueOf(type))
        .isEmpty()) {
      changeRecordService.recordChange(
          org, DocumentType.valueOf(type), "DOC-TEST", "field", "old", "new", "test");
    }
  }

  @When("I query changes for organization {string} and document type {string}")
  public void queryChangesByOrgAndType(String org, String type) {
    changes = changeRecordService.getChangesByOrgAndType(org, DocumentType.valueOf(type));
  }

  @Then("I receive filtered changes")
  public void receiveFilteredChanges() {
    assertNotNull(changes);
    assertFalse(changes.isEmpty());
  }

  @Then("I can query changes for document {string}")
  public void queryChangesByDocument(String docId) {
    List<ChangeRecord> docChanges = changeRecordService.getChangesByDocumentId(docId);
    assertNotNull(docChanges);
    assertFalse(docChanges.isEmpty());
  }
}
