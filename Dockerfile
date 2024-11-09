FROM ghcr.io/graalvm/graalvm-ce:22 as build

RUN gu install native-image

RUN curl -O https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein && \
    chmod +x lein && \
    mv lein /usr/bin/lein && \
    lein upgrade

COPY . /usr/src/app

WORKDIR /usr/src/app

RUN lein uberjar

RUN native-image \
      --no-fallback \
      --report-unsupported-elements-at-runtime \
      --trace-class-initialization=ch.qos.logback.core.status.InfoStatus \
      --initialize-at-run-time=io.prometheus,org.pg,org.bouncycastle,org.slf4j,io.opentelemetry,org.eclipse.jetty.server,org.eclipse.jetty.util,ch.qos.logback,org.eclipse.jetty.http,org.eclipse.jetty.http2,org.eclipse.jetty.server.ServerConnector,org.apache.http.impl.auth.NTLMEngineImpl,io.prometheus.client.Striped64 \
      --features=clj_easy.graal_build_time.InitClojureClasses \
      --enable-url-protocols=http,https \
      -Dio.pedestal.log.defaultMetricsRecorder=nil \
      -jar ./target/rango-graalvm-0.1.0-SNAPSHOT-standalone.jar \
      -H:Name=rango \
      -H:+ReportExceptionStackTraces \
      -H:+StaticExecutableWithDynamicLibC \
      -H:ReflectionConfigurationFiles=reflect-config.json \
      -H:CCompilerOption=-pipe

FROM gcr.io/distroless/base:latest

WORKDIR /app

COPY --from=build /usr/src/app/rango  /app/rango

CMD ["./rango"]
