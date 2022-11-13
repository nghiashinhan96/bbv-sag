# 0. PREPARE THE CONNECTIVITY TO MSSQL server for ESHOP
# 1. copy the jar file to the directory in server
# 2. cd to the directory, run the following command
mvn install:install-file -Dfile=sqljdbc42-1.0.jar -DgroupId=com.microsoft.sqlserver -DartifactId=sqljdbc42 -Dversion=1.0 -Dpackaging=jar