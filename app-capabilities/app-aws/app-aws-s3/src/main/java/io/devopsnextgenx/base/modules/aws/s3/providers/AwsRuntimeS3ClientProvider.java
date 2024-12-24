package io.devopsnextgenx.base.modules.aws.s3.providers;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import io.devopsnextgenx.base.modules.aws.s3.config.AppS3Config;
import io.devopsnextgenx.base.modules.config.aws.AppAwsProperties;
import io.devopsnextgenx.base.modules.config.aws.exceptions.AppAwsResourceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * AwsRuntimeS3ClientProvider:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/15/2019
 */
@Slf4j
@RequiredArgsConstructor
public class AwsRuntimeS3ClientProvider implements AmazonS3ClientProvider {
    private final AWSCredentialsProvider credentials;
    private final AppAwsProperties awsProperties;

    @Override
    public AmazonS3 provide(AppS3Config config) throws AppAwsResourceException {
        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
                .withCredentials(credentials)
                .withRegion(Optional.ofNullable(awsProperties.getRegion())
                        .orElseThrow(() -> new AppAwsResourceException("Parameter 'appx.modules.s3.region' must be specified in integrative configuration")))
                .build();
        log.info("An integrative S3 client was created");
        return amazonS3;
    }
}
