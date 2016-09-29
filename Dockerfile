FROM java:8
VOLUME /tmp
ADD /build/libs/cds-test-1.0.0.jar app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-Xms256m", "-Xmx512m", "-Dspring.profiles.active=local", "-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
