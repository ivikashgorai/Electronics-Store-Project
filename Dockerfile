#as we are using java version 23 in the project
FROM openjdk:23
#taking jar file in argument in target folder when project will get build
ARG JAR_FILE=target/*.jar
#Copying the jar file,name of the jar file in docker will be electronics_store
COPY ${JAR_FILE} electronics_store.jar
#below command will get run
ENTRYPOINT ["java","-jar","/electronics_store.jar"]