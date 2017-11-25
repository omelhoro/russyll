FROM clojure

RUN apt-get install curl \
  && curl --silent --location https://deb.nodesource.com/setup_9.x | bash - \
  && apt-get install -y nodejs

RUN mkdir /app

WORKDIR /app

COPY ./project.clj /app
RUN lein deps

COPY ./package.json /app
RUN npm install

COPY ./ /app

RUN lein cljsbuild once server
RUN lein test
RUN lein uberjar

CMD ["node","app"]

EXPOSE 5000
