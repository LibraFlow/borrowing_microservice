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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllBorrowingsByUserUseCaseTest {

    @Mock
    private BorrowingRepository borrowingRepository;

    @Mock
    private BorrowingMapper borrowingMapper;

    @InjectMocks
    private GetAllBorrowingsByUserUseCase getAllBorrowingsByUserUseCase;

    private BorrowingDTO testBorrowingDTO1;
    private BorrowingDTO testBorrowingDTO2;
    private BorrowingEntity testBorrowingEntity1;
    private BorrowingEntity testBorrowingEntity2;
    private List<BorrowingEntity> testBorrowingEntities;
    private List<BorrowingDTO> expectedBorrowingDTOs;
    private Integer testUserId;

    @BeforeEach
    void setUp() {
        // Initialize test data
        testUserId = 100;
        LocalDate startDate1 = LocalDate.now();
        LocalDate endDate1 = LocalDate.now().plusDays(14);
        LocalDate startDate2 = LocalDate.now().minusDays(5);
        LocalDate endDate2 = LocalDate.now().plusDays(9);
        
        // Create first borrowing
        testBorrowingDTO1 = BorrowingDTO.builder()
                .id(1)
                .userId(testUserId)
                .bookUnitId(200)
                .shippingAddress("123 Test Street")
                .startDate(startDate1)
                .endDate(endDate1)
                .build();

        testBorrowingEntity1 = BorrowingEntity.builder()
                .id(1)
                .userId(testUserId)
                .bookUnitId(200)
                .shippingAddress("123 Test Street")
                .startDate(startDate1)
                .endDate(endDate1)
                .createdAt(LocalDate.now())
                .build();
        
        // Create second borrowing
        testBorrowingDTO2 = BorrowingDTO.builder()
                .id(2)
                .userId(testUserId)
                .bookUnitId(201)
                .shippingAddress("456 Another Street")
                .startDate(startDate2)
                .endDate(endDate2)
                .build();

        testBorrowingEntity2 = BorrowingEntity.builder()
                .id(2)
                .userId(testUserId)
                .bookUnitId(201)
                .shippingAddress("456 Another Street")
                .startDate(startDate2)
                .endDate(endDate2)
                .createdAt(LocalDate.now())
                .build();
        
        // Create lists for testing
        testBorrowingEntities = Arrays.asList(testBorrowingEntity1, testBorrowingEntity2);
        expectedBorrowingDTOs = Arrays.asList(testBorrowingDTO1, testBorrowingDTO2);
    }

    @Test
    void getAllBorrowingsByUser_Success() {
        // Arrange
        when(borrowingRepository.findByUserId(testUserId)).thenReturn(testBorrowingEntities);
        when(borrowingMapper.toDTO(testBorrowingEntity1)).thenReturn(testBorrowingDTO1);
        when(borrowingMapper.toDTO(testBorrowingEntity2)).thenReturn(testBorrowingDTO2);

        // Act
        List<BorrowingDTO> result = getAllBorrowingsByUserUseCase.getAllBorrowingsByUser(testUserId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testBorrowingDTO1.getId(), result.get(0).getId());
        assertEquals(testBorrowingDTO1.getUserId(), result.get(0).getUserId());
        assertEquals(testBorrowingDTO1.getBookUnitId(), result.get(0).getBookUnitId());
        assertEquals(testBorrowingDTO1.getShippingAddress(), result.get(0).getShippingAddress());
        assertEquals(testBorrowingDTO1.getStartDate(), result.get(0).getStartDate());
        assertEquals(testBorrowingDTO1.getEndDate(), result.get(0).getEndDate());
        assertEquals(testBorrowingDTO2.getId(), result.get(1).getId());
        assertEquals(testBorrowingDTO2.getUserId(), result.get(1).getUserId());
        assertEquals(testBorrowingDTO2.getBookUnitId(), result.get(1).getBookUnitId());
        assertEquals(testBorrowingDTO2.getShippingAddress(), result.get(1).getShippingAddress());
        assertEquals(testBorrowingDTO2.getStartDate(), result.get(1).getStartDate());
        assertEquals(testBorrowingDTO2.getEndDate(), result.get(1).getEndDate());

        // Verify interactions
        verify(borrowingRepository, times(1)).findByUserId(testUserId);
        verify(borrowingMapper, times(1)).toDTO(testBorrowingEntity1);
        verify(borrowingMapper, times(1)).toDTO(testBorrowingEntity2);
    }

    @Test
    void getAllBorrowingsByUser_NoBorrowingsFound() {
        // Arrange
        when(borrowingRepository.findByUserId(testUserId)).thenReturn(Collections.emptyList());

        // Act
        List<BorrowingDTO> result = getAllBorrowingsByUserUseCase.getAllBorrowingsByUser(testUserId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // Verify interactions
        verify(borrowingRepository, times(1)).findByUserId(testUserId);
        verifyNoInteractions(borrowingMapper);
    }

    @Test
    void getAllBorrowingsByUser_WithNullUserId_ShouldThrowException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            getAllBorrowingsByUserUseCase.getAllBorrowingsByUser(null);
        });

        // Verify no interactions with dependencies
        verifyNoInteractions(borrowingRepository);
        verifyNoInteractions(borrowingMapper);
    }
} 