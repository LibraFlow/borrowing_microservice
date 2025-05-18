package backend2.business.mapper;

import backend2.domain.BorrowingDTO;
import backend2.persistence.entity.BorrowingEntity;
import backend2.security.EncryptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BorrowingMapperTest {

    @Mock
    private EncryptionService encryptionService;

    private BorrowingMapper borrowingMapper;

    private BorrowingEntity testEntity;
    private BorrowingDTO testDTO;
    private final LocalDate TODAY = LocalDate.now();
    private final LocalDate TOMORROW = TODAY.plusDays(1);
    private final LocalDate NEXT_WEEK = TODAY.plusDays(7);

    @BeforeEach
    void setUp() {
        borrowingMapper = new BorrowingMapper(encryptionService);

        testEntity = BorrowingEntity.builder()
                .id(1)
                .userId(100)
                .bookUnitId(200)
                .shippingAddress("123 Test Street, Test City")
                .startDate(TOMORROW)
                .endDate(NEXT_WEEK)
                .createdAt(TODAY)
                .active(true)
                .build();

        testDTO = BorrowingDTO.builder()
                .id(1)
                .userId(100)
                .bookUnitId(200)
                .shippingAddress("123 Test Street, Test City")
                .startDate(TOMORROW)
                .endDate(NEXT_WEEK)
                .active(true)
                .build();
    }

    @Test
    void toDTO_WhenEntityIsNull_ShouldReturnNull() {
        // Act
        BorrowingDTO result = borrowingMapper.toDTO(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toDTO_WhenEntityIsValid_ShouldMapAllFields() {
        // Arrange
        when(encryptionService.decrypt(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
        // Act
        BorrowingDTO result = borrowingMapper.toDTO(testEntity);

        // Assert
        assertNotNull(result);
        assertEquals(testEntity.getId(), result.getId());
        assertEquals(testEntity.getUserId(), result.getUserId());
        assertEquals(testEntity.getBookUnitId(), result.getBookUnitId());
        assertEquals(testEntity.getShippingAddress(), result.getShippingAddress());
        assertEquals(testEntity.getStartDate(), result.getStartDate());
        assertEquals(testEntity.getEndDate(), result.getEndDate());
    }

    @Test
    void toEntity_WhenDTOIsNull_ShouldReturnNull() {
        // Act
        BorrowingEntity result = borrowingMapper.toEntity(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toEntity_WhenDTOIsValid_ShouldMapAllFields() {
        // Arrange
        when(encryptionService.encrypt(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
        // Act
        BorrowingEntity result = borrowingMapper.toEntity(testDTO);

        // Assert
        assertNotNull(result);
        assertEquals(testDTO.getId(), result.getId());
        assertEquals(testDTO.getUserId(), result.getUserId());
        assertEquals(testDTO.getBookUnitId(), result.getBookUnitId());
        assertEquals(testDTO.getShippingAddress(), result.getShippingAddress());
        assertEquals(testDTO.getStartDate(), result.getStartDate());
        assertEquals(testDTO.getEndDate(), result.getEndDate());
    }

    @Test
    void toEntity_WhenDTOHasNoId_ShouldSetCreatedAtToToday() {
        // Arrange
        testDTO.setId(null);
        when(encryptionService.encrypt(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
        // Act
        BorrowingEntity result = borrowingMapper.toEntity(testDTO);

        // Assert
        assertNotNull(result);
        assertEquals(TODAY, result.getCreatedAt());
    }

    @Test
    void toEntity_WhenDTOHasId_ShouldNotSetCreatedAt() {
        // Arrange
        when(encryptionService.encrypt(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
        // Act
        BorrowingEntity result = borrowingMapper.toEntity(testDTO);

        // Assert
        assertNotNull(result);
        assertNull(result.getCreatedAt());
    }
} 