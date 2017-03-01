
# Readme

The application is built using Scala 2.11, Akka 2.4.8 and Akka Http. The frontend is built using AngularJs.

## Installation

In order to install and execute the application the following tools are required:

 - Maven (for Java and Scala dependencies)
 - Bower (for Javascript dependencies)
 - Docker

To install all the JS dependencies:

    cd src/main/resources/web
    bower install

To build the application, run the tests and finally create a docker image do the following:

    cd ../../../..
    mvn clean install

Once done you can execute the application by doing:

    docker-compose up

Please make sure that the current configuration expects Docker to run natively on the machine.
In case you are running boot2docker (or docker-machine) you can change the HTTP_ENDPOINT environment variable in the docker-compose file

Redis is used as datastore to persist the data. You can only spin up Redis by doing

    docker-compose -f docker-compose-redis.yml up

and then run the application

    cd target
    java -DREDIS_ENDPOINT=127.0.0.1 -jar  url-shortener.jar




### Limitations

 The url validation uses a regular exception that requires "http://" to be part of the url to be shortened.
 If "http://" is missing it will be considered invalid URL.
