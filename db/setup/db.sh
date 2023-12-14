#!/bin/bash
echo "Podweb database setup script"

# Choose your user that can create the db podweb
export PGPASSWORD=postgres
export PGUSER=postgres

files=(podweb-schema.sql podweb-data.sql podweb-additions.sql)

for file in "${files[@]}"; do
    echo "
--- Running $file..."
	psql -h "127.0.0.1" -U "$PGUSER" -a -f $file -v ON_ERROR_STOP=1 | grep ERROR
done

echo "
Seems to be all good !"