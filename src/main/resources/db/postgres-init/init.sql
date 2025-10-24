CREATE DATABASE IF NOT EXISTS notes_db;

-- Change password
ALTER USER notesuser WITH PASSWORD 'notespass123';

-- Grant full database privileges
GRANT ALL PRIVILEGES ON DATABASE notes_db TO notesuser;

-- Grant privileges on existing tables
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO notesuser;

-- Grant privileges on future tables
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL PRIVILEGES ON TABLES TO notesuser;