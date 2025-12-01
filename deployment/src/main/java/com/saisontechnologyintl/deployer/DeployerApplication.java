/*
 * Copyright (c) 2025 Saison Technology International
 * https://saison-technology-intl.com/
 * All rights reserved.
 */
package com.saisontechnologyintl.deployer;

import io.micronaut.runtime.Micronaut;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeployerApplication {

  public static void main(String[] args) {
    log.info("Starting AWS VPC Lattice Deployer...");
    Micronaut.run(DeployerApplication.class, args);
  }
}
