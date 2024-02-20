package com.hyeon.backend.config;

import com.hyeon.backend.enums.DatabaseEnum;
import com.hyeon.backend.properties.DatabaseProperties;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties.CoreConfiguration;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@Configuration
@EnableTransactionManagement
public class DatabaseConfig {

  @Autowired
  private MybatisProperties mybatisProperties;

  @Autowired
  private DatabaseProperties databaseProperties;

  @Bean(name = "databaseDatasource", destroyMethod = "close")
  @ConfigurationProperties("spring.database.datasource.hikari")
  public HikariDataSource databaseDatasource() {
    log.info("{}", databaseProperties.getDatabaseType());
    DatabaseEnum databaseEnum = DatabaseEnum.valueOf(
      databaseProperties.getDatabaseType()
    );
    DataSourceProperties dataSourceProperties = databaseEnum.databaseSelecter(
      databaseEnum,
      databaseProperties.getDatabaseIp(),
      databaseProperties.getDatabasePort(),
      databaseProperties.getDatabaseName(),
      databaseProperties.getId(),
      databaseProperties.getPassword(),
      databaseProperties.getDatabaseOption()
    );
    log.info("url : " + dataSourceProperties.getUrl());
    return dataSourceProperties
      .initializeDataSourceBuilder()
      .type(HikariDataSource.class)
      .build();
  }

  @Bean(name = "databaseSqlSessionFactory")
  public SqlSessionFactory databaseSqlSessionFactory(
    @Qualifier("databaseDatasource") DataSource dataSource,
    ApplicationContext applicationContext
  ) throws Exception {
    SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
    sqlSessionFactoryBean.setVfs(SpringBootVFS.class);
    sqlSessionFactoryBean.setDataSource(dataSource);

    sqlSessionFactoryBean.setTypeAliasesPackage(
      mybatisProperties.getTypeAliasesPackage()
    );

    org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
    CoreConfiguration mybatisConfiguration = mybatisProperties.getConfiguration();
    if (mybatisConfiguration != null) {
      BeanUtils.copyProperties(mybatisConfiguration, configuration);
    }
    sqlSessionFactoryBean.setConfiguration(configuration);

    return sqlSessionFactoryBean.getObject();
  }

  @Primary
  @Bean(name = "databaseSqlSessionTemplate")
  public SqlSessionTemplate databaseSqlSessionTemplate(
    @Qualifier("databaseSqlSessionFactory") SqlSessionFactory sqlSessionFactory
  ) {
    return new SqlSessionTemplate(sqlSessionFactory);
  }
}
