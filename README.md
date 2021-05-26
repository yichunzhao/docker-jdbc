# docker-jdbc

init. a Postgres container, mapping container internal port 5432 to the local system at the port 5435

````
docker run --name myPostgres -p 5435:5432 -e POSTGRES_PASSWORD=test -d postgres:latest
````

![image](https://user-images.githubusercontent.com/17804600/119436141-efceb480-bd1b-11eb-9158-ac7b703e02b0.png)


Setup Gui pgAdmin 

Linking the Postgres container to a new server

![image](https://user-images.githubusercontent.com/17804600/119435564-a7fb5d80-bd1a-11eb-924d-bb90ae279ec2.png)

docker run [options]

docker run --help

````
-p, --publish list                   Publish a container's port(s) to
                                       the host
-e, --env list                       Set environment variables

-d, --detach                         Run container in background and
                                       print container ID
````

![image](https://user-images.githubusercontent.com/17804600/119532437-2a206c00-bd85-11eb-8a96-e25d81c23902.png)

Connecting to PostgreSql container via docker Exec 

![image](https://user-images.githubusercontent.com/17804600/119606115-3d1d5580-bdf2-11eb-95d3-ad2960724899.png)

Connecting to PostgreSql container via SQL shell(psql) 

![image](https://user-images.githubusercontent.com/17804600/119606214-71911180-bdf2-11eb-87a6-08342c16b089.png)


