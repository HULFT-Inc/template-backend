/*
 * Copyright (c) 2025 Saison Technology International
 * https://saison-technology-intl.com/
 * All rights reserved.
 */
package com.saisontechnologyintl.template.command;

import java.util.Map;

public interface AwsCommand {
  Map<String, Object> execute();

  void undo();
}
