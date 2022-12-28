FROM eclipse-temurin:11-jre-alpine
VOLUME /tmp

RUN apk add --no-cache curl

ADD build/libs/app.jar /app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS="-Xms512m -Xmx512m"

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
