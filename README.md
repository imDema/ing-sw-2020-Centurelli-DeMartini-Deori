# Prova Finale Ingegneria del Software 2020
# Santorini - Game

## Gruppo AM08


- ###   10576649    Emilio Centurelli ([@emiliocenturelli](https://github.com/emiliocenturelli))<br>emilio.centurelli@mail.polimi.it
- ###   10565376    Luca De Martini ([@luca-de-martini](https://github.com/luca-de-martini))<br>luca.demartini@mail.polimi.it
- ###   10572912    Nicolò Deori ([@NicoloDeori](https://github.com/NicoloDeori))<br>nicolo.deori@mail.polimi.it

| Functionality | State |
|:-----------------------|:------------------------------------:|
| Basic rules | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Complete rules | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Socket | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| GUI | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| CLI | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Multiple games | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Persistence | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |
| Advanced Gods | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Undo | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |

<!--
[![RED](https://placehold.it/15/f03c15/f03c15)](#)
[![YELLOW](https://placehold.it/15/ffdd00/ffdd00)](#)
[![GREEN](https://placehold.it/15/44bb44/44bb44)](#)
-->

# Usage

### Client

##### Graphical interface

The graphical interface can be launched with

```shell script
java -jar AM8.jar
```

##### Command line interface

The command line client can be started by launching the jar with the `-c`
flag and specifying the ip and port of the server to connect to. Example 
(Connecting to a server hosted locally on loopback and port 5656):

```shell script
java -jar AM8.jar -c 127.0.0.1 5656
```

### Server

The server can be started by launching the jar with the `-s` flag and 
specifying the ip and port to bind and listen for connection from. Example
 (Binding on all interfaces and port 5656):
 
 ```shell script
java -jar AM8.jar -s 0.0.0.0 5656
```

##### Using docker-compose

The server can be built and started in a docker container with a single 
command using `docker-compose` from the main repository directory

```shell script
docker-compose up
```

This will start a containerized server instance listening on all 
interfaces and port 5656

### Command line reference

```shell script
Usage: java -jar AM8.jar [--cli|--server <IP> <PORT>] [--gods <JSON>]

Launching in server or cli mode requires specifying an ip and and a port
By default the application is started in graphical mode

-c, --cli:         Launch the application in client mode with a Command Line Interface, connect to server at tcp://IP:PORT
-s, --server:      Start in server mode binding on tcp://IP:PORT
-g, --gods JSON:   Load god configuration from JSON file (server side)
-h, --help:        Display this help message
Example: java -jar AM8-1.0-SNAPSHOT.jar -c 127.0.0.1 5656
```

# Building

The project can be built using [maven](https://maven.apache.org/)

```shell script
mvn package
```

The jar will be in the `target/` directory with name `AM8-1.0-jar-with-dependencies.jar`