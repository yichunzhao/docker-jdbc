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
-p, --publish list                   Publish a container's port(s) to  the host
-e, --env list                       Set environment variables
-d, --detach                         Run container in background and print container ID
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

Display [PSQL](https://www.postgresql.org/docs/13/app-psql.html) Commands: You can append + to show more details.

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
psql -h localhost -p Port -U username -d databasename
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
However, the container has a virtual file system that cannot persist data. When the container is removed or restarted, 
the data is gone. So in order to persist data, the container needs to plug in a physical folder located at the host, 
by this way it may persist the data even after the container having been removed or restarted.

> Docker volumes on Windows are always created in the path of the graph driver, which is where Docker stores all image 
> layers, writeable container layers and volumes. By default 
> the root of the graph driver in Windows is C:\ProgramData\docker, but you can mount a volume to a specific directory when you run a container.
>

There are three ways to mount container volumes, a) anonymous volume, b) mounting a specific host file folder, c) named volume

a) When having no volume specified, the PostgreSQL docker container pick up a random folder at the host to store data.
   
````
docker run --name myPostgres -p 5435:5432 -e POSTGRES_PASSWORD=test -v /var/lib/postgresql/data postgres:latest
````

b) You may mount a specific host file folder on a PostgreSQL container, and pointing it to PostgreSQL default data folder: 
/var/lib/postgresql/data. In this way, we may persist the container data on the host file system. 

````
docker run --name myPostgres -p 5435:5432 -e POSTGRES_PASSWORD=test -v ${pwd}:/var/lib/postgresql/data postgres:latest
````
c) Giving an anonymous volume a specific name, then achieving a named volume. 
The named volume is mostly used in production. It leaves a host to manage volumes, meanwhile allows to share volumes among containers. 

````
docker run --name myPostgres -p 5435:5432 -e POSTGRES_PASSWORD=test -v postgresVolume:/var/lib/postgresql/data -d postgres:latest
````

**Backup Database**

A named volume is mostly used in a proudction env., as as to leave host to manage volumes. Using a shared named-volume, 
we may present database in a the new container instance. 

![image](https://user-images.githubusercontent.com/17804600/121562678-f9ddfc00-ca19-11eb-92e5-0e580c03a887.png)

create two tables in the myPostgres

![image](https://user-images.githubusercontent.com/17804600/121575672-ec2f7300-ca27-11eb-9014-a2eab0cb9a3f.png)

create another postgreSql container instance, and mounting with the well-known named-volume; so we have two containers sharing the same volume. 

````
docker run --name anotherPostgres -p 5436:5432 -e POSTGRES_PASSWORD=test -v postgresVolume:/var/lib/postgresql/data -d postgres:latest
````

![image](https://user-images.githubusercontent.com/17804600/121568869-7673d900-ca20-11eb-8683-684a5efd4d25.png)

Create a new container PostgreSql container instance, and mounting this named volume. 

![image](https://user-images.githubusercontent.com/17804600/121569787-72948680-ca21-11eb-8ece-40628a1a3d1a.png)

Login the newly created database, and presenting two previously created tables.

![image](https://user-images.githubusercontent.com/17804600/121575220-68758680-ca27-11eb-97a5-30624c33fac3.png)


**Running PostgreSQL init script**

Step inside the PostgreSQL container, and list folders. At the root folder, 
there is a folder /docker-entrypoint-initdb.d, where it allows to do additional initialization in an image derived 
from the PostgreSql image, and add one or more *.sql, *.sql.gz, or *.sh scripts. After the entrypoint calls 
initdb to create the default postgres user and database, it will run any *.sql files, run any executable *.sh scripts, 
and source any non-executable *.sh scripts found in that directory to do further initialization before starting the service.  

![image](https://user-images.githubusercontent.com/17804600/120797061-d7d90b00-c53b-11eb-84a4-fe9b4f20f47b.png)

**Populating DB**

how to populate data in an existing PostgreSql container?


##Tips Of JDBC

* Method executeUpdate() returns a count of the rows that are or would be affected in the database for row insertions, modifications, and deletion. The value is returned even if the statement isn’t committed. This method returns 0 for SQL DDL statements, which create database objects and modify their structure or delete them. 
* If your SQL statement doesn’t return any rows, the ResultSet object doesn’t point to a null value. In this case, you’ll get a ResultSet object that won’t include any rows.
* Although everything in Java is 0-based, column indexes in a ResultSet are 1-based.
* Method executeQuery() is used for SQL SELECT statements.
* For all SQL operations on a database, the preferred programming approach is to close the Connection and Statement objects. You must either close them explicitly by calling close() on them or use them with a try-with-resources statement, which auto-closes them. 
* Note that you need to call method executeUpdate() on Statement and not executeQuery() to execute a Data Definition Language (DDL) request.
* There are two ways to connect to a database: by using class java.sql.DriverManager or the interface javax.sql.DataSource. Class DriverManager was included in the JDBC API since its beginning and the interface DataSource was added later in JDBC version 2.0. Class DriverManager is the preferred class to establish database connections with Java SE applications because DataSource works with the Java Naming and Directory Interface (JNDI). JNDI is usually supported by Java applications with a container that supports JNDI like Java Enterprise Edition Server.
* The JDBC URL determines the appropriate driver for a given URL string. For example jdbc:subprotocol:\//<host>:<port>\/<database_name>
* JDBC 4.0 and its later versions support automatic loading and registration of all JDBC drivers accessible via an application’s class path. You no longer need to explicitly
load the driver in memory using Class.forName(). 
* A JDBC driver is a set of classes provided by the database vendor, or a third party, usually in a .jar or .zip file, to support the JDBC API. A Driver class is an
implementation of the interface java.sql.Driver in a JDBC driver. For example, for MySQL, its platform-independent JDBC driver can be downloaded as mysql-connector-java-5.1.27.zip. The name of the class that implements java.sql.Driver in MySQL Connector\/J (JDBC driver) is com.mysql.jdbc.Driver.  
  
  
  
