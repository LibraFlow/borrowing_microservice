package backend2.business.borrowing;

import backend2.domain.BorrowingDTO;
import backend2.persistence.BorrowingRepository;
import backend2.persistence.entity.BorrowingEntity;
import backend2.business.mapper.BorrowingMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.ArgumentCaptor;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import backend2.config.RabbitMQConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.mockito.ArgumentMatchers;
import org.springframework.util.concurrent.SettableListenableFuture;
import java.util.concurrent.CompletableFuture;
import java.time.LocalDate;
import org.mockito.Mockito;
import static org.mockito.Mockito.lenient;
import backend2.domain.SubscriptionCheckResultEvent;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddBorrowingUseCaseTest {

    @Mock
    private BorrowingRepository borrowingRepository;

    @Mock
    private BorrowingMapper borrowingMapper;

    @InjectMocks
    private AddBorrowingUseCase addBorrowingUseCase;

    private BorrowingDTO testBorrowingDTO;
    private BorrowingEntity testBorrowingEntity;
    private BorrowingEntity savedBorrowingEntity;
    private ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;
    private String userServiceUrl = "http://localhost:8081";

    @BeforeEach
    void setUp() {
        // Initialize test data
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(14);
        
        // Create borrowing DTO
        testBorrowingDTO = BorrowingDTO.builder()
                .id(1)
                .userId(100)
                .bookUnitId(200)
                .shippingAddress("123 Test Street")
                .startDate(startDate)
                .endDate(endDate)
                .build();

        // Create borrowing entity
        testBorrowingEntity = BorrowingEntity.builder()
                .id(1)
                .userId(100)
                .bookUnitId(200)
                .shippingAddress("123 Test Street")
                .startDate(startDate)
                .endDate(endDate)
                .createdAt(LocalDate.now())
                .build();

        // Create saved borrowing entity
        savedBorrowingEntity = BorrowingEntity.builder()
                .id(1)
                .userId(100)
                .bookUnitId(200)
                .shippingAddress("123 Test Street")
                .startDate(startDate)
                .endDate(endDate)
                .createdAt(LocalDate.now())
                .build();

        // Re-instantiate addBorrowingUseCase with all required dependencies
        addBorrowingUseCase = new AddBorrowingUseCase(borrowingRepository, borrowingMapper, kafkaTemplate, objectMapper, userServiceUrl);
        // Make the KafkaTemplate.send mock lenient
        CompletableFuture<org.springframework.kafka.support.SendResult<String, String>> future = CompletableFuture.completedFuture(null);
        lenient().when(kafkaTemplate.send(anyString(), anyString())).thenReturn(future);
        // Simulate a successful subscription check result for the correlationId
        lenient().doAnswer(invocation -> {
            String topic = invocation.getArgument(0);
            String message = invocation.getArgument(1);
            // Extract correlationId from the message if possible
            if (topic != null && topic.contains("subscription.check.requested")) {
                // Use ObjectMapper to parse the message and get the correlationId
                try {
                    com.fasterxml.jackson.databind.JsonNode node = objectMapper.readTree(message);
                    String correlationId = node.get("correlationId").asText();
                    SubscriptionCheckResultEvent result = new SubscriptionCheckResultEvent(100, correlationId, true, LocalDate.now().plusDays(30));
                    CompletableFuture<SubscriptionCheckResultEvent> futureResult = AddBorrowingUseCase.subscriptionCheckFutures.get(correlationId);
                    if (futureResult != null) {
                        futureResult.complete(result);
                    }
                } catch (Exception ignored) {}
            }
            return future;
        }).when(kafkaTemplate).send(anyString(), anyString());
    }

    @Test
    void createBorrowing_Success() {
        // Arrange
        when(borrowingMapper.toEntity(any(BorrowingDTO.class))).thenReturn(testBorrowingEntity);
        when(borrowingRepository.save(any(BorrowingEntity.class))).thenReturn(savedBorrowingEntity);
        when(borrowingMapper.toDTO(any(BorrowingEntity.class))).thenReturn(testBorrowingDTO);

        // Act
        BorrowingDTO result = addBorrowingUseCase.createBorrowing(testBorrowingDTO);

        // Assert
        assertNotNull(result);
        assertEquals(testBorrowingDTO.getId(), result.getId());
        assertEquals(testBorrowingDTO.getUserId(), result.getUserId());
        assertEquals(testBorrowingDTO.getBookUnitId(), result.getBookUnitId());
        assertEquals(testBorrowingDTO.getShippingAddress(), result.getShippingAddress());
        assertEquals(testBorrowingDTO.getStartDate(), result.getStartDate());
        assertEquals(testBorrowingDTO.getEndDate(), result.getEndDate());

        // Verify interactions
        verify(borrowingMapper, times(1)).toEntity(testBorrowingDTO);
        verify(borrowingRepository, times(1)).save(testBorrowingEntity);
        verify(borrowingMapper, times(1)).toDTO(savedBorrowingEntity);
    }

    @Test
    void createBorrowing_WithNullInput_ShouldThrowException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            addBorrowingUseCase.createBorrowing(null);
        });

        // Verify no interactions with dependencies
        verifyNoInteractions(borrowingRepository);
        verifyNoInteractions(borrowingMapper);
    }

    @Test
    void createBorrowing_ShouldLogAuditTrail() {
        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(AddBorrowingUseCase.class);
        @SuppressWarnings("unchecked")
        Appender<ILoggingEvent> mockAppender = mock(Appender.class);
        logger.addAppender(mockAppender);

        when(borrowingMapper.toEntity(any(BorrowingDTO.class))).thenReturn(testBorrowingEntity);
        when(borrowingRepository.save(any(BorrowingEntity.class))).thenReturn(savedBorrowingEntity);
        when(borrowingMapper.toDTO(any(BorrowingEntity.class))).thenReturn(testBorrowingDTO);

        // Act
        addBorrowingUseCase.createBorrowing(testBorrowingDTO);

        // Assert: verify that the audit log message was produced
        verify(mockAppender, atLeastOnce()).doAppend(argThat(event ->
            event.getFormattedMessage().contains("AUDIT: Borrowing created") &&
            event.getFormattedMessage().contains("userId=" + testBorrowingDTO.getUserId())
        ));
        logger.detachAppender(mockAppender);
    }
} 