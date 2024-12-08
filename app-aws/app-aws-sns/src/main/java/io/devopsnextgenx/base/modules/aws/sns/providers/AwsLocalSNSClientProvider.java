package io.devopsnextgenx.base.modules.aws.sns.providers;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSAsync;
import com.amazonaws.services.sns.AmazonSNSAsyncClientBuilder;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import io.devopsnextgenx.base.modules.aws.sns.config.AppSnsConfig;
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
public class AwsLocalSNSClientProvider extends AppResourceClientProvider implements AmazonSNSClientProvider {
    @Override
    public AmazonSNS provide(AppSnsConfig config) throws AppAwsResourceException {
        AppResource resourceDetails = getResourceDetails(config);

        AmazonSNS amazonSNS = AmazonSNSClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                        String.format("http://%s:%s", resourceDetails.getHostname(), resourceDetails.getHttpPort()),
                        config.getRegion())
                )
                .build();
        log.info("Local SNS client was created at '[{}:{}]' .", resourceDetails.getHostname(), resourceDetails.getHttpPort());
        return amazonSNS;
    }

    @Override
    public AmazonSNSAsync provideAsync(AppSnsConfig config) throws AppAwsResourceException {
        AppResource resourceDetails = getResourceDetails(config);

        AmazonSNSAsync amazonSNSAsync = AmazonSNSAsyncClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                        String.format("http://%s:%s", resourceDetails.getHostname(), resourceDetails.getHttpPort()),
                        config.getRegion())
                )
                .build();
        log.info("Local SNSAsync client was created at '[{}:{}]' .", resourceDetails.getHostname(), resourceDetails.getHttpPort());
        return amazonSNSAsync;
    }
}
