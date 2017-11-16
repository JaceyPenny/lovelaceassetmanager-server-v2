# Lovelace Asset Manager (Springboot Server)

This is the Springboot Server that serves Lovelace Technologies Asset Management API requests. This
is a project intended to be deployed to the heroku dyno "lovelaceassetmanager-server"

## Running the application

To run the application from your IDE, simply run the `Application` class as
a Java Application.
Alternatively the application can be started from the terminal using maven with `mvn spring-boot:run`.
After starting the application, point your browser to http://localhost:8080.

## Using a Postgres database for persistence

For running the application using a real [Postgres](http://www.postgresql.org/) database, uncomment all property
definitions in the `application.properties` file and put the configuration for your Postgres instance there.
You can also use [Docker](http://docker.com) for starting a Postgres database. Just run the `docker-postgres.sh` script
and it will create a Postgres container for you. When using docker, all you have to change in the
`application.properties` file is `spring.datasource.url`. For boot2docker users, the host name should already be
correct. For Linux users it has to be changed to localhost.

## License

Code is under the [Apache Licence v2](https://www.apache.org/licenses/LICENSE-2.0.txt).
