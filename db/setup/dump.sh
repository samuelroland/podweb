## Help commands to dump a database
pg_dump -h "127.0.0.1" -U postgres -a -t public.* -T podcasts -T episodes -T migrations -T personal_access_tokens -f out.sql
pg_dump -h "127.0.0.1" -U postgres -a -t podweb.podcasts -t podweb.episodes -f podepi.sql

## Final dump to export all data related to podweb
pg_dump -h "127.0.0.1" -U postgres -a -t podweb.* -f final.sql
