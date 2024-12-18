package io.devopsnextgenx.base.modules.aws.sqs.providers;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import io.devopsnextgenx.base.modules.aws.sqs.config.AppSqsConfig;
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
public class AwsRuntimeSQSClientProvider implements AmazonSQSClientProvider {
    private final AWSCredentialsProvider credentials;
    private final AppAwsProperties awsProperties;

    @Override
    public AmazonSQS provide(AppSqsConfig config) throws AppAwsResourceException {
        AmazonSQS amazonSQS = AmazonSQSClientBuilder.standard()
                .withCredentials(credentials)
                .withRegion(Optional.ofNullable(awsProperties.getRegion())
                        .orElseThrow(() -> new AppAwsResourceException("Parameter 'app.modules.s3.region' must be specified in integrative configuration")))
                .build();
        log.info("An integrative SQS client was created");
        return amazonSQS;
    }

    @Override
    public AmazonSQSAsync provideAsync(AppSqsConfig config) throws AppAwsResourceException {
        AmazonSQSAsync amazonSQSAsync = AmazonSQSAsyncClientBuilder.standard()
                .withCredentials(credentials)
                .withRegion(Optional.ofNullable(awsProperties.getRegion())
                        .orElseThrow(() -> new AppAwsResourceException("Parameter 'app.modules.s3.region' must be specified in integrative configuration")))

                .build();
        log.info("An integrative SQSAsync client was created");
        return amazonSQSAsync;
    }

//    @Override
//    public SqsAsyncClient provideSqsAsyncClient(AppSqsConfig config) throws AppAwsResourceException {
//        SqsAsyncClient amazonSQSAsync = SqsAsyncClient
//                .builder()
//                .region(Region.of(awsProperties.getRegion()))
//                .credentialsProvider(new AwsCredentialsProvider() {
//                    @Override
//                    public AwsCredentials resolveCredentials() {
//                        AwsCredentials awsCredentials = new AwsCredentials() {
//                            @Override
//                            public String accessKeyId() {
//                                return credentials.getCredentials().getAWSAccessKeyId();
//                            }
//
//                            @Override
//                            public String secretAccessKey() {
//                                return credentials.getCredentials().getAWSSecretKey();
//                            }
//                        };
//                        return awsCredentials;
//                    }
//                })
//                .build();
//        log.info("An integrative SQSAsync client was created");
//        return amazonSQSAsync;
//    }
}
