FROM openjdk:11
ARG AUTH_JAR=Authentication-2.0.0-alpha.jar
COPY build/libs/$AUTH_JAR $AUTH_JAR
ENTRYPOINT ["java","-jar","/$AUTH_JAR"]
