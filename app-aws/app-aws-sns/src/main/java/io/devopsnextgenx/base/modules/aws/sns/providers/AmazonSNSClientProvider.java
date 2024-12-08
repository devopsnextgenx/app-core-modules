package io.devopsnextgenx.base.modules.aws.sns.providers;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSAsync;
import io.devopsnextgenx.base.modules.aws.sns.config.AppSnsConfig;
import io.devopsnextgenx.base.modules.config.aws.exceptions.AppAwsResourceException;

public interface AmazonSNSClientProvider {
    AmazonSNS provide(AppSnsConfig config) throws AppAwsResourceException;
    AmazonSNSAsync provideAsync(AppSnsConfig config) throws AppAwsResourceException;
}
