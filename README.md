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

**Connecting to PostgreSQL container via Psql**

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

[current supported filtering key-value](https://docs.docker.com/engine/reference/commandline/ps/)

![image](https://user-images.githubusercontent.com/17804600/120453941-bf2df100-c393-11eb-8b57-38f88acb9bdd.png)

**Restart an existing container**

![image](https://user-images.githubusercontent.com/17804600/120457779-049fed80-c397-11eb-85f4-0b3c20cbbe76.png)

````
docker ps
````

![image](https://user-images.githubusercontent.com/17804600/120463011-c1944900-c39b-11eb-9364-2a23f0dd6c9b.png)

**Docker volumes**

We may need to back up current PostgreSQL database data and bring them back as creating a new PostgresSQL container instance.
However, the container cannot persist data, it has to reply on to plug into a folder located at the host,
by this way the container then is able to persist the data at the host file system.

> Docker volumes on Windows are always created in the path of the graph driver, which is where Docker stores all image layers, writeable container layers and volumes. By default 
> the root of the graph driver in Windows is C:\ProgramData\docker, but you can mount a volume to a specific directory when you run a container.
>

There are three ways to mount container volumes, a) default volume, b) mounting a specific host file folder, c) named volume

a) By default, if having no volume specified, the PostgreSQL docker container pick up a random folder at the host to store data.   

![image](https://user-images.githubusercontent.com/17804600/120910448-6609da00-c67f-11eb-88dd-694212c6367a.png)


b) You may mount a specific host folder on a PostgreSQL container, pointing it to PostgreSQL folder: 
``/var/lib/postgresql/data´´. In this way, we have the database data synchronised 
via the volume, and we may backup via the shared volume.

````
docker run --name myPostgres -p 5435:5432 -e POSTGRES_PASSWORD=test -v ${pwd}:/var/lib/postgresql/data postgres:latest
````

Console print out: 

![image](https://user-images.githubusercontent.com/17804600/120926839-c845f780-c6de-11eb-9c3b-a9fb3347091b.png)

c) Named volume is a way between default volume and specific volume, by offering a specific name to a default volume.

![image](https://user-images.githubusercontent.com/17804600/121440055-6cea6280-c987-11eb-9774-6c6d58b94048.png)


**Running PostgreSQL init script**

Step inside the PostgreSQL container, and list folders. At the root folder, there is a folder /docker-entrypoint-initdb.d, where it allows to do additional initialization in an image derived from the PostgreSql image, and add one or more *.sql, *.sql.gz, or *.sh scripts. After the entrypoint calls initdb to create the default postgres user and database, it will run any *.sql files, run any executable *.sh scripts, and source any non-executable *.sh scripts found in that directory to do further initialization before starting the service.  

![image](https://user-images.githubusercontent.com/17804600/120797061-d7d90b00-c53b-11eb-84a4-fe9b4f20f47b.png)


**Populating DB**

how to populate data in an existing PostgreSql container?
