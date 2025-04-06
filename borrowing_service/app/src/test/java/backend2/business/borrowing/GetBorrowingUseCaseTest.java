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
        
        // Create borrowing DTO
        testBorrowingDTO = BorrowingDTO.builder()
                .id(testBorrowingId)
                .userId(100)
                .bookId(200)
                .borrowDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(14))
                .returnDate(null)
                .status("BORROWED")
                .build();

        // Create borrowing entity
        testBorrowingEntity = new BorrowingEntity();
        testBorrowingEntity.setId(testBorrowingId);
        testBorrowingEntity.setUserId(100);
        testBorrowingEntity.setBookId(200);
        testBorrowingEntity.setBorrowDate(LocalDate.now());
        testBorrowingEntity.setDueDate(LocalDate.now().plusDays(14));
        testBorrowingEntity.setReturnDate(null);
        testBorrowingEntity.setStatus("BORROWED");
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
        assertEquals(testBorrowingDTO.getBookId(), result.getBookId());
        assertEquals(testBorrowingDTO.getBorrowDate(), result.getBorrowDate());
        assertEquals(testBorrowingDTO.getDueDate(), result.getDueDate());
        assertEquals(testBorrowingDTO.getReturnDate(), result.getReturnDate());
        assertEquals(testBorrowingDTO.getStatus(), result.getStatus());

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