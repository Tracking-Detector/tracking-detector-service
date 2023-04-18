FROM openjdk:17-jdk-alpine
LABEL maintainer=heschwerdt
WORKDIR /app
COPY libs libs/
COPY resources resources/
COPY classes classes/
ENTRYPOINT ["java", "-cp", "/app/resources:/app/classes:/app/libs/*", "com.trackingdetector.trackingdetectorservice.TrackingDetectorServiceApplicationKt"]