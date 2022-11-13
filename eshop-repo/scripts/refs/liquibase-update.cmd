-- Download Liquibase version liquibase-3.5.5-bin, at https://github.com/liquibase/liquibase/releases/download/liquibase-parent-3.5.5/liquibase-3.5.5-bin.zip
-- extract the zip file, and cd to ../liquibase-3.5.5-bin/
-- download and copy 2 libraries as required for executing liquibase command line to liquibase\lib folder: slf4j-api-1.7.25 and sqljdbc42-1.0

-- Liquibase command line
https://www.liquibase.org/documentation/command_line.html

-- to generate the current schema to xml liquibase file
liquibase --driver=com.microsoft.sqlserver.jdbc.SQLServerDriver --changeLogFile=D:/projects/sag/atax/sag-connect-ax/eshop-repo/src/main/resources/db/dev/db.changelog-1.0.xml --url="jdbc:sqlserver://148.251.19.36:9254;databaseName=ax" --username=sag --password="eU24|%569k(v}yJ" --logLevel=DEBUG --logFile="db_gen.log" generateChangeLog

-- validate changelog
liquibase --driver=com.microsoft.sqlserver.jdbc.SQLServerDriver --changeLogFile=D:/projects/sag/atax/sag-connect-ax/eshop-repo/src/main/resources/db/changelog/db.changelog-iter-074.xml --url="jdbc:sqlserver://148.251.19.36:9254;databaseName=axch" --username=sag --password="eU24|%569k(v}yJ" --logLevel=DEBUG --logFile="db_validate.log" validate

-- for example, to execute the update changes for database of iteration 074: at src/main/resources/db/changelog/db.changelog-iter-074.xml, on DB DEV CH 'axch'
-- to tag the database before running update
liquibase --driver=com.microsoft.sqlserver.jdbc.SQLServerDriver --changeLogFile=D:/projects/sag/atax/sag-connect-ax/eshop-repo/src/main/resources/db/changelog/db.changelog-iter-074.xml --url="jdbc:sqlserver://148.251.19.36:9254;databaseName=axch" --username=sag --password="eU24|%569k(v}yJ" --logLevel=DEBUG --logFile="db_tag.log" tagExists "ATCH_V1_0" tag "ATCH_V1_0"
liquibase --driver=com.microsoft.sqlserver.jdbc.SQLServerDriver --changeLogFile=D:/projects/sag/atax/sag-connect-ax/eshop-repo/src/main/resources/db/changelog/db.changelog-iter-074.xml --url="jdbc:sqlserver://148.251.19.36:9254;databaseName=axch" --username=sag --password="eU24|%569k(v}yJ" --logLevel=DEBUG --logFile="db_release.log" update
 
-- rollback by datetime
liquibase --driver=com.microsoft.sqlserver.jdbc.SQLServerDriver --changeLogFile=D:/projects/sag/atax/sag-connect-ax/eshop-repo/src/main/resources/db/changelog/db.changelog-iter-074.xml --url="jdbc:sqlserver://148.251.19.36:9254;databaseName=axch" --username=sag --password="eU24|%569k(v}yJ" --logLevel=DEBUG --logFile="db_rollback.log" rollbackToDate "2018-12-11 17:10:32.430"

-- rollback by tag
liquibase --driver=com.microsoft.sqlserver.jdbc.SQLServerDriver --changeLogFile=D:/projects/sag/atax/sag-connect-ax/eshop-repo/src/main/resources/db/changelog/db.changelog-iter-074.xml --url="jdbc:sqlserver://148.251.19.36:9254;databaseName=axch" --username=sag --password="eU24|%569k(v}yJ" --logLevel=DEBUG --logFile="db_rollback.log" tagExists "ATCH_V1_0" rollback "ATCH_V1_0"

-- should tag, label the changeset for maintainability

-- Drop all objects
liquibase --driver=com.microsoft.sqlserver.jdbc.SQLServerDriver --changeLogFile=D:/projects/sag/atax/sag-connect-ax/eshop-repo/src/main/resources/db/changelog/db.changelog-iter-074.xml --url="jdbc:sqlserver://148.251.19.36:9254;databaseName=axch" --username=sag --password="eU24|%569k(v}yJ" --logLevel=DEBUG --logFile="db_dropAll.log" dropAll


