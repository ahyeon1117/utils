package com.hyeon.backend.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("database")
public class DatabaseProperties {

  private String id;
  private String url;
  private String password;
  private String databaseIp;
  private String databasePort;
  private String databaseName;
  private String databaseType;
  private String databaseOption;
}
