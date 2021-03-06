FROM openjdk:15
EXPOSE 5002

COPY target/rms-application-service-*.jar /rms-application-service.jar

ENTRYPOINT ["java", "-jar", "/rms-application-service.jar"]
