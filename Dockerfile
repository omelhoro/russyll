FROM node:6

RUN mkdir /app
ENV NODE_ENV production
WORKDIR /app

COPY ./package.json /app
RUN npm install --loglevel silent

COPY ./ /app

CMD ["npm","start"]

EXPOSE 5000
