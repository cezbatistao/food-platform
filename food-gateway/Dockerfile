FROM openjdk:11-jre-slim
VOLUME /tmp

ENV SPRING_PROFILES_ACTIVE default

ADD build/libs/app.jar /app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS="-Xms512m -Xmx512m"

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
