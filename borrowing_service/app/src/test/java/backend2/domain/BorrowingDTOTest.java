package backend2.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

class BorrowingDTOTest {

    @Test
    void testBorrowingDTOBuilder() {
        // Arrange & Act
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 2, 1);
        
        BorrowingDTO borrowingDTO = BorrowingDTO.builder()
                .id(1)
                .userId(100)
                .bookUnitId(200)
                .shippingAddress("123 Test Street")
                .startDate(startDate)
                .endDate(endDate)
                .build();

        // Assert
        assertNotNull(borrowingDTO);
        assertEquals(1, borrowingDTO.getId());
        assertEquals(100, borrowingDTO.getUserId());
        assertEquals(200, borrowingDTO.getBookUnitId());
        assertEquals("123 Test Street", borrowingDTO.getShippingAddress());
        assertEquals(startDate, borrowingDTO.getStartDate());
        assertEquals(endDate, borrowingDTO.getEndDate());
    }

    @Test
    void testBorrowingDTOAllArgsConstructor() {
        // Arrange & Act
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 2, 1);
        
        BorrowingDTO borrowingDTO = new BorrowingDTO(1, 100, 200, "123 Test Street", startDate, endDate);

        // Assert
        assertNotNull(borrowingDTO);
        assertEquals(1, borrowingDTO.getId());
        assertEquals(100, borrowingDTO.getUserId());
        assertEquals(200, borrowingDTO.getBookUnitId());
        assertEquals("123 Test Street", borrowingDTO.getShippingAddress());
        assertEquals(startDate, borrowingDTO.getStartDate());
        assertEquals(endDate, borrowingDTO.getEndDate());
    }

    @Test
    void testBorrowingDTOEqualsAndHashCode() {
        // Arrange
        LocalDate startDate1 = LocalDate.of(2023, 1, 1);
        LocalDate endDate1 = LocalDate.of(2023, 2, 1);
        
        BorrowingDTO borrowingDTO1 = BorrowingDTO.builder()
                .id(1)
                .userId(100)
                .bookUnitId(200)
                .shippingAddress("123 Test Street")
                .startDate(startDate1)
                .endDate(endDate1)
                .build();

        BorrowingDTO borrowingDTO2 = BorrowingDTO.builder()
                .id(1)
                .userId(100)
                .bookUnitId(200)
                .shippingAddress("123 Test Street")
                .startDate(startDate1)
                .endDate(endDate1)
                .build();

        LocalDate startDate2 = LocalDate.of(2023, 3, 1);
        LocalDate endDate2 = LocalDate.of(2023, 4, 1);
        
        BorrowingDTO borrowingDTO3 = BorrowingDTO.builder()
                .id(2)
                .userId(101)
                .bookUnitId(201)
                .shippingAddress("456 Different Street")
                .startDate(startDate2)
                .endDate(endDate2)
                .build();

        // Assert
        assertEquals(borrowingDTO1, borrowingDTO2);
        assertNotEquals(borrowingDTO1, borrowingDTO3);
        assertEquals(borrowingDTO1.hashCode(), borrowingDTO2.hashCode());
        assertNotEquals(borrowingDTO1.hashCode(), borrowingDTO3.hashCode());
    }

    @Test
    void testBorrowingDTOToString() {
        // Arrange
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 2, 1);
        
        BorrowingDTO borrowingDTO = BorrowingDTO.builder()
                .id(1)
                .userId(100)
                .bookUnitId(200)
                .shippingAddress("123 Test Street")
                .startDate(startDate)
                .endDate(endDate)
                .build();

        // Act
        String toString = borrowingDTO.toString();

        // Assert
        assertNotNull(toString);
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("userId=100"));
        assertTrue(toString.contains("bookUnitId=200"));
        assertTrue(toString.contains("shippingAddress=123 Test Street"));
        assertTrue(toString.contains("startDate=" + startDate));
        assertTrue(toString.contains("endDate=" + endDate));
    }
} 