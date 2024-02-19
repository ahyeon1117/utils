package com.hyeon.backend.utils.database;

import com.hyeon.backend.utils.database.interfaces.DatabaseInterface;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

public class H2 implements DatabaseInterface {

  @Override
  public DataSourceProperties execute(
    String databaseIp,
    String databasePort,
    String databaseName,
    String databaseUser,
    String databasePassword,
    String databaseOption
  ) {
    DataSourceProperties dataSourceProperties = new DataSourceProperties();
    dataSourceProperties.setDriverClassName("org.h2.Driver");
    dataSourceProperties.setPassword(databasePassword);
    dataSourceProperties.setUsername(databaseUser);
    dataSourceProperties.setUrl("jdbc:h2:" + databaseIp);
    return dataSourceProperties;
  }
}
