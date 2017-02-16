FROM iron/java:latest

USER root

COPY url-shortener.jar /app/url-shortener.jar

COPY start.sh /app/start.sh
RUN chmod 777 /app/start.sh

EXPOSE 9000

ENTRYPOINT ["/app/start.sh"]