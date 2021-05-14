FROM openjdk:11
EXPOSE 8080
ADD ./build/libs/CustomerFrontEnd-0.0.1-SNAPSHOT.jar /srv/CustomerFrontEnd-0.0.1-SNAPSHOT.jar
CMD java -jar /srv/CustomerFrontEnd-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
