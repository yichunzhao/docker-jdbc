# docker-jdbc

init. a Postgres container, mapping port to 5435

````
docker run --name myPostgres -p 5435:5432 -e POSTGRES_PASSWORD=test -d postgres:latest
````

Setup Gui pgAdmin 

Linking the Postgres container to a new server

![image](https://user-images.githubusercontent.com/17804600/119435564-a7fb5d80-bd1a-11eb-924d-bb90ae279ec2.png)

