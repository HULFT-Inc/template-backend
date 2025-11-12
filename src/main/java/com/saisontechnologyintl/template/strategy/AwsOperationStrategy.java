/*
 * Copyright (c) 2025 Saison Technology International
 * https://saison-technology-intl.com/
 * All rights reserved.
 */
package com.saisontechnologyintl.template.strategy;

import java.util.Map;

public interface AwsOperationStrategy {
  Map<String, Object> execute();
}
