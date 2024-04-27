#!/bin/bash
# Stop the script on any errors
set -e

# Environment variables POSTGRES_USER, POSTGRES_PASSWORD, and POSTGRES_DB should be set in the Docker container environment

# Run SQL commands to create user and database
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE USER $POSTGRES_USER WITH PASSWORD '${POSTGRES_PASSWORD}';
    CREATE DATABASE $POSTGRES_DB;
    GRANT ALL PRIVILEGES ON DATABASE $POSTGRES_DB TO $POSTGRES_USER;
EOSQL