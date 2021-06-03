# docker-jdbc

init. a Postgres container, mapping container internal port 5432(Postgres default exposure port) to the local system at the port 5435

````
docker run --name myPostgres -p 5435:5432 -e POSTGRES_PASSWORD=test -d postgres:latest
````

![image](https://user-images.githubusercontent.com/17804600/119436141-efceb480-bd1b-11eb-9158-ac7b703e02b0.png)


**Connecting to PostgreSql via Gui pgAdmin** 

Linking the Postgres container to a new server; it allows operations on the Database instead of command lines. 

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


detach mode: This command starts the container, prints its id, and then returns to the shell prompt. Thus, we can continue with other tasks while the container continues to run in the background. We can connect to this container later using either its name or container id.

**Connecting to PostgreSql container via Exec**

The Exec command allows the client go inside a running container, and execute commands

````
docker exec -it myPostgres bash

docker command: 

exec        Run a command in a running container

docker exec --help

Usage:  docker exec [OPTIONS] CONTAINER COMMAND [ARG...]

Run a command in a running container

Options:
  -d, --detach               Detached mode: run command in the background
      --detach-keys string   Override the key sequence for detaching a
                             container
  -e, --env list             Set environment variables
      --env-file list        Read in a file of environment variables
  -i, --interactive          Keep STDIN open even if not attached
      --privileged           Give extended privileges to the command
  -t, --tty                  Allocate a pseudo-TTY
  -u, --user string          Username or UID (format:
                             <name|uid>[:<group|gid>])
  -w, --workdir string       Working directory inside the container

````

![image](https://user-images.githubusercontent.com/17804600/119606115-3d1d5580-bdf2-11eb-95d3-ad2960724899.png)


**Connecting to PostgreSql container via SQL shell(psql)** 

![image](https://user-images.githubusercontent.com/17804600/119606214-71911180-bdf2-11eb-87a6-08342c16b089.png)

Display Commands: You can append + to show more details.

* \\l: List all database (or \list).
* \\d: Display all tables, indexes, views, and sequences.
* \\dt: Display all tables.
* \\di: Display all indexes.
* \\dv: Display all views.
* \\ds: Display all sequences.
* \\dT: Display all types.
* \\dS: Display all system tables.
* \\du: Display all users.

* \\?: show all psql commands.
* \\c dbname [username]: Connect to database, with an optional username (or \connect).
* \\h sql-cmd: show syntax on sql command

**Connecting to PostgreSql container via Psql**

````
psql -h localhost -U username databasename
````

![image](https://user-images.githubusercontent.com/17804600/120614338-6889eb00-c457-11eb-8cc1-c067ef5fd91b.png)



**List containers**

list all running containers

````
docker ps  
````
docker ps -a : list all containers no matter which status

The filtering flag (-f or --filter) format is a key=value pair. If there is more than one filter, then pass multiple flags (e.g. --filter "foo=bar" --filter "bif=baz")

docker ps -f key=value: list out running containers which match the given key-value.
docker ps -f key=value -a: list out all containers which match the given key-value.

![image](https://user-images.githubusercontent.com/17804600/120453941-bf2df100-c393-11eb-8b57-38f88acb9bdd.png)


**Restart an existing container**

![image](https://user-images.githubusercontent.com/17804600/120457779-049fed80-c397-11eb-85f4-0b3c20cbbe76.png)

````
docker ps
````

![image](https://user-images.githubusercontent.com/17804600/120463011-c1944900-c39b-11eb-9364-2a23f0dd6c9b.png)


