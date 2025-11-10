/*
 * Copyright (c) 2025 Saison Technology International
 * https://saison-technology-intl.com/
 * All rights reserved.
 */
package com.saisontechnologyintl.template.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

class ArchitectureTest {

  private final JavaClasses importedClasses =
      new ClassFileImporter().importPackages("com.saisontechnologyintl.template");

  @Test
  void controllersShouldOnlyBeAccessedByControllers() {
    ArchRule rule =
        classes()
            .that()
            .resideInAPackage("..controller..")
            .should()
            .onlyBeAccessed()
            .byClassesThat()
            .resideInAnyPackage("..controller..", "..test..");

    rule.check(importedClasses);
  }

  @Test
  void servicesShouldNotDependOnControllers() {
    ArchRule rule =
        noClasses()
            .that()
            .resideInAPackage("..service..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("..controller..");

    rule.check(importedClasses);
  }

  @Test
  void controllersShouldBeAnnotatedWithController() {
    ArchRule rule =
        classes()
            .that()
            .haveSimpleNameEndingWith("Controller")
            .should()
            .beAnnotatedWith("io.micronaut.http.annotation.Controller");

    rule.check(importedClasses);
  }

  @Test
  void noClassesShouldUseSystemOut() {
    ArchRule rule =
        noClasses()
            .should()
            .callMethod(System.class, "out")
            .orShould()
            .callMethod(System.class, "err");

    rule.check(importedClasses);
  }

  @Test
  void layeredArchitectureShouldBeRespected() {
    ArchRule rule =
        layeredArchitecture()
            .consideringAllDependencies()
            .layer("Controller")
            .definedBy("..controller..")
            .layer("Service")
            .definedBy("..service..")
            .layer("Repository")
            .definedBy("..repository..")
            .whereLayer("Controller")
            .mayNotBeAccessedByAnyLayer()
            .whereLayer("Service")
            .mayOnlyBeAccessedByLayers("Controller")
            .whereLayer("Repository")
            .mayOnlyBeAccessedByLayers("Service");

    rule.check(importedClasses);
  }
}
