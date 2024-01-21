FROM gradle:6.5.1-jdk8 AS build

RUN apk add --no-cache openjdk21

COPY app/build/libs/app-uber.jar /app/podweb.jar
COPY app/src/main/static/out.css /app/static/out.css

FROM alpine:latest

WORKDIR /app

# Copy package.json and package-lock.json
COPY package*.json ./

# Install all dependencies
RUN npm install

EXPOSE 7000

# Build the app for production
RUN npm run prod

ENV PODWEB_PRODUCTION ""

CMD ["java", "-jar", "podweb.jar"]
