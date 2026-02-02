FROM eclipse-temurin:17-jdk-alpine

RUN apk add --no-cache curl

WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

RUN ./mvnw dependency:go-offline

COPY src ./src

RUN ./mvnw clean package -DskipTests

EXPOSE 8080

ENV JAVA_OPTS="-XX:+UseContainerSupport \
    -XX:MaxRAMPercentage=75.0 \
    -XX:+PrintFlagsFinal \
    -XX:+UnlockExperimentalVMOptions \
    -Xlog:gc*:file=/app/gc.log:time,uptime:filecount=5,filesize=10M"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar target/task-management-1.0.0.jar"]