package io.devopsnextgenx.base.modules.aws.s3.providers;

import com.amazonaws.services.s3.AmazonS3;
import io.devopsnextgenx.base.modules.aws.s3.config.AppS3Config;
import io.devopsnextgenx.base.modules.config.aws.exceptions.AppAwsResourceException;

/**
 * AmazonS3ClientProvider:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/15/2019
 */
public interface AmazonS3ClientProvider {
    AmazonS3 provide(AppS3Config config) throws AppAwsResourceException;
}
