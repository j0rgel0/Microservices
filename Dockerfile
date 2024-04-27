# Use the latest official PostgreSQL image as the base image
FROM postgres:latest

# Copy the initialization script into the container
COPY init-db.sh /docker-entrypoint-initdb.d/

# Expose the port PostgreSQL runs on
EXPOSE 5432