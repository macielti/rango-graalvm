FROM container-registry.oracle.com/graalvm/native-image:23 as build

RUN curl -O https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein && \
    chmod +x lein && \
    mv lein /usr/bin/lein && \
    lein upgrade

COPY . /usr/src/app

WORKDIR /usr/src/app

RUN lein clean

RUN lein deps

RUN lein uberjar

RUN lein native

FROM gcr.io/distroless/base:latest

WORKDIR /app

COPY --from=build /usr/src/app/target/rango-graalvm  /app/rango-graalvm

CMD ["./rango-graalvm"]
