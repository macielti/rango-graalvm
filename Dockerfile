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
      --initialize-at-run-time=org.apache.http.impl.auth.NTLMEngineImpl,io.prometheus.client.Striped64 \
      --features=clj_easy.graal_build_time.InitClojureClasses  \
      --initialize-at-build-time=io.prometheus.client.Collector \
      --initialize-at-build-time=org.pg.Pool \
      --initialize-at-build-time=org.bouncycastle.asn1.nist.NISTObjectIdentifiers,org.bouncycastle.asn1.x509.X509ObjectIdentifiers \
      --enable-url-protocols=http,https \
      -Dio.pedestal.log.defaultMetricsRecorder=nil \
      -jar ./target/rango-graalvm-0.1.0-SNAPSHOT-standalone.jar \
      -H:Name=rango \
      -H:+ReportExceptionStackTraces \
      -H:+StaticExecutableWithDynamicLibC \
      -H:CCompilerOption=-pipe

FROM gcr.io/distroless/base:latest

WORKDIR /app

COPY --from=build /usr/src/app/rango  /app/rango

CMD ["./rango"]
