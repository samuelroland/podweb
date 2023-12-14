#!/bin/bash
export PGPASSWORD=postgres
files=(podweb-schema.sql podweb-additions.sql podweb-podcasts-population.sql podweb-episodes-population.sql)
set -e
for file in "${files[@]}"; do
    echo "Launching $file:"
	psql -h "127.0.0.1" -U postgres -a -f $file
done

echo "Seems to be all good !"