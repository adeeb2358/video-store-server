FROM adoptopenjdk/openjdk15 AS gradle
WORKDIR /appsrc
FROM gradle AS dev
# run development instance
WORKDIR /app
CMD ["./gradlew", "bootRun"]

FROM gradle AS builder
# build
COPY . .
RUN ./gradlew bootJar --info
RUN mkdir -p build/unpacked && (cd build/unpacked && jar -xf ../libs/video.sharing.server-0.0.1-SNAPSHOT.jar)

FROM adoptopenjdk/openjdk15:alpine-jre
# package the app for deployment
WORKDIR /app
ARG BUILD_SRC=/appsrc/build/unpacked
COPY --from=builder ${BUILD_SRC}/BOOT-INF/lib /app/lib
COPY --from=builder ${BUILD_SRC}/META-INF /app/META-INF
COPY --from=builder ${BUILD_SRC}/BOOT-INF/classes /app
RUN addgroup -S javagroup && adduser -S javauser -G javagroup && \
    chown -R javauser:javagroup /app
USER javauser
ENTRYPOINT ["java", "-cp", "/app:/app/lib/*", "package VideoStorageServerApplication"]