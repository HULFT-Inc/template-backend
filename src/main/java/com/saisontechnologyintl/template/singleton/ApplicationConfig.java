/*
 * Copyright (c) 2025 Saison Technology International
 * https://saison-technology-intl.com/
 * All rights reserved.
 */
package com.saisontechnologyintl.template.singleton;

import java.util.Properties;

public class ApplicationConfig {

  private static volatile ApplicationConfig instance;
  private final Properties properties;

  private ApplicationConfig() {
    this.properties = new Properties();
    loadDefaultProperties();
  }

  public static ApplicationConfig getInstance() {
    if (instance == null) {
      synchronized (ApplicationConfig.class) {
        if (instance == null) {
          instance = new ApplicationConfig();
        }
      }
    }
    return instance;
  }

  private void loadDefaultProperties() {
    properties.setProperty("app.name", "template-backend");
    properties.setProperty("app.version", "0.1");
    properties.setProperty("metrics.enabled", "true");
    properties.setProperty("tracing.enabled", "true");
  }

  public String getProperty(String key) {
    return properties.getProperty(key);
  }

  public String getProperty(String key, String defaultValue) {
    return properties.getProperty(key, defaultValue);
  }

  public void setProperty(String key, String value) {
    properties.setProperty(key, value);
  }
}
