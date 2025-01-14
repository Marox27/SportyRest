FROM amazoncorretto:17-alpine-jdk

COPY target/SportyRest-0.0.1-SNAPSHOT.jar /ApiSportyRest-v1.jar

ENTRYPOINT ["java", "-jar", "/ApiSportyRest-v1.jar"]
