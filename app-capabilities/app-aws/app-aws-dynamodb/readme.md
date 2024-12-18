### DynamoDb Configuration

- Default Properties for DynamoDb Environment from `app.modules.dynamodb`

### Run Dynamodb Locally
 - Refer to the local-dynamodb folder in the current folder.
 
### Install Local Dynamodb GUI browser
```
npm install dynamodb-admin -g
export DYNAMO_ENDPOINT=http://localhost:8000
dynamodb-admin

## dynamodb-admin listening on http://localhost:8001 (alternatively http://0.0.0.0:8001)
```

### Install Local SQS GUI browser
```
npm install sqs-admin -g
export SQS_ENDPOINT=http://localhost:5555
sqs-admin

## dynamodb-admin listening on http://localhost:8002 (alternatively http://0.0.0.0:8002)
```

### Integrative and Runtime modes
    - Integrative mode is used for the local dynamodb
    - Runtime mode is used for the AWS dynamodb