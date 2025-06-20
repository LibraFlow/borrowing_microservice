FROM gradle:8.6.0-jdk21 AS build
WORKDIR /app
COPY . .
RUN ./gradlew bootJar

FROM gcr.io/distroless/java21-debian12
WORKDIR /app
COPY --from=build /app/app/build/libs/*.jar app.jar

ENV PORT=8086
ENV SPRING_PROFILES_ACTIVE=prod
ENV INSTANCE_CONNECTION_NAME=libraflow-457510:europe-west4:borrowing-service-db
ENV SPRING_DATASOURCE_USERNAME=user5
ENV SPRING_DATASOURCE_PASSWORD=password5
ENV SPRING_DATASOURCE_DATABASE=db5

EXPOSE 8086

# The distroless image does not have a shell or tools like curl,
# so the original HEALTHCHECK and startup script are removed.
# The Java process is started directly.
CMD ["/app/app.jar"] 