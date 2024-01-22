#!/bin/bash
echo "Podweb database setup script"

# Choose your user that can create the db podweb
dotenv="../../podweb/.env"
if ! test -f $dotenv; then
    echo "The file .env should be set inside $dotenv to indicate DB credentials. Run this again after you created this file."
    exit 1;
fi

source "$dotenv"
export PGPASSWORD=$DB_USER
export PGUSER=$DB_PWD

files=(podweb-schema.sql podweb-data.sql podweb-additions.sql)

for file in "${files[@]}"; do
    echo "
--- Running $file..."
	psql -d "$DB_NAME" -h "$DB_HOST" -U "$PGUSER" -a -f $file -v ON_ERROR_STOP=1 | grep ERROR
done

echo "
--- Fixing last inserted IDs"
## Set last inserted id to the max one because Postgres doesn't update it when doing insert into with given IDs...
## It was causing failures of duplicated primary keys values on insert
for table in `grep -Po "(?<=CREATE TABLE).*(?= \()" podweb-schema.sql`; do
    echo "Trying to update last id of table $table to max id..."
    query=("set search_path=podweb; SELECT setval('${table}_id_seq', (SELECT MAX(id) FROM ${table}));")

    # Note: it will throw errors on tables without ID but we can ignore them
    psql -d "$DB_NAME" -h "$DB_HOST" -U "$PGUSER" -a -c "$query" -v ON_ERROR_STOP=1 &> /dev/null
done

echo "
--- Setting all users pwd to 'pass'"
psql -d "$DB_NAME" -h "$DB_HOST" -U "$PGUSER" -a -c "set search_path=podweb; update users set password = 'pass';" -v ON_ERROR_STOP=1

echo "
--- Setting BabyListener badge condition to 3"
psql -d "$DB_NAME" -h "$DB_HOST" -U "$PGUSER" -a -c "set search_path=podweb; update badges set condition_value = 3 where id = 1;" -v ON_ERROR_STOP=1


echo "
Seems to be all good !"