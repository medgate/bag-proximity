# BAG COVID-19 Screening Backend

## Purpose

This project serves as a backend API for the BAG COVID-19 Screening app. The submitted answers are
stored for statistical purposes into a dedicated database.

## Local setup

Install at least the following:

- JDK 11 - see [SDKMAN!](https://sdkman.io/) for an easy setup
- [Docker CE](https://docs.docker.com/install/)
- [Docker Compose](https://docs.docker.com/compose/install/)

### Dependencies

This backend application relies on the following components:

- Locally accessible [MSSQL](https://www.microsoft.com/en-us/sql-server/sql-server-2019) store

Ensure those dependencies are available and ready before running the Backend.

### Running the backend locally with Gradle

The following commands will run the application through the bootRun Gradle plugin.
```shell script
./run.sh
```
or simply:
```shell script
./gradlew clean bootRun
```

### Running the backend dependencies with docker-compose

Define your own local properties by editing the
[application.properties](src/main/resources/application.properties) file and optionally define your
environment variables in your own .env file.

Here is a `.env` file example:
```
# MSSQL
SPRING_DATASOURCE_URL=jdbc:sqlserver://localhost;databaseName=bag
SPRING_DATASOURCE_USERNAME=sa
SPRING_DATASOURCE_PASSWORD=Pass@word
```

Before you run docker-compose, ensure the custom MSSQL image is available.
To build the Docker image locally you can run the build script as follows:
```shell script
cd mssql
./build.sh
```

Then, you can run the Backend dependencies through Docker Compose, as follows:
```shell script
docker-compose -f docker-compose-dev.yml up --build
```

### Debug mode

To allow debugging from the IDE, append flag `--debug-jvm` to a run command. Use the `--info` flag
to get more debug output.

## Docker build

Run the following command to build a Docker image of the app. Adapt the tag name to your needs:
```shell script
docker build -t app .
```

### Running the backend with docker-compose in other environments

Define the appropriate environment variables to override those present in the
[application.properties](src/main/resources/application.properties).
Have a look at the [docker-compose.yml](docker-compose.yml) and
[docker-compose.override.yml](docker-compose.override.yml) files.

Before you run docker-compose, ensure you have the rights to access the Docker repository, that all
images have been previously built and are made available for download.

Then, you can run the all the components through Docker Compose, as follows:
```shell script
docker-compose up
```

## Running tests

Tests for the entire app can be run through the following command:
```shell script
./gradlew test
```

Use the `--tests` option to run specific tests, e.g.
```shell script
./gradlew test --tests="*.integration.*"
```

## Delivery

Continuous build and delivery is partially automated through Azure Pipelines.
Branches have the following roles:
- `develop` is the development branch, each commit is automatically deployed to the INT environment.
- `qa` is the staging branch, HEAD commit from this branch can be manually deployed to the QA environment.
- `master` is the production branch, tagging the commit and manually deploying the tag to the PRD environment is mandatory.

The `bag-corona` source code repository contains all related pipeline configurations for deployment.

### Environments

The following environments are available:
- INT: integration environment, accessible through OpenWeb or Medgate VPNs or from internal IPs.
- QA: staging environment, publicly accessible, but secured through basic Auth.
- PRD: production environment, publicly accessible.

Please consult the Wiki for obtaining appropriate credentials or VPN access.

### Release procedure

This project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html) principles.
To release a new x.y.z version follow those simple steps:

- Create a `release/x.y.z` branch from the current production version tag, where x.y.z is the new version.
- Increase the version inside the (gradle.properties)[gradle.properties] file to x.y.z.
- Merge your features (e.g. from `develop`) into the `release/x.y.z` branch.
- Create Pull Requests for both `master` and `develop` branches.
- Once approved, merge the `release/x.y.z` branch into the `master` and `develop` branches.
- Cleanup the `release/x.y.z` branch by removing it after a successful merge.
- Tag the `master` branch with a new version tag `vx.y.z`, the `v` prefix being mandatory.

### Hotfixes

To perform hotfixes to production while the `develop` branch already went too far, instead of a
`release/x.y.z` branch, create a `hotfix/x.y.z` branch from the git tag corresponding to the current
problematic production version.

Release the fix by following the previous procedure and do not forget to merge it back to `develop`
once done.

## API Documentation

A Postman collection and a local environment config are available under the `docs` folder.

Swagger UI can be accessed locally [here](http://localhost:8080/swagger-ui.html)

### Authentication

The application is publicly available. There are no specific user roles.

## Database migrations

Database schema for MSSQL is versioned through migrations.

[Flyway](https://flywaydb.org/) is used for MSSQL migrations. The migration files are located under
the [db.migrations](/src/main/resources/db/migrations) package.

Additionally, a MSSQL schema documentation can be found [here](https://dbdiagram.io/d/5e7874e44495b02c3b889f96).
Please, keep it up-to-date and inform the customer about important changes.

![MSSQL schema](/docs/BAGCoronapp-db-schema.png)

## Contributing

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

### Design considerations

The project is layered as an 'onion' with the core domain at the center and other layers around.
Packages map to those layers as follows:

- `common`: contains minimal amount of __common__ code shared in-between layers, e.g. Time Helper.
- `domain`: __business logic__ containing Use Cases (Services), domain Entities at the core and
            interfaces to external layers, e.g. Clients.
- `web.api`: __REST API__ layer containing Controllers, DTOs and related JSON Mappers.
- `web.security`: __security__ layer implementing API authentication.

The most important aspect of this design is that objects from the `domain` layer shall not have any
dependency on other application or infrastructure layers.

The other layers can use the `domain` but usually manipulate their own data representations (e.g. DTOs)
and use dedicated mappers to convert their results back to domain entities.

The `common` code shall protect `domain` objects from any specific framework or infrastructure
concerns. However, the overall idea is to keep the size og this package as small as possible.

### Resources

This architecture draws it's inspiration from several sources (different words, similar concepts).
These are pretty in-depth lectures. If you feel ready, please plan some of your own time ahead to
study some of these resources.

- Clean Architecture
    - [Clean Architecture Blog](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
    - [Clean Architecture with Java 11](https://medium.com/slalom-engineering/clean-architecture-with-java-11-f78bba431041)
    - [Spring Data and Clean Architecture](https://blog.sourced-bvba.be/article/2017/05/01/what-i-dont-like-spring-data/)
- Explicit Architecture
    - [Explicit architecture](https://herbertograca.com/2017/11/16/explicit-architecture-01-ddd-hexagonal-onion-clean-cqrs-how-i-put-it-all-together/)
    - [Onion architecture](https://www.thinktocode.com/2018/08/16/onion-architecture/)
- Hexagonal Architecture
    - [Chris Fidao's article](https://fideloper.com/hexagonal-architecture)
- Ports and Adapters
    - [Ports and Adapters Pattern ](https://softwarecampament.wordpress.com/portsadapters/)
- Domain Driven Design
    - [DDD Resources](https://domainlanguage.com/ddd/)
    - [DDD Reference](http://domainlanguage.com/wp-content/uploads/2016/05/DDD_Reference_2015-03.pdf)
    - [Domain Driven Design by Eric Evans](http://www.beginnersheap.com/domain-driven-design-eric-evans-pdf-download-free/)
    - [Domain-Driven Design Intro](https://herbertograca.com/2017/09/07/domain-driven-design/)

### Reference Documentation

For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.2.5.RELEASE/gradle-plugin/reference/html/)
* [Spring Security](https://docs.spring.io/spring-boot/docs/2.2.5.RELEASE/reference/htmlsingle/#boot-features-security)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/2.2.5.RELEASE/reference/htmlsingle/#production-ready)
* [Spring Configuration Processor](https://docs.spring.io/spring-boot/docs/2.2.5.RELEASE/reference/htmlsingle/#configuration-metadata-annotation-processor)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.2.5.RELEASE/reference/htmlsingle/#boot-features-developing-web-applications)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/2.2.5.RELEASE/reference/htmlsingle/#using-boot-devtools)

### Spring Guides

The following guides illustrate how to use some features concretely:

* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
* [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)
* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)
