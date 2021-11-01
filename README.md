# Medgate Leitfaden (bag-proximity)

## Purpose

The BAG Proximity triager helps triage Swiss Covid users that were in contact with infected users.

## Local setup

Install at least the following:

- JDK 11 (https://sdkman.io/)
- [Docker](https://docs.docker.com/install/)
- [Docker Compose](https://docs.docker.com/compose/install/)

### Run the component's dependencies

Dependencies can be easily set up via Docker:
```shell script
docker-compose -f docker-compose-dev.yml up --build
```

### Run the component

The application can be run using Docker.
```shell script
docker-compose up
```
This exposes the frontend on port 8081, and the backend on port 8080

Define your local properties by editing
[application.properties](proximity-backend/src/main/resources/application.properties).

The application is available at http://localhost:8080.

### Debug mode

To allow debugging from the IDE, append flag `--debug-jvm` to a run command.

### Authentication

No authentication

## API Documentation

A Postman collection and the environment configs are available under the `docs` folder.

Swagger UI can be accessed at http://localhost:8080/swagger-ui.html.

## Continuous Delivery

Continuous Delivery is automated by Azure.
Branches have the following roles:
- `develop` is the main development branch, deploying automatically to the DEV environment.
- `master` is the release branch, tagging the commit and pushing automatically to the Medgate Docker Registry.
- To perform hotfixes to production while the `master` branch already went too far, create a `support/` branch 
from the git tag corresponding to the production version. A `support/` branch has the same behavior as `master`.


## Running tests

Tests for the entire app can be run through the following command:

```shell script
./gradlew test
```

Alternatively, use the following command if you want to only run your backend unit tests:

```shell script
./gradlew :proximity-backend:cleanTest :proximity-backend:test -x :proximity-frontend:npm_run_build
```

Gradle will skip the longest frontend task (ng build), allowing you to reduce the test suite execution time.

## Gradle tips

- Use the `--info` flag to get more debug output
- Use the `--tests` option to run specific tests, e.g. `--tests="*.ApplicationTests.*"`
- For debug mode, you can still use the `--debug-jvm` option


## Docker build

### Build Docker images

First, log in on the SDT Docker registry to be able to pull the `java` base image:

```shell script
docker login -u medgate-user registry.openwt.com
```

Then, run the following command to build a Docker image of the app:

```shell script
./gradlew clean assemble buildDocker
```

### Run in Docker

To run the full application stack with Docker, execute the following commands:
```shell script
docker network create traefik
docker-compose -f docker-compose-dev.yml up -d
```


## Contributing

### Issues

Here is a quick reminder of our workflow:

- Once the team has agreed to work on a particular task, take the lead and focus on it until its completion.
- Please assign the task to you and move it to the "In Progress" column to show the team you are working on it.
- Before completion, ensure your work has been reviewed by someone else on the team.
- Assign the task to the reviewer and move it to the "In Review" column.
- Once reviewed, the task shall be moved to "Done" and assigned to the original requester (e.g. PM) 


### Coding style

The code follows the [Google Java style guide](https://google.github.io/styleguide/javaguide.html).
The project won't build if there is a code formatting violation.

IntelliJ users are strongly encouraged to configure their IDE as follows.
- Install the `google-java-format` plugin through Settings > Plugins > Marketplace
- Enable the `google-java-format` plugin for this project
- Import the provided `intellij-java-google-style.xml` style (see `docs` folder):
    - in Settings > Editor > Code Style 
    - select the provided XML file under Scheme > Import Scheme > IntelliJ IDEA code style XML
- Install the `Save actions` plugin to automatically format the source code on 'Save':
    - enable `Save actions` plugin for this project from Settings > Other settings > Save actions
    - the `intellij-save-actions-config.png` screenshot shows the suggested configuration for this plugin
- Ensure `Lombok` plugin is installed
- Enable annotation processing from Settings > Build, Execution, Deployment > Compiler > Annotation Processing

## Proximity Backend

Implements recommendation logic.
It currently uses a Java 11 + Spring Boot 2 + MSSQL server-side stack.

### Design considerations

The project is layered as an 'onion' with the core domain at the center and other layers around.
Packages map to those layers as follows:

- `domain`: __business logic__ containing Use Cases (Services), domain Entities at the core and the Store (Repository) interfaces.
- `shared`: contains minimal amount of __common__ code shared in-between layers, e.g. Pagination.
- `web.api`: __REST API__ layer containing Controllers, DTOs and related JSON Encoders.
- `storage`: __persistence__ layer containing the Repository implementations (e.g. for MongoDB).

The most important aspect of this design is that object form the `domain` has no dependencies on other application or infrastructure layers.

The other layers can use the `domain` but usually manipulate their own data representations (e.g. DTOs, persistence-related objects)
and use dedicated mappers to convert their results back to domain entities.

The `shared` code protects `domain` objects from any specific framework concern (e.g. Spring, Spring Data).
However, the overall idea is to keep its size as small as possible.

### Resources

This architecture draws it's inspiration from several sources (different words, similar concepts).
These are pretty in-depth lectures. If you feel ready, please plan some of your own time ahead to study some of these resources.

- Clean Architecture
    - [Clean Architecture Blog](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
    - [Clean Architecture with Java 11](https://medium.com/slalom-engineering/clean-architecture-with-java-11-f78bba431041)
    - [Spring Data and Clean Architecture](https://blog.sourced-bvba.be/article/2017/05/01/what-i-dont-like-spring-data/)
- Explicit Architecture
    - [Explicit architecture](https://herbertograca.com/2017/11/16/explicit-architecture-01-ddd-hexagonal-onion-clean-cqrs-how-i-put-it-all-together/)
- Hexagonal Architecture
    - [Chris Fidao's article](https://fideloper.com/hexagonal-architecture)
- Ports and Adapters
    - [Ports and Adapters Pattern ](https://softwarecampament.wordpress.com/portsadapters/)
- Domain Driven Design
    - [DDD Resources](https://domainlanguage.com/ddd/)
    - [DDD Reference](http://domainlanguage.com/wp-content/uploads/2016/05/DDD_Reference_2015-03.pdf)
    - [Domain Driven Design by Eric Evans](http://www.beginnersheap.com/domain-driven-design-eric-evans-pdf-download-free/)
    - [Domain-Driven Design Intro](https://herbertograca.com/2017/09/07/domain-driven-design/)

## Proximity Frontend

Achieves the UI/UX part of by consuming the provided API on the client-side.
It currently uses Angular and has been built with [Angular CLI](https://github.com/angular/angular-cli) version 8.1.0.

### Development server

Run `npm start` for a dev server. Navigate to http://localhost:4200/. The app will automatically reload if you change any of the source files.

### Code scaffolding

Run `ng generate component component-name` to generate a new component. You can also use `ng generate directive|pipe|service|class|guard|interface|enum|module`.

### Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory. Use the `--prod` flag for a production build.

### Running unit tests

Run `ng test` to execute the unit tests via [Karma](https://karma-runner.github.io).

### Running end-to-end tests

Run `ng e2e` to execute the end-to-end tests via [Protractor](http://www.protractortest.org/).

### Further help

To get more help on the Angular CLI use `ng help` or go check out the [Angular CLI README](https://github.com/angular/angular-cli/blob/master/README.md).
