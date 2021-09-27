FROM openjdk:11 as dropwizard-img
RUN mkdir -p app
COPY ./target/ds-project1-1.0-SNAPSHOT.jar /app
COPY ./src/config/config.yml /app
WORKDIR /app

FROM nginx:alpine as ui-server
COPY /ui/index.html /usr/share/nginx/html
COPY /ui/my-script.js /usr/share/nginx/html
