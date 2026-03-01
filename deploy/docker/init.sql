SELECT 'CREATE DATABASE order'
    WHERE NOT EXISTS (
    SELECT FROM pg_database WHERE datname = 'order'
)\gexec