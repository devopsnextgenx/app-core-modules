nssm.exe install local-dynamodb "C:\Program Files\Java\jre-9.0.4\bin\java.exe" "-jar C:\opt\utils\aws\local-dynamo-db-server.jar -dbPath C:\opt\utils\aws -sharedDb"

java -Djava.library.path=/opt/utils/aws -jar /opt/utils/aws/local-dynamo-db-server.jar -dbPath /opt/utils/aws -sharedDb