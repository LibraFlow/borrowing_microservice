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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.junit.jupiter.api.AfterEach;
import java.util.Collections;

import java.time.LocalDate;
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
                .bookUnitId(1)
                .shippingAddress("123 Test Street")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 2, 1))
                .build();
    }

    private void mockAuthenticationWithRole(String role) {
        Authentication authentication = mock(Authentication.class);
        // Use raw cast to avoid generics issues
        when(authentication.getAuthorities()).thenReturn((java.util.Collection) Collections.singletonList(new SimpleGrantedAuthority(role)));
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void createBorrowingTest() {
        mockAuthenticationWithRole("ROLE_ADMINISTRATOR");
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
        mockAuthenticationWithRole("ROLE_ADMINISTRATOR");
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
        mockAuthenticationWithRole("ROLE_ADMINISTRATOR");
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