package volunteer.plus.backend.config.storage;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("aws-s3")
public class AWSProperties {
    // default bucket for app (created one only for economy purposes)
    private String reportBucketName;
}
