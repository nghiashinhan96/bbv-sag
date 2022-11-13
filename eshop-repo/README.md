ESHOP-REPO
===========

This is the module contains all implementation with database as entities, tables, SQL scripts, database configuration,...

How to use
----------

You can pull it from Maven repositories or other child module:

```xml
<dependency>
  <groupId>com.sagag.eshop</groupId>
  <artifactId>eshop-repo</artifactId>
  <version>${project.version}</version>
</dependency>
```

Setup
----------

We setup new database configuration with the template:

##### File naming

```
appilication-db-<country-code>-<env-name>.yml
```

Example:
```
application-db-at-dev.yml
application-db-ch-umbpre.yml
```

##### Configuration

We have to setup some global configuration of database at YAML files.

| No | Config | Value | Description | Note |
| -- | ------ | ----- | ----------- | ---- |
| 01 | spring.datasource.type | com.zaxxer.hikari.HikariDataSource | ----------- | ---- |
| 02 | spring.datasource.hikari.driverClassName | com.microsoft.sqlserver.jdbc.SQLServerDriver | ----------- | ---- |
| 03 | spring.datasource.hikari.jdbcUrl | jdbc:sqlserver://<***ip-or-host-name***>:<***port-number***>;databaseName=<***database-name***>;applicationName=***ConnectRestServer***| ----------- | ---- |
| 04 | spring.datasource.hikari.username | [Wiki-Env] | ----------- | ---- |
| 05 | spring.datasource.hikari.password | [Wiki-Env] | ----------- | ---- |
| 06 | spring.datasource.hikari.idle-timeout | 10000 | ----------- | ---- |
| 07 | spring.datasource.hikari.minimum-idle | 1 | ----------- | ---- |
| 08 | spring.datasource.hikari.maximum-pool-size | 20 | ----------- | ---- |
| 09 | spring.datasource.hikari.pool-name | restatax_pool | ----------- | ---- |
| 10 | spring.datasource.hikari.connection-timeout | 10000 | ----------- | ---- |
| 11 | spring.datasource.hikari.max-lifetime | 30000 | ----------- | ---- |
| 12 | spring.datasource.hikari.register-mbeans | true | ----------- | ---- |
| 13 | spring.datasource.hikari.allow-pool-suspension | true | ----------- | ---- |
| 14 | spring.datasource.hikari.leak-detection-threshold | 30000 | ----------- | ---- |
| 15 | spring.jpa.show-sql | true | ----------- | ---- |
| 16 | spring.jpa.properties.hibernate.format_sql | true | ----------- | ---- |
| 17 | spring.jpa.properties.hibernate.dialect | org.hibernate.dialect.SQLServer2012Dialect | ----------- | ---- |
| 18 | spring.jpa.properties.hibernate.physical_naming_strategy | org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy | ----------- | ---- |
| 19 | spring.jpa.properties.hibernate.implicit_naming_strategy | org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy | ----------- | ---- |
| 20 | spring.jpa.hibernate.jdbc.batch_size | 100 | ----------- | ---- |
| 21 | spring.jpa.hibernate.order_inserts | true | ----------- | ---- |
| 22 | spring.hz.type | com.zaxxer.hikari.HikariDataSource | ----------- | ---- |
| 23 | spring.hz.hikari.driverClassName | com.microsoft.sqlserver.jdbc.SQLServerDriver | ----------- | ---- |
| 24 | spring.hz.hikari.jdbcUrl | jdbc:sqlserver://<***ip-or-host-name***>:<***port-number***>;databaseName=<***database-name***>;applicationName=***ConnectHazelcastBackup*** | ----------- | ---- |
| 25 | spring.hz.hikari.username | [Wiki-Env] | ----------- | ---- |
| 26 | spring.hz.hikari.password | [Wiki-Env] | ----------- | ---- |
| 27 | spring.hz.hikari.idle-timeout | 10000 | ----------- | ---- |
| 28 | spring.hz.hikari.minimum-idle | 1 | ----------- | ---- |
| 29 | spring.hz.hikari.maximum-pool-size | 20 | ----------- | ---- |
| 30 | spring.hz.hikari.pool-name | hzbackup_pool | ----------- | ---- |
| 31 | spring.hz.hikari.connection-timeout | 10000 | ----------- | ---- |
| 32 | spring.hz.hikari.max-lifetime | 30000 | ----------- | ---- |
| 33 | spring.hz.hikari.register-mbeans | true | ----------- | ---- |
| 34 | spring.hz.hikari.allow-pool-suspension | true | ----------- | ---- |
| 35 | spring.hz.hikari.leak-detection-threshold | 30000 | ----------- | ---- |

List of database:
----------

| No | Country Code | Profile | Database Name | Note |
| -- | :-----: | ------- | ----------- | ---- |
| 01 | at | db-at | axat | ---- |
| 02 | at | db-at-test | econnectAxIT | ---- |
| 03 | at | db-at-umbpre | econnectAxPPAT | ---- |
| 04 | at | db-at-umbprod | econnectAxP | ---- |
| 05 | autonet | db-autonet | autonet | ---- |
| 06 | autonet | db-autonet-test | econnectAxIT | ---- |
| 07 | autonet | db-autonet-umbpre | axat(temp) | ---- |
| 08 | autonet | db-autonet-umbprod | axat(temp) | ---- |
| 09 | ch | db-ch | axch | ---- |
| 10 | ch | db-ch-test | econnectAxIT | ---- |
| 11 | ch | db-ch-umbpre | econnectAxPCH_tmp | ---- |
| 12 | ch | db-ch-umbprod | econnectAxPCH | ---- |
| 13 | cz | db-cz | st-cz | ---- |
| 14 | cz | db-cz-pre | econnectSTPCZ | ---- |
| 15 | cz | db-cz-prod | econnectSTPCZ | ---- |

Liquibase:
----------


SQL Scripts:
----------


[//]: #

[Wiki]: https://tfs.bbv.ch/tfs/SAGCollection/Connect%20Plus/_wiki/wikis/Connect-Plus.wiki?
[Wiki-Env]: https://tfs.bbv.ch/tfs/SAGCollection/Connect%20Plus/_wiki/wikis/Connect-Plus.wiki?wikiVersion=GBwikiMaster&pagePath=%2FConnect%20Plus%20Home%20Page%2FDeployment%20Guide%2FSAG%20eShop%20Environment%20Version%202&pageId=44