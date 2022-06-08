FROM openjdk:17-alpine
RUN addgroup -S cryptocheck && adduser -S cryptocheck -G cryptocheck
USER cryptocheck:cryptocheck
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]