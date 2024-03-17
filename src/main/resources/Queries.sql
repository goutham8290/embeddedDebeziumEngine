CREATE SCHEMA operations;


CREATE TABLE operations.product (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    price NUMERIC
);

CREATE USER operations_user WITH PASSWORD 'operations_pw';


GRANT  SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA operations TO operations_user;


COMMIT;

ALTER SYSTEM SET wal_level = 'logical';
SELECT pg_reload_conf();



CREATE ROLE debezium_user REPLICATION LOGIN;



ALTER role inventory_user WITH REPLICATION;

SELECT usename FROM pg_catalog.pg_user;


SHOW wal_level;

create publication dbz_publication for table debezium_tutorial.inventory.product ;

drop role if exists debezium_user;

CREATE ROLE debezium_user WITH LOGIN PASSWORD 'debezium_pw';


ALTER ROLE debezium_user WITH
    NOSUPERUSER
    NOCREATEDB
    NOCREATEROLE
    INHERIT
    LOGIN
    REPLICATION
    NOBYPASSRLS
    CONNECTION LIMIT -1 ;

  GRANT SELECT ON TABLE debezium_tutorial.inventory.product TO debezium_user;

 ALTER ROLE debezium_user WITH REPLICATION;
GRANT USAGE, CREATE ON SCHEMA inventory TO debezium_user;

select * from pg_catalog.pg_replication_slots;

CREATE EXTENSION decoderbufs;

