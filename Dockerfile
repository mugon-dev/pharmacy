FROM openjdk:17
ARG JAR_FILE=build/libs/app.jar
COPY ${JAR_FILE} ./app.jar
ENV TZ=Asiz/Seoul
ENTRYPOINT ["java","-jar","./app.jar"]