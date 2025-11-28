FROM eclipse-temurin:17-jdk
VOLUME /tmp
COPY target/User-K8S-0.0.1-SNAPSHOT-exec.jar User-0.0.1-SNAPSHOT-exec.jar
ENTRYPOINT ["java","-jar","User-0.0.1-SNAPSHOT-exec.jar"]