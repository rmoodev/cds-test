# Introduction

This is Ryan's CDS coding assessment project.

## Swagger
- [Swagger documentation](http://localhost:9002/swagger-ui.html) (local).
- [Swagger documentation](http://192.168.99.100:9002/swagger-ui.html) (docker).

##Build & Run

####Prerequisites & Assumptions

Docker is installed.

Gradle is installed.

A Docker daemon is running. To start an existing docker-machine, run:

    docker-machine start default

I also assume that your docker machine is setup to run on <b>192.168.99.100</b>. If it's not - you can find your machine ip by listing your machines:
        
    docker-machine ls
   
####Startup - Local

The best way to line-debug this project is via a <b>run configuration in IntelliJ</b>. But - this project is configured just like any other Spring-Boot/gradle project. It can be started via:

    gradle bootRun

####Startup - Docker

Run your gradle build.

    gradle build
    
Kill whatever is already running. (optional)

    docker-compose kill
    
Blow away current containers. (May not be needed - but whatever) (optional)

     docker rm `docker ps --no-trunc -aq`
     
Build docker containers described in compose file.

    docker-compose build;
    
Upload/update them in Docker machine if needed.

    docker-compose up -d
    
Alternatively - you can just run it all in a one-liner!

    gradle clean build; docker-compose kill; docker rm `docker ps --no-trunc -aq`; docker-compose build; docker-compose up -d

After this process - you should be able to hit the app @

    http://192.168.99.100:9002/api/1/factors?input=135

#### Connecting to the Docker DB

    -Host: 192.168.99.100
    -Port: 3306
    -User: root
    -Password: password
    -DB: rds-test