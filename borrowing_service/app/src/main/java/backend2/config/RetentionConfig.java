package backend2.config;

import org.springframework.context.annotation.Configuration;
import java.time.Duration;

@Configuration
public class RetentionConfig {
    // Retention periods in days
    public static final int BORROWING_HISTORY_RETENTION_DAYS = 730; // 2 years
} 