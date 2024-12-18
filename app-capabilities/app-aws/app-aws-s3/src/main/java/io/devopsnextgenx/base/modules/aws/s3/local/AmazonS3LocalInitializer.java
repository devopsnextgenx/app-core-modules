package io.devopsnextgenx.base.modules.aws.s3.local;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.util.StringUtils;
import io.devopsnextgenx.base.modules.aws.s3.config.AppS3Config;
import io.devopsnextgenx.base.modules.config.aws.AwsResourceInitializer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * AmazonS3LocalInitializer:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/15/2019
 */
@Slf4j
@AllArgsConstructor
public class AmazonS3LocalInitializer implements AwsResourceInitializer {
    private final AppS3Config config;
    private final AmazonS3 amazonS3;

    @Override
    public void initializeResources() {
        doCreation();
    }

    public void doCreation() {
        String envPrefix = Optional.ofNullable(config.getEnvironmentPrefix())
                .map(env -> env + "-")
                .orElse(null);
        String bucketName = config.getDevelopment().getBucketName();

        if (StringUtils.isNullOrEmpty(bucketName))
            throw new RuntimeException("Package name must be specified in order to create the buckets for local S3");

        List<String> buckets = null;
        try {
            buckets = amazonS3.listBuckets().stream().map(Bucket::getName).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to list buckets from local S3, check if local S3 server is running:  {}", e.toString());
            throw new RuntimeException(String.format("Failed to list buckets from local S3, check if local S3 server is running:  %s", e.toString()));
        }

        for (String bucket : bucketName.split(":")) {
            bucket = String.format("%s%s", envPrefix, bucket);
            if (buckets.contains(bucketName)) {
                log.info("Bucket '{}' already created, Skipped bucket creation.", bucket);
                continue;
            }
            amazonS3.createBucket(bucket);
            log.info("{} bucket was created", bucket);
        }
    }

}
