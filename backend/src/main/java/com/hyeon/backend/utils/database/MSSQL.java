package com.hyeon.backend.utils.database;

import com.hyeon.backend.utils.database.interfaces.DatabaseInterface;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

public class MSSQL implements DatabaseInterface {

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
    dataSourceProperties.setDriverClassName(
      "com.microsoft.sqlserver.jdbc.SQLServerDriver"
    );
    dataSourceProperties.setPassword(databasePassword);
    dataSourceProperties.setUsername(databaseUser);
    dataSourceProperties.setUrl(
      "jdbc:sqlserver://" +
      databaseIp +
      ":" +
      databasePort +
      ";databaseName=" +
      databaseName +
      databaseOption
    );
    return dataSourceProperties;
  }
}
