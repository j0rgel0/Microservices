-- Flyway migration script
-- Version: 1
-- Description: Create user and profile tables

-- Enable UUID-OSSP extension for uuid_generate_v4() function
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Basic users
CREATE TABLE users (
                       id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                       first_name VARCHAR(50) NOT NULL,
                       last_name VARCHAR(50),
                       email VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(100) NOT NULL,
                       creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       last_update TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       soft_delete BOOLEAN NOT NULL DEFAULT FALSE,
                       user_type VARCHAR(15) NOT NULL  -- 'administrator' or 'manager'
);

-- Administrator profile
CREATE TABLE administrator_profiles (
                       user_id UUID PRIMARY KEY,
                       department VARCHAR(50),  -- Department or area of responsibility
                       permissions_level VARCHAR(20),  -- Permission level, e.g., 'full', 'restricted'
                       FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Manager profile
CREATE TABLE manager_profiles (
                       user_id UUID PRIMARY KEY,
                       team_size INT,  -- Size of the team they manage
                       area_of_responsibility VARCHAR(255),  -- Specific area of responsibility
                       FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
