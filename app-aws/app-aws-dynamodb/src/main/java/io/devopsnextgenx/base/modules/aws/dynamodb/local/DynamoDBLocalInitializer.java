package io.devopsnextgenx.base.modules.aws.dynamodb.local;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.util.StringUtils;
import io.devopsnextgenx.base.modules.aws.dynamodb.BaseDynamoDBMapperInitializer;
import io.devopsnextgenx.base.modules.aws.dynamodb.config.AppDynamoDBConfig;
import io.devopsnextgenx.base.modules.config.aws.AppAwsProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.LinkedList;
import java.util.List;

/**
 * DynamoDBIntegrativeInitializer:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/15/2019
 */
@Slf4j
public class DynamoDBLocalInitializer extends BaseDynamoDBMapperInitializer {

    public DynamoDBLocalInitializer(AppDynamoDBConfig appDynamoDBConfig, AmazonDynamoDB awsDynamoDB,
                                          AWSCredentialsProvider awsCredentialsProvider, AppAwsProperties appAwsProperties) {
        this.appDynamoDBConfig = appDynamoDBConfig;
        this.awsDynamoDB = awsDynamoDB;
        this.awsCredentialsProvider = awsCredentialsProvider;
        this.appAwsProperties = appAwsProperties;
    }

    @Override
    public void initializeResources() {
        doCreation();
    }

    public void doCreation() {
        log.info("Creating Dynamodb tables!!!");
        if (StringUtils.isNullOrEmpty(appDynamoDBConfig.getDevelopment().getModelsPackage()))
            throw new RuntimeException("Package name must be specified in order to create the tables for local Dynamo DB");

        List<String>  tables = null;
        try {
            tables = awsDynamoDB.listTables().getTableNames();
        } catch (Exception e) {
            log.error("Failed to list tables from local dynamoDB, check if local dynamoDB server is running:  {}", e.toString());
            throw new RuntimeException(String.format("Failed to list tables from local dynamoDB, check if local dynamoDB server is running:  %s", e.toString()));
        }

        for (Class<?> model : findAllConfigurationClassesInPackage(appDynamoDBConfig.getDevelopment().getModelsPackage())) {
            CreateTableRequest tableRequest = dynamoDBMapper().generateCreateTableRequest(model);

            if (tables.contains(tableRequest.getTableName())) {
                log.info("Table '{}' already created, Skipped table creation.", tableRequest.getTableName());
                continue;
            }

            tableRequest.setProvisionedThroughput(new ProvisionedThroughput(15L, 10L));

            awsDynamoDB.createTable(tableRequest);
            log.info("{} table was created", tableRequest.getTableName());
        }
    }

    private List<Class<?>> findAllConfigurationClassesInPackage(String packageName) {
        List<Class<?>> annotatedClasses = new LinkedList<>();
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(true);
        provider.addIncludeFilter(new AnnotationTypeFilter(DynamoDBTable.class));
        for (BeanDefinition beanDefinition : provider.findCandidateComponents(packageName)) {
            try {
                annotatedClasses.add(Class.forName(beanDefinition.getBeanClassName()));
            } catch (ClassNotFoundException e) {
                log.warn("Could not resolve class object for bean definition", e);
            }
        }
        return annotatedClasses;
    }

}
