FROM jenkins/jenkins:lts
USER root
RUN apt-get update -qq \
    && apt-get install -qqy apt-transport-https ca-certificates curl gnupg2 software-properties-common
RUN curl -fsSL https://download.docker.com/linux/debian/gpg | apt-key add -
RUN add-apt-repository \
    "deb [arch=amd64] https://download.docker.com/linux/debian \
    $(lsb_release -cs) \
    stable"
RUN apt-get update  -qq \
    && apt-get install docker-ce -y
RUN usermod -aG docker jenkins

FROM openjdk:8
ADD target/java-sspr_lab3.jar java-sspr_lab3.jar
ENTRYPOINT ["java", "-jar","java-sspr_lab3.jar"]
EXPOSE 8080