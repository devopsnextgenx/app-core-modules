package io.devopsnextgenx.base.modules.aws.s3.providers;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import io.devopsnextgenx.base.modules.aws.s3.config.AppS3Config;
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
public class AwsLocalS3ClientProvider extends AppResourceClientProvider implements AmazonS3ClientProvider {
    @Override
    public AmazonS3 provide(AppS3Config config) throws AppAwsResourceException {
        AppResourceClientProvider.AppResource resourceDetails = getResourceDetails(config);

        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                        String.format("http://%s:%s", resourceDetails.getHostname(), resourceDetails.getHttpPort()),
                        config.getRegion())
                ).withPathStyleAccessEnabled(true)
                .build();
        log.info("Local S3 client was created at port '{}' .", resourceDetails.getHttpPort());
        return amazonS3;
    }
}
