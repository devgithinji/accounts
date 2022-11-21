#start with a base image containing the Java run time
FROM openjdk:17-slim as build

#information around who maintains the image
MAINTAINER densoft.com

#Add the application's jar to the container
COPY target/Accounts-0.0.1-SNAPSHOT.jar Accounts-0.0.1-SNAPSHOT.jar

#exceute the application
ENTRYPOINT ["java","-jar","/Accounts-0.0.1-SNAPSHOT.jar"]

