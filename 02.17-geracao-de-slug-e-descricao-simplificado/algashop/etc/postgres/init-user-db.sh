#!/bin/bash
set -e
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
	CREATE DATABASE fastpay;
	CREATE DATABASE ordering;
	CREATE DATABASE billing;
	CREATE DATABASE ordering_test;
	CREATE DATABASE billing_test;
EOSQL