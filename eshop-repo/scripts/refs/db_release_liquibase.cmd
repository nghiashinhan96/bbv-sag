When you want to release the database using Liquibase, You should follow strictly those below steps.
REMEMBER:
	- please don't run script by hand due to:
		* to avoid database change's trackings from developers.
		* to benefit the features from Liquibase such as: Rollback, validate, changelogs tracking.

1. Tag your previous version BEFORE running database changes/updates
liquibase --driver=com.microsoft.sqlserver.jdbc.SQLServerDriver --changeLogFile=db.changelog-iter-0xx.xml --url="jdbc:sqlserver://148.251.19.36:9254;databaseName=axch" --username=sag --password="eU24|%569k(v}yJ" --logLevel=DEBUG --logFile="db_tag.log" tagExists "Release_VX" tag "Release_VX"
2. Execute database changes/update release
liquibase --driver=com.microsoft.sqlserver.jdbc.SQLServerDriver --changeLogFile=db.changelog-iter-0xx.xml --url="jdbc:sqlserver://148.251.19.36:9254;databaseName=axch" --username=sag --password="eU24|%569k(v}yJ" --logLevel=DEBUG --logFile="db_release.log" update

In case rollback, if there's anything wrong in previous steps
3. Rollback the changes/update to previous Tags
liquibase --driver=com.microsoft.sqlserver.jdbc.SQLServerDriver --changeLogFile=db.changelog-iter-0xx.xml --url="jdbc:sqlserver://148.251.19.36:9254;databaseName=axch" --username=sag --password="eU24|%569k(v}yJ" --logLevel=DEBUG --logFile="db_rollback.log" tagExists "Release_VX" rollback "Release_VX"