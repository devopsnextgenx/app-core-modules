import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

@Configuration
public class AwsConfiguration {
    @Value("${spring.cloud.aws.credentials.profile-name:default}")
    private String profileName;

    @Bean
    @ConditionalOnMissingBean
    public AwsCredentialsProvider awsCredentialsProvider() {
        return ProfileCredentialsProvider.create(); // Use a specific profile
    }
}
