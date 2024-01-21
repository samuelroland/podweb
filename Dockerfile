FROM bitnami/postgresql:16
WORKDIR /repos/db/setup 

CMD bash db.sh