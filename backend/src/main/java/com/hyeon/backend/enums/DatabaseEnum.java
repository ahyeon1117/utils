package com.hyeon.backend.enums;

import com.hyeon.backend.utils.database.Database;
import com.hyeon.backend.utils.database.H2;
import com.hyeon.backend.utils.database.MSSQL;
import com.hyeon.backend.utils.database.Maria;
import com.hyeon.backend.utils.database.MySQL;
import com.hyeon.backend.utils.database.Oracle;
import com.hyeon.backend.utils.database.PostgreSQL;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

public enum DatabaseEnum {
  POSTGRES,
  ORACLE,
  MARIA,
  MSSQL,
  MYSQL,
  NODB;

  public DataSourceProperties databaseSelecter(
    DatabaseEnum databasePropety,
    String databaseIp,
    String databasePort,
    String databaseName,
    String id,
    String password,
    String option
  ) {
    Database database = new Database();
    switch (databasePropety) {
      case POSTGRES:
        database.setDatabaseInterface(new PostgreSQL());
        break;
      case ORACLE:
        database.setDatabaseInterface(new Oracle());
        break;
      case MSSQL:
        database.setDatabaseInterface(new MSSQL());
        break;
      case MYSQL:
        database.setDatabaseInterface(new MySQL());
        break;
      case MARIA:
        database.setDatabaseInterface(new Maria());
        break;
      case NODB:
        database.setDatabaseInterface(new H2());
        break;
      default:
        break;
    }

    return database.execute(
      databaseIp,
      databasePort,
      databaseName,
      id,
      password,
      option
    );
  }
}
