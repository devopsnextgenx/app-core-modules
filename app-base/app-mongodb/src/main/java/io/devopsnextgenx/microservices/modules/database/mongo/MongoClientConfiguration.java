package io.devopsnextgenx.microservices.modules.database.mongo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.lang.NonNull;

@Configuration
public class MongoClientConfiguration extends AbstractMongoClientConfiguration {

  @Value("${appx.modules.db.mongodb.uri:mongodb://localhost:27017/appx}")
  private String mongoUri;

  @Value("${appx.modules.db.mongodb.collections:io.devopsnextgenx.microservices.modules.collections}")
  private String collections;

  @Value("${appx.modules.db.mongodb.cluster}")
  private String cluster;

  @Value("${appx.modules.db.mongodb.dbname}")
  private String database;

  @Value("${appx.modules.db.mongodb.authentication-database}")
  private String authDatabase;

  @Value("${appx.modules.db.mongodb.username}")
  private String username;

  @Value("${appx.modules.db.mongodb.password}")
  private String password;

  @Override
  public @NonNull MongoClient mongoClient() {
    MongoCredential credentials = MongoCredential.createCredential(username, authDatabase, password.toCharArray());
    final ConnectionString connectionString = new ConnectionString(getConnectionString(false));
    final MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
        .applyConnectionString(connectionString)
        .credential(credentials)
        .build();
    return MongoClients.create(mongoClientSettings);
  }

  protected String getConnectionString(boolean isLogsEnabled) {
    StringBuilder mongoConnectionString = new StringBuilder();
    mongoConnectionString.append("mongodb://")
      .append(cluster).append("/")
      .append(database).append("?retryWrites=true&w=majority");
    return mongoConnectionString.toString();
  }

  @Override
  protected @NonNull String getDatabaseName() {
    return database;
  }
}