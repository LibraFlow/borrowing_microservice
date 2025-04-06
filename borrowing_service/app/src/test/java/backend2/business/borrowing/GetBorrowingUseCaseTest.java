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

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetBorrowingUseCaseTest {

    @Mock
    private BorrowingRepository borrowingRepository;

    @Mock
    private BorrowingMapper borrowingMapper;

    @InjectMocks
    private GetBorrowingUseCase getBorrowingUseCase;

    private BorrowingDTO testBorrowingDTO;
    private BorrowingEntity testBorrowingEntity;
    private Integer testBorrowingId;

    @BeforeEach
    void setUp() {
        // Initialize test data
        testBorrowingId = 1;
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(14);
        
        // Create borrowing DTO
        testBorrowingDTO = BorrowingDTO.builder()
                .id(testBorrowingId)
                .userId(100)
                .bookUnitId(200)
                .shippingAddress("123 Test Street")
                .startDate(startDate)
                .endDate(endDate)
                .build();

        // Create borrowing entity
        testBorrowingEntity = BorrowingEntity.builder()
                .id(testBorrowingId)
                .userId(100)
                .bookUnitId(200)
                .shippingAddress("123 Test Street")
                .startDate(startDate)
                .endDate(endDate)
                .createdAt(LocalDate.now())
                .build();
    }

    @Test
    void getBorrowing_Success() {
        // Arrange
        when(borrowingRepository.findById(testBorrowingId)).thenReturn(Optional.of(testBorrowingEntity));
        when(borrowingMapper.toDTO(testBorrowingEntity)).thenReturn(testBorrowingDTO);

        // Act
        BorrowingDTO result = getBorrowingUseCase.getBorrowing(testBorrowingId);

        // Assert
        assertNotNull(result);
        assertEquals(testBorrowingDTO.getId(), result.getId());
        assertEquals(testBorrowingDTO.getUserId(), result.getUserId());
        assertEquals(testBorrowingDTO.getBookUnitId(), result.getBookUnitId());
        assertEquals(testBorrowingDTO.getShippingAddress(), result.getShippingAddress());
        assertEquals(testBorrowingDTO.getStartDate(), result.getStartDate());
        assertEquals(testBorrowingDTO.getEndDate(), result.getEndDate());

        // Verify interactions
        verify(borrowingRepository, times(1)).findById(testBorrowingId);
        verify(borrowingMapper, times(1)).toDTO(testBorrowingEntity);
    }

    @Test
    void getBorrowing_NotFound_ShouldThrowException() {
        // Arrange
        when(borrowingRepository.findById(testBorrowingId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            getBorrowingUseCase.getBorrowing(testBorrowingId);
        });
        
        assertEquals("Borrowing not found with id: " + testBorrowingId, exception.getMessage());

        // Verify interactions
        verify(borrowingRepository, times(1)).findById(testBorrowingId);
        verifyNoInteractions(borrowingMapper);
    }

    @Test
    void getBorrowing_WithNullId_ShouldThrowException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            getBorrowingUseCase.getBorrowing(null);
        });

        // Verify no interactions with dependencies
        verifyNoInteractions(borrowingRepository);
        verifyNoInteractions(borrowingMapper);
    }
} 