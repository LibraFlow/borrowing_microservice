package backend2.presentation;

import backend2.domain.BorrowingDTO;
import backend2.business.borrowing.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BorrowingControllerTest {

    @Mock
    private AddBorrowingUseCase addBorrowingUseCase;

    @Mock
    private GetBorrowingUseCase getBorrowingUseCase;

    @Mock
    private GetAllBorrowingsByUserUseCase getAllBorrowingsByUserUseCase;

    @InjectMocks
    private BorrowingController borrowingController;

    private BorrowingDTO testBorrowingDTO;

    @BeforeEach
    void setUp() {
        testBorrowingDTO = BorrowingDTO.builder()
                .id(1)
                .userId(1)
                .bookId(1)
                .borrowDate("2024-03-20")
                .returnDate("2024-04-20")
                .build();
    }

    @Test
    void createBorrowingTest() {
        // Arrange
        when(addBorrowingUseCase.createBorrowing(any(BorrowingDTO.class))).thenReturn(testBorrowingDTO);

        // Act
        ResponseEntity<BorrowingDTO> response = borrowingController.createBorrowing(testBorrowingDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testBorrowingDTO, response.getBody());
        verify(addBorrowingUseCase, times(1)).createBorrowing(any(BorrowingDTO.class));
    }

    @Test
    void getBorrowingTest() {
        // Arrange
        Integer borrowingId = 1;
        when(getBorrowingUseCase.getBorrowing(anyInt())).thenReturn(testBorrowingDTO);

        // Act
        ResponseEntity<BorrowingDTO> response = borrowingController.getBorrowing(borrowingId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testBorrowingDTO, response.getBody());
        verify(getBorrowingUseCase, times(1)).getBorrowing(borrowingId);
    }

    @Test
    void getAllBorrowingsByUserTest() {
        // Arrange
        Integer userId = 1;
        List<BorrowingDTO> expectedBorrowings = Arrays.asList(testBorrowingDTO);
        when(getAllBorrowingsByUserUseCase.getAllBorrowingsByUser(anyInt())).thenReturn(expectedBorrowings);

        // Act
        ResponseEntity<List<BorrowingDTO>> response = borrowingController.getAllBorrowingsByUser(userId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBorrowings, response.getBody());
        assertEquals(1, response.getBody().size());
        verify(getAllBorrowingsByUserUseCase, times(1)).getAllBorrowingsByUser(userId);
    }
} 