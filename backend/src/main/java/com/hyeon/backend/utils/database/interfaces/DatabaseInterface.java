package com.hyeon.backend.utils.database.interfaces;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

public interface DatabaseInterface {
  public DataSourceProperties execute(
    String databaseIp,
    String databasePort,
    String database,
    String databaseUser,
    String databasePassword,
    String databaseOption
  );
}
