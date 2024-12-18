package io.devopsnextgenx.base.modules.aws.sqs.providers;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import io.devopsnextgenx.base.modules.aws.sqs.config.AppSqsConfig;
import io.devopsnextgenx.base.modules.config.aws.exceptions.AppAwsResourceException;

public interface AmazonSQSClientProvider {
    AmazonSQS provide(AppSqsConfig config) throws AppAwsResourceException;
    AmazonSQSAsync provideAsync(AppSqsConfig config) throws AppAwsResourceException;
}
