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
    void borrowingModificationIsLoggedInAuditTrail() {
        // Arrange
        BorrowingService borrowingService = mock(BorrowingService.class);
        AuditTrailService auditTrailService = mock(AuditTrailService.class);
        User user = new User("user1", "User");
        Borrowing borrowing = new Borrowing(1L, user, "book1");

        // Act
        borrowingService.addBorrowing(borrowing, user);
        verify(borrowingService).addBorrowing(borrowing, user);
        ArgumentCaptor<AuditTrailEntry> captor = ArgumentCaptor.forClass(AuditTrailEntry.class);
        verify(auditTrailService).log(captor.capture());
        AuditTrailEntry entry = captor.getValue();
        assertEquals("user1", entry.getUserId());
        assertEquals("ADD_BORROWING", entry.getAction());
        assertNotNull(entry.getTimestamp());
    }
} 