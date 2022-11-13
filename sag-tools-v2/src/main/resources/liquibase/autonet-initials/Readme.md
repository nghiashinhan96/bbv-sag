### Purpose
Initials all the tables, constraint, view, ... schema of autonet DB

The schema is loaded from Database dev-ch.
### Software need to executed this job
1. [liquibase](https://www.liquibase.org/ "liquibase")
2. sqljdbc42-1.0.jar (from ..\eshop-repo\lib\)

### Step by step
-  install liquibase and check if success by comment `liquibase`
Expected result if install liquibase correctly:

```
Starting Liquibase at Thu, 16 Jan 2020 10:54:58 ICT (version 3.8.5 #42 built at Thu Jan 09 04:58:55 UTC 2020)


Usage: java -jar liquibase.jar [options] [command]

Standard Commands:
 update                         Updates database to current version
 updateSQL                      Writes SQL to update database to current
                                version to STDOUT
 updateCount <num>              Applies next NUM changes to the database
 updateCountSQL <num>           Writes SQL to apply next NUM changes
                                to the database
....
```
- Check and config file `liquibase.properties` to the correct DB url, ...
- Keep 3 files `sqljdbc42-1.0.jar`, `liquibase.properties` and `db-changelog-initial-autonet.xml` in the same directory.
- Execute command `liquibase update`
