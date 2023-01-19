FROM openjdk:19
RUN mkdir /app

WORKDIR /app

#ENV PORT 8080
EXPOSE 8080
#COPY target/*.jar /app/app.jar
#ENTRYPOINT exec java $JAVA_OPTS -jar app.jar

ADD ./target/admin-service-0.0.1-SNAPSHOT.jar /app

EXPOSE 8080

CMD ["java", "-jar", "admin-service-0.0.1-SNAPSHOT.jar"]