# docker-jdbc

init. a Postgres container, mapping port to 5435

````
docker run --name myPostgres -p 5435:5432 -e POSTGRES_PASSWORD=test -d postgres:latest
````

![image](https://user-images.githubusercontent.com/17804600/119436141-efceb480-bd1b-11eb-9158-ac7b703e02b0.png)


Setup Gui pgAdmin 

Linking the Postgres container to a new server

![image](https://user-images.githubusercontent.com/17804600/119435564-a7fb5d80-bd1a-11eb-924d-bb90ae279ec2.png)

