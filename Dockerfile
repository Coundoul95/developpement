FROM openjdk:11

MAINTAINER afrilins.net

# Service name
ENV SERVICE_NAME gestionenqquete

ENV VERSION 0.0.1-SNAPSHOT

# Context path
ENV CONTEXT_PATH gestionenquete

# WAR filename
ENV WAR_FILENAME gestionenquete.jar

COPY target/${SERVICE_NAME}-${VERSION}.jar ${WAR_FILENAME}

COPY ./entrypoint.sh /

RUN chmod 755 entrypoint.sh

ENTRYPOINT ["java", "-jar", "/gestionimmeublerapport.jar"]

# Expose necessary port
EXPOSE 8989

# HEALTHCHECK
HEALTHCHECK --retries=3 CMD curl --fail http://localhost:8989/actuator/health || exit 1

