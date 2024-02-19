package com.hyeon.backend.utils.database;

import com.hyeon.backend.utils.database.interfaces.DatabaseInterface;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

@Slf4j
@Setter
public class Database {

  private DatabaseInterface databaseInterface;

  public DataSourceProperties execute(
    String databaseIp,
    String databasePort,
    String databaseName,
    String databaseUser,
    String databasePassword,
    String databaseOption
  ) {
    DataSourceProperties dataSourceProperties = null;
    if (databaseInterface == null) {
      log.info("DataBase setting is null");
      return dataSourceProperties;
    } else {
      if (databaseOption == null) {
        databaseOption = "";
      }
      return databaseInterface.execute(
        databaseIp,
        databasePort,
        databaseName,
        databaseUser,
        databasePassword,
        databaseOption
      );
    }
  }
}
