FROM openjdk:11 as dropwizard-img
RUN mkdir -p app
COPY ./target/ds-project1-1.0-SNAPSHOT.jar /app
COPY ./src/config/config.yml /app
WORKDIR /app
