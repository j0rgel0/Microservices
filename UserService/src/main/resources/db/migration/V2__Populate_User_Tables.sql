-- Flyway migration script
-- Version: 2
-- Description: Populate user and profile tables with initial data

-- Insert an administrator
INSERT INTO users (first_name, email, password, role)
VALUES ('Alice', 'alice@example.com', '$2a$10$74hKeGBPe2KPLn6ZrGzZEukaVFu2SqW27qo3ULxCy2zYh2XSIco8W', 'administrator')
    ON CONFLICT (email) DO NOTHING;

-- Add administrator profile
INSERT INTO administrator_profiles (user_id, department, permissions_level)
VALUES ((SELECT id FROM users WHERE email='alice@example.com'), 'IT', 'full')
    ON CONFLICT (user_id) DO NOTHING;

-- Insert a manager
INSERT INTO users (first_name, email, password, role)
VALUES ('Bob', 'bob@example.com', '$2a$10$I93MzLeDvK4lgrdhjpdHx.Bv6rhxwBZ3BQo9f5FPaJbLVau0S0UEC', 'manager')
    ON CONFLICT (email) DO NOTHING;

-- Add manager profile
INSERT INTO manager_profiles (user_id, team_size, area_of_responsibility)
VALUES ((SELECT id FROM users WHERE email='bob@example.com'), 10, 'Sales')
    ON CONFLICT (user_id) DO NOTHING;