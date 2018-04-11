FROM theasp/clojurescript-nodejs:latest AS builder

WORKDIR /app
COPY ./project.clj /app
RUN lein deps
COPY ./ /app
RUN lein uberjar

FROM nginx AS server
COPY --from=builder /app/resources/public /usr/share/nginx/html
EXPOSE 80
