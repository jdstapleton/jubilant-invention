# this file is designed to be in a context with only the stuff to be placed
# in the image is in the context.  (i.e. target/docker once the build-docker-img.sh
# is built)
FROM openjdk:11-jdk-slim
RUN adduser --system --home /home/app --group --disabled-password app
USER app
COPY . /home/app
WORKDIR /home/app

VOLUME ["/home/app/config"]
CMD ["java", "-jar", "bool-expression-matcher-service.jar"]
EXPOSE 8080
