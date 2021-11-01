#!/bin/bash
 # start SQL Server + prevent auto-shutdown: https://stackoverflow.com/questions/51039052/sql-server-docker-container-is-stopping-after-setup
sh -c "
# wait for the SQL Server to come up
echo 'Sleeping 30 seconds before running setup script'
sleep 30s

# run the setup script to create the DB and the schema in the DB
/opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P Pass@word -d master -i setup.sql
" &
exec /opt/mssql/bin/sqlservr
