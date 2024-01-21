FROM alpine:latest

RUN apk add --no-cache openjdk21

WORKDIR /app

COPY app/build/libs/app-uber.jar /app/podweb.jar

COPY app/src/main/static/out.css /app/static/out.css

EXPOSE 7000

ENV PODWEB_PRODUCTION ""

CMD ["java", "-jar", "podweb.jar"]