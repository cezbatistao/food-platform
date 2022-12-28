FROM eclipse-temurin:11-jre-alpine
VOLUME /tmp

RUN apk add --no-cache curl

ENV ENVIRONMENT_APP default

ADD build/libs/app.jar /app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS="-Xms512m -Xmx512m"

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Dspring.profiles.active=${ENVIRONMENT_APP} -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
