package backend2.business.borrowing;

import backend2.domain.SubscriptionCheckResultEvent;
import backend2.config.KafkaConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentMap;

@Component
@RequiredArgsConstructor
public class SubscriptionCheckResultListener {
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = KafkaConfig.SUBSCRIPTION_CHECK_RESULT_TOPIC, groupId = "libraflow-group")
    public void handleSubscriptionCheckResult(String json) {
        try {
            SubscriptionCheckResultEvent event = objectMapper.readValue(json, SubscriptionCheckResultEvent.class);
            ConcurrentMap<String, CompletableFuture<SubscriptionCheckResultEvent>> futures = AddBorrowingUseCase.subscriptionCheckFutures;
            CompletableFuture<SubscriptionCheckResultEvent> future = futures.get(event.getCorrelationId());
            if (future != null) {
                future.complete(event);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 