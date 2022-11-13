Assume that S1 is the source schema we need to move and merge all its data to the target schema (S2)

*Steps to migrate/merge data from S1 to S2*

# Export all latest S2 schema to XML changesets (use Liquibase)

- Download Liquibase package and ensure the sqljdbc42-1.0.jar connect library in the Liquibase class path
	
	`copy sqljdbc42-1.0.jar to ${Liquibase installation path}/lib`

- Run the command to export the current AXAT PROD to XML Liquibase changesets
	
	`liquibase 
	--driver=com.microsoft.sqlserver.jdbc.SQLServerDriver 
	--changeLogFile=changelog/ax.master-1.0.xml 
	--url="axprodurl" --username=umblocal --password="*********" 
	--logLevel=DEBUG --logFile="axat_prod_gen.log" generateChangeLog`

# Run the service to check the differences of S1 and S2 (test case service)
Run a test case of service DatabaseDifferenceExporterService to list out the difference of all tables and columns between S1 and S2

- the differences are just listed for just tables and columns name.
- no low checking the differences of column type and other column constraints.

Run the test case DatabaseDifferenceExportTest.java at corresponding profile: copydb,dev or copydb,pre ... 

# Update changesets in XML of step 1. with the differences in step 2. (Manually)
Maybe we would run to export the changelog of SAGSYS database to have the different changesets

	`liquibase 
	--driver=com.microsoft.sqlserver.jdbc.SQLServerDriver 
	--changeLogFile=changelog/sagsys.master-1.0.xml 
	--url="sagsysprodurl" --username=umblocal --password="*********" 
	--logLevel=DEBUG --logFile="sagsys_prod_gen.log" generateChangeLog`
# Start to create new schema (S3=S2 union S1) with updated XML of step 3. (use Liquibase)
See the testing on bbv DEV scripts.
We can do the same on the destination schema on target environment before we migrate

# Update the batch script for differences in step 2. (Manually)
Since the database changed overtime on live, this batch script should ONLY be valid at a time of migration. Therefore, this batch would be maintained before get it running. 
	
- Generate JPA mappings for S3 schema
Currently, we use EclipseLink 2.5 JPA generator to generate the entities mapping from the database table. However, due to the generated JPAs are not always good enough. Especially, there are needs to enhance the datatype mapping of Datetime and for nullable columns.

We have to enhance the JPA entities after the generation using Lombok annotations and some datatype mappings for Datetime and nullable columns.

In the future, it would be nice to have the automated generation script for the JPA when we have time.

- Generate batch script skeleton from JPA
Manually writing every batch job to process any table over a list of tables does usually take a lot of time. So, we try to automate this stuff by creating a service to generate the skeletons for a list of migrating tables.

A service SkeletonGeneratorService.java would implement the skeletons generation for all configured tables in the application.yml file

Basic idea of this service is to
- List all tables and its dependencies (direct and transitive dependent tables) to generate the **ordered**  job steps
- Spring JPA repositories generation
- Spring batch item process generation
- Spring batch item writer generation
- Jobs steps generation
- Drop all artefacts tables job generation - from Spring Batch execution
- Truncate all destination tables data job generation

# Test the batch script (Manually)
Command to execute the batch script to migrate data from source to destination schema - job CopyDatabase

	java -jar sag-copydb.jar --spring.profiles.active=copydb,dev name="CopyDb_18.10.2019_11.47" --spring.batch.job.names=CopyDatabase

The command must specify the active profiles (copydb is required) with 'dev' or 'pre' or 'prod' or 'testing'

Using pure SQL to increase the performance of the Job and the database logging. To use pure SQL batch script copying, start the command

	java -jar sag-copydb.jar --spring.profiles.active=copydb,dev name="CopyDb_18.10.2019_11.47" --spring.batch.job.names=CopyDatabaseUsingSQL  

When executing the spring batch jobs, those artefacts tables below are generated

- BATCH\_JOB\_EXECUTION
- BATCH\_JOB\_EXECUTION\_CONTEXT
- BATCH\_JOB\_EXECUTION\_PARAMS
- BATCH\_JOB\_EXECUTION\_SEQ
- BATCH\_JOB\_INSTANCE
- BATCH\_JOB\_SEQ
- BATCH\_STEP\_EXECUTION
- BATCH\_STEP\_EXECUTION\_CONTEXT
- BATCH\_STEP\_EXECUTION\_SEQ`

Hints: sometimes, we need to delete those batch tables, it could be done possibly by using below command

	java -jar sag-copydb.jar --spring.profiles.active=copydb,dev name="CopyDb_18.10.2019_11.47" --spring.batch.job.names=DropBatchTables
	
To clean (delete) all records in the destination migrated tables, we could use	

	java -jar sag-copydb.jar --spring.profiles.active=copydb,dev name="CopyDb_18.10.2019_11.47" --spring.batch.job.names=TruncateAllDestTables
	
In SAG Preprod, we use the testing server (e.g. IP 10.1.150.40) to execute the jar service.

- Source schema: url = sag-ppd-cdb-021.sag.services;databaseName=**econnectCHT** 
- Destination schema: url = sag-ppd-cdb-021.sag.services;databaseName=**econnectATPP** 
	
# Start to run the batch script (data: S1 -> S3) (use created jar service)
Build the **sag-copydb**  module to have jar service file. Copy this jar file to the strong server to execute the migration jobs.

	Target environments can be executed (dev, test, pre, prod) 

	
# Testing on bbv DEV
We tried to test the batch script on bbv DEV MSSQL server by creating 2 database schemas: **axprod**  and **sagsysprod** .

- Run the Liquibase XML changesets S3 on test database [axprod]

	Validate changelog
		`liquibase 
		--driver=com.microsoft.sqlserver.jdbc.SQLServerDriver 
		--changeLogFile=changelog/merged.master.xml 
		--url="jdbc:sqlserver://148.251.19.36:9254;databaseName=axprod" --username=sag --password="*********" 
		--logLevel=DEBUG --logFile="axprod_validate.log" validate`

	Update database with schema
		`liquibase 
		--driver=com.microsoft.sqlserver.jdbc.SQLServerDriver 
		--changeLogFile=changelog/merged.master.xml 
		--url="jdbc:sqlserver://148.251.19.36:9254;databaseName=axprod" --username=sag --password="*********" 
		--logLevel=DEBUG --logFile="axprod_tag.log" tagExists "axprod_v1" tag "axprod_v1"`
	
		`liquibase 
		--driver=com.microsoft.sqlserver.jdbc.SQLServerDriver 
		--changeLogFile=changelog/merged.master.xml 
		--url="jdbc:sqlserver://148.251.19.36:9254;databaseName=axprod" --username=sag --password="*********" 
		--logLevel=DEBUG --logFile="axprod_release.log" update`

- Run the Liquibase XML changesets S1 on test database [sagsysprod]

	Validate changelog
	
		`liquibase 
		--driver=com.microsoft.sqlserver.jdbc.SQLServerDriver 
		--changeLogFile=changelog/sagsys.master-1.0.xml 
		--url="jdbc:sqlserver://148.251.19.36:9254;databaseName=sagsysprod" --username=sag --password="*********" 
		--logLevel=DEBUG --logFile="sagsysprod_validate.log" validate`
	
	Update database with schema
	
		`liquibase --driver=com.microsoft.sqlserver.jdbc.SQLServerDriver 
		--changeLogFile=changelog/sagsys.master-1.0.xml 
		--url="jdbc:sqlserver://148.251.19.36:9254;databaseName=sagsysprod" --username=sag --password="*********" 
		--logLevel=DEBUG --logFile="sagsysprod_tag.log" tagExists "sagsysprod_v1" tag "sagsysprod_v1"`
	
		`liquibase --driver=com.microsoft.sqlserver.jdbc.SQLServerDriver 
		--changeLogFile=changelog/sagsys.master-1.0.xml 
		--url="jdbc:sqlserver://148.251.19.36:9254;databaseName=sagsysprod" --username=sag --password="*********" 
		--logLevel=DEBUG --logFile="sagsysprod_release.log" update`

- Re-run the batch script

	In order to re-run the batch script, we need to drop all objects in the database.
	
		`liquibase 
		--driver=com.microsoft.sqlserver.jdbc.SQLServerDriver 
		--changeLogFile=changelog/merged.master.xml  
		--url="jdbc:sqlserver://148.251.19.36:9254;databaseName=axprod" --username=sag --password="*********" 
		--logLevel=DEBUG --logFile="axprod_dropAll.log" dropAll`


