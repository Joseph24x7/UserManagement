FROM openjdk:17-oracle
EXPOSE 8081
ADD target/usermanagement.jar usermanagement.jar
ENTRYPOINT ["java","-jar","/usermanagement.jar"]
