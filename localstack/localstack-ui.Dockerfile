FROM node:lts-alpine3.11

COPY localstack-ui-entrypoint.sh /usr/local/bin/
RUN ln -s /usr/local/bin/local-ui-entrypoint.sh /
RUN apk add --update nodejs nodejs-npm
RUN npm install sqs-admin -g && npm install dynamodb-admin -g



CMD ["localstack-ui-entrypoint.sh"]
