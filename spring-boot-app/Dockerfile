FROM curlimages/curl:7.81.0 AS download
ARG OTEL_AGENT_VERSION="1.18.0"
RUN curl --silent --fail -L "https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v${OTEL_AGENT_VERSION}/opentelemetry-javaagent.jar" \
    -o "$HOME/opentelemetry-javaagent.jar"

FROM maven:eclipse-temurin AS build
ADD . /build
RUN cd /build && mvn package --quiet

FROM eclipse-temurin:17
# Healthcheck is done by Kubernetes probes
HEALTHCHECK NONE
COPY --from=build /build/target/*.jar /app.jar
COPY --from=download /home/curl_user/opentelemetry-javaagent.jar /opentelemetry-javaagent.jar
ENTRYPOINT ["java", \
  "-javaagent:/opentelemetry-javaagent.jar", \
  "-jar", "/app.jar" \
  ]