FROM clojure:lein-alpine AS frontend-builder

WORKDIR /app
COPY ./project.clj /app
RUN lein deps
COPY ./ /app
RUN lein uberjar

FROM nginx AS server
COPY --from=frontend-builder /app/resources/public /usr/share/nginx/html
EXPOSE 80
