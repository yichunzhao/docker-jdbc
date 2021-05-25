# docker-jdbc

init. a Postgres container, mapping port to 5435

````
docker run --name myPostgres -p 5435:5432 -e POSTGRES_PASSWORD=test -d postgres:latest
````

Setup Gui pgAdmin 

new server

