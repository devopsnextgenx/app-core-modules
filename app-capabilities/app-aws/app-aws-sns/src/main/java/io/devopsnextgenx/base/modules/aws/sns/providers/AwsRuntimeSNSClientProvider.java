package io.devopsnextgenx.base.modules.aws.sns.providers;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSAsync;
import com.amazonaws.services.sns.AmazonSNSAsyncClientBuilder;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import io.devopsnextgenx.base.modules.aws.sns.config.AppSnsConfig;
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
public class AwsRuntimeSNSClientProvider implements AmazonSNSClientProvider {
    private final AWSCredentialsProvider credentials;
    private final AppAwsProperties awsProperties;

    @Override
    public AmazonSNS provide(AppSnsConfig config) throws AppAwsResourceException {
        AmazonSNS amazonSns = AmazonSNSClientBuilder.standard()
                .withCredentials(credentials)
                .withRegion(Optional.ofNullable(awsProperties.getRegion())
                        .orElseThrow(() -> new AppAwsResourceException("Parameter 'appx.modules.s3.region' must be specified in integrative configuration")))
                .build();
        log.info("An integrative SNS client was created");
        return amazonSns;
    }

    @Override
    public AmazonSNSAsync provideAsync(AppSnsConfig config) throws AppAwsResourceException {
        AmazonSNSAsync amazonSnsAsync = AmazonSNSAsyncClientBuilder.standard()
                .withCredentials(credentials)
                .withRegion(Optional.ofNullable(awsProperties.getRegion())
                        .orElseThrow(() -> new AppAwsResourceException("Parameter 'appx.modules.s3.region' must be specified in integrative configuration")))

                .build();
        log.info("An integrative SNSAsync client was created");
        return amazonSnsAsync;
    }

//    @Override
//    public AmazonCloudFormation provideCloudFormation(AppSnsConfig config) throws AppAwsResourceException {
//        AmazonCloudFormation amazonCloudFormation = AmazonCloudFormationClientBuilder.standard()
//                .withCredentials(credentials)
//                .withRegion(Optional.ofNullable(awsProperties.getRegion())
//                        .orElseThrow(() -> new AppAwsResourceException("Parameter 'appx.modules.s3.region' must be specified in integrative configuration")))
//
//                .build();
//        log.info("Local amazonCloudFormation client was created.");
//        return amazonCloudFormation;
//    }

}
