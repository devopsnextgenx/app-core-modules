package io.devopsnextgenx.base.modules.aws.sqs.providers;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import io.devopsnextgenx.base.modules.aws.sqs.config.AppSqsConfig;
import io.devopsnextgenx.base.modules.config.aws.AppResourceClientProvider;
import io.devopsnextgenx.base.modules.config.aws.exceptions.AppAwsResourceException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * AwsLocalDynamoDBProvider:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/15/2019
 */
@Slf4j
@AllArgsConstructor
public class AwsLocalSQSClientProvider extends AppResourceClientProvider implements AmazonSQSClientProvider {
    @Override
    public AmazonSQS provide(AppSqsConfig config) throws AppAwsResourceException {
        AppResource resourceDetails = getResourceDetails(config);

        AmazonSQS amazonSQS = AmazonSQSClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                        String.format("http://%s:%s", resourceDetails.getHostname(), resourceDetails.getPort()),
                        config.getRegion())
                )
                .build();
        log.info("Local SQS client was created at '[{}:{}]' .", resourceDetails.getHostname(), resourceDetails.getPort());
        return amazonSQS;
    }

    @Override
    public AmazonSQSAsync provideAsync(AppSqsConfig config) throws AppAwsResourceException {
        AppResource resourceDetails = getResourceDetails(config);

        AmazonSQSAsync amazonSQSAsync = AmazonSQSAsyncClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                        String.format("http://%s:%s", resourceDetails.getHostname(), resourceDetails.getPort()),
                        config.getRegion())
                )
                .build();
        log.info("Local SQSAsync client was created at '[{}:{}]' .", resourceDetails.getHostname(), resourceDetails.getPort());
        return amazonSQSAsync;
    }

}
