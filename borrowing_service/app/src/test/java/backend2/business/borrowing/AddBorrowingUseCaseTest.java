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

    @BeforeEach
    void setUp() {
        // Initialize test data
        testBorrowingDTO = BorrowingDTO.builder()
                .id(1)
                .userId(100)
                .bookId(200)
                .borrowDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(14))
                .returnDate(null)
                .status("BORROWED")
                .build();

        testBorrowingEntity = new BorrowingEntity();
        testBorrowingEntity.setId(1);
        testBorrowingEntity.setUserId(100);
        testBorrowingEntity.setBookId(200);
        testBorrowingEntity.setBorrowDate(LocalDate.now());
        testBorrowingEntity.setDueDate(LocalDate.now().plusDays(14));
        testBorrowingEntity.setReturnDate(null);
        testBorrowingEntity.setStatus("BORROWED");

        savedBorrowingEntity = new BorrowingEntity();
        savedBorrowingEntity.setId(1);
        savedBorrowingEntity.setUserId(100);
        savedBorrowingEntity.setBookId(200);
        savedBorrowingEntity.setBorrowDate(LocalDate.now());
        savedBorrowingEntity.setDueDate(LocalDate.now().plusDays(14));
        savedBorrowingEntity.setReturnDate(null);
        savedBorrowingEntity.setStatus("BORROWED");
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
        assertEquals(testBorrowingDTO.getBookId(), result.getBookId());
        assertEquals(testBorrowingDTO.getBorrowDate(), result.getBorrowDate());
        assertEquals(testBorrowingDTO.getDueDate(), result.getDueDate());
        assertEquals(testBorrowingDTO.getReturnDate(), result.getReturnDate());
        assertEquals(testBorrowingDTO.getStatus(), result.getStatus());

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
} 