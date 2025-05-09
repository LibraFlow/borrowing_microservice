package backend2.persistence.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BorrowingEntityTest {

    private BorrowingEntity borrowingEntity;

    @BeforeEach
    void setUp() {
        borrowingEntity = new BorrowingEntity();
    }

    @Test
    void testBorrowingEntityBuilder() {
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(14);
        LocalDate createdAt = LocalDate.now();
        
        BorrowingEntity builtEntity = BorrowingEntity.builder()
                .id(1)
                .userId(100)
                .bookUnitId(200)
                .shippingAddress("123 Test Street, Test City, 12345")
                .startDate(startDate)
                .endDate(endDate)
                .createdAt(createdAt)
                .build();

        assertNotNull(builtEntity);
        assertEquals(1, builtEntity.getId());
        assertEquals(100, builtEntity.getUserId());
        assertEquals(200, builtEntity.getBookUnitId());
        assertEquals("123 Test Street, Test City, 12345", builtEntity.getShippingAddress());
        assertEquals(startDate, builtEntity.getStartDate());
        assertEquals(endDate, builtEntity.getEndDate());
        assertEquals(createdAt, builtEntity.getCreatedAt());
    }

    @Test
    void testBorrowingEntitySettersAndGetters() {
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(14);
        LocalDate createdAt = LocalDate.now();

        borrowingEntity.setId(1);
        borrowingEntity.setUserId(100);
        borrowingEntity.setBookUnitId(200);
        borrowingEntity.setShippingAddress("123 Test Street, Test City, 12345");
        borrowingEntity.setStartDate(startDate);
        borrowingEntity.setEndDate(endDate);
        borrowingEntity.setCreatedAt(createdAt);

        assertEquals(1, borrowingEntity.getId());
        assertEquals(100, borrowingEntity.getUserId());
        assertEquals(200, borrowingEntity.getBookUnitId());
        assertEquals("123 Test Street, Test City, 12345", borrowingEntity.getShippingAddress());
        assertEquals(startDate, borrowingEntity.getStartDate());
        assertEquals(endDate, borrowingEntity.getEndDate());
        assertEquals(createdAt, borrowingEntity.getCreatedAt());
    }

    @Test
    void testBorrowingEntityEqualsAndHashCode() {
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(14);
        LocalDate createdAt = LocalDate.now();
        
        BorrowingEntity entity1 = BorrowingEntity.builder()
                .id(1)
                .userId(100)
                .bookUnitId(200)
                .shippingAddress("123 Test Street, Test City, 12345")
                .startDate(startDate)
                .endDate(endDate)
                .createdAt(createdAt)
                .build();

        BorrowingEntity entity2 = BorrowingEntity.builder()
                .id(1)
                .userId(100)
                .bookUnitId(200)
                .shippingAddress("123 Test Street, Test City, 12345")
                .startDate(startDate)
                .endDate(endDate)
                .createdAt(createdAt)
                .build();

        assertEquals(entity1, entity2);
        assertEquals(entity1.hashCode(), entity2.hashCode());
    }

    @Test
    void testBorrowingEntityToString() {
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(14);
        LocalDate createdAt = LocalDate.now();
        
        BorrowingEntity entity = BorrowingEntity.builder()
                .id(1)
                .userId(100)
                .bookUnitId(200)
                .shippingAddress("123 Test Street, Test City, 12345")
                .startDate(startDate)
                .endDate(endDate)
                .createdAt(createdAt)
                .build();

        String toString = entity.toString();
        
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("userId=100"));
        assertTrue(toString.contains("bookUnitId=200"));
        assertTrue(toString.contains("shippingAddress=123 Test Street, Test City, 12345"));
        assertTrue(toString.contains("startDate=" + startDate));
        assertTrue(toString.contains("endDate=" + endDate));
        assertTrue(toString.contains("createdAt=" + createdAt));
    }

    @Test
    void testBorrowingEntityNoArgsConstructor() {
        BorrowingEntity entity = new BorrowingEntity();
        assertNotNull(entity);
    }

    @Test
    void testBorrowingEntityAllArgsConstructor() {
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(14);
        LocalDate createdAt = LocalDate.now();
        
        BorrowingEntity entity = new BorrowingEntity(1, 100, 200, "123 Test Street, Test City, 12345", startDate, endDate, createdAt, true);

        assertEquals(1, entity.getId());
        assertEquals(100, entity.getUserId());
        assertEquals(200, entity.getBookUnitId());
        assertEquals("123 Test Street, Test City, 12345", entity.getShippingAddress());
        assertEquals(startDate, entity.getStartDate());
        assertEquals(endDate, entity.getEndDate());
        assertEquals(createdAt, entity.getCreatedAt());
    }
} 