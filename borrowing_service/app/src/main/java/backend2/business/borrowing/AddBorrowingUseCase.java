package backend2.business.borrowing;

import backend2.persistence.entity.BorrowingEntity;
import backend2.domain.BorrowingDTO;
import backend2.persistence.BorrowingRepository;
import backend2.business.mapper.BorrowingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import backend2.domain.BorrowingCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import backend2.config.KafkaConfig;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import org.owasp.encoder.Encode;
import org.springframework.transaction.annotation.Transactional;
import backend2.domain.SubscriptionCheckRequestedEvent;
import backend2.domain.SubscriptionCheckResultEvent;
import java.util.UUID;
import java.util.concurrent.*;

@Service
public class AddBorrowingUseCase {
    private final BorrowingRepository borrowingRepository;
    private final BorrowingMapper borrowingMapper;
    private final Logger logger = LoggerFactory.getLogger(AddBorrowingUseCase.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final WebClient webClient;
    private final String userServiceUrl;
    static final ConcurrentMap<String, CompletableFuture<SubscriptionCheckResultEvent>> subscriptionCheckFutures = new ConcurrentHashMap<>();

    public AddBorrowingUseCase(BorrowingRepository borrowingRepository, BorrowingMapper borrowingMapper, KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper, @Value("${user.service.url:http://localhost:8081}") String userServiceUrl) {
        this.borrowingRepository = borrowingRepository;
        this.borrowingMapper = borrowingMapper;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.userServiceUrl = userServiceUrl;
        this.webClient = WebClient.builder().baseUrl(userServiceUrl).build();
    }

    @Transactional
    public BorrowingDTO createBorrowing(BorrowingDTO borrowingDTO) {
        if (borrowingDTO == null) {
            throw new IllegalArgumentException("BorrowingDTO cannot be null");
        }
        
        // Kafka-based subscription check
        String correlationId = UUID.randomUUID().toString();
        SubscriptionCheckRequestedEvent event = new SubscriptionCheckRequestedEvent(borrowingDTO.getUserId(), correlationId);
        CompletableFuture<SubscriptionCheckResultEvent> future = new CompletableFuture<>();
        subscriptionCheckFutures.put(correlationId, future);
        try {
            kafkaTemplate.send(KafkaConfig.SUBSCRIPTION_CHECK_REQUESTED_TOPIC, objectMapper.writeValueAsString(event));
        } catch (Exception e) {
            subscriptionCheckFutures.remove(correlationId);
            throw new RuntimeException("Failed to send subscription check request", e);
        }
        SubscriptionCheckResultEvent result;
        try {
            result = future.get(5, TimeUnit.SECONDS); // Wait for up to 5 seconds
        } catch (Exception e) {
            subscriptionCheckFutures.remove(correlationId);
            throw new RuntimeException("Timed out waiting for subscription check result", e);
        }
        subscriptionCheckFutures.remove(correlationId);
        if (!Boolean.TRUE.equals(result.isHasActiveSubscription())) {
            throw new IllegalStateException("User does not have an active subscription and cannot borrow books.");
        }
        if (result.getSubscriptionEndDate() == null || borrowingDTO.getEndDate() == null || result.getSubscriptionEndDate().isBefore(borrowingDTO.getEndDate())) {
            throw new IllegalStateException("User's subscription does not cover the entire borrowing period.");
        }

        // Save the borrowing to the database
        BorrowingEntity borrowingEntity = borrowingMapper.toEntity(borrowingDTO);
        BorrowingEntity savedBorrowing = borrowingRepository.save(borrowingEntity);

        // Log audit trail
        logger.info("AUDIT: Borrowing created - userId={}, bookUnitId={}, startDate={}, endDate={}",
            borrowingDTO.getUserId(),
            borrowingDTO.getBookUnitId(),
            borrowingDTO.getStartDate(),
            borrowingDTO.getEndDate());

        // Publish the event
        BorrowingCreatedEvent borrowingCreatedEvent = new BorrowingCreatedEvent(savedBorrowing.getBookUnitId());
        try {
            kafkaTemplate.send(KafkaConfig.BORROWING_CREATED_TOPIC, objectMapper.writeValueAsString(borrowingCreatedEvent));
        } catch (Exception e) {
            logger.error("Failed to serialize or send BorrowingCreatedEvent to Kafka", e);
        }

        // Return the saved DTO (with generated ID, etc.)
        return borrowingMapper.toDTO(savedBorrowing);
    }
}