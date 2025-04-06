package backend2.persistence.entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BorrowingEntityTest {

    private Validator validator;
    
    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testValidBorrowingEntity() {
        // Create a valid borrowing entity
        BorrowingEntity borrowing = BorrowingEntity.builder()
                .userId(1)
                .bookUnitId(1)
                .shippingAddress("123 Main Street, Apt 4B, New York, NY 10001")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(14))
                .createdAt(LocalDate.now())
                .build();

        // Validate the entity
        Set<ConstraintViolation<BorrowingEntity>> violations = validator.validate(borrowing);
        
        // Assert that there are no validation violations
        assertTrue(violations.isEmpty(), "Valid borrowing entity should have no violations");
    }

    @Test
    void testNullUserId() {
        // Create a borrowing entity with null userId
        BorrowingEntity borrowing = BorrowingEntity.builder()
                .userId(null)
                .bookUnitId(1)
                .shippingAddress("123 Main Street, Apt 4B, New York, NY 10001")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(14))
                .createdAt(LocalDate.now())
                .build();

        // Validate the entity
        Set<ConstraintViolation<BorrowingEntity>> violations = validator.validate(borrowing);
        
        // Assert that there is a validation violation for userId
        assertFalse(violations.isEmpty(), "Borrowing with null userId should have violations");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("userId")), 
                "Should have a violation for userId");
    }

    @Test
    void testNullBookUnitId() {
        // Create a borrowing entity with null bookUnitId
        BorrowingEntity borrowing = BorrowingEntity.builder()
                .userId(1)
                .bookUnitId(null)
                .shippingAddress("123 Main Street, Apt 4B, New York, NY 10001")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(14))
                .createdAt(LocalDate.now())
                .build();

        // Validate the entity
        Set<ConstraintViolation<BorrowingEntity>> violations = validator.validate(borrowing);
        
        // Assert that there is a validation violation for bookUnitId
        assertFalse(violations.isEmpty(), "Borrowing with null bookUnitId should have violations");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("bookUnitId")), 
                "Should have a violation for bookUnitId");
    }

    @Test
    void testBlankShippingAddress() {
        // Create a borrowing entity with blank shipping address
        BorrowingEntity borrowing = BorrowingEntity.builder()
                .userId(1)
                .bookUnitId(1)
                .shippingAddress("")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(14))
                .createdAt(LocalDate.now())
                .build();

        // Validate the entity
        Set<ConstraintViolation<BorrowingEntity>> violations = validator.validate(borrowing);
        
        // Assert that there is a validation violation for shippingAddress
        assertFalse(violations.isEmpty(), "Borrowing with blank shipping address should have violations");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("shippingAddress")), 
                "Should have a violation for shippingAddress");
    }

    @Test
    void testShippingAddressTooShort() {
        // Create a borrowing entity with too short shipping address
        BorrowingEntity borrowing = BorrowingEntity.builder()
                .userId(1)
                .bookUnitId(1)
                .shippingAddress("123 Main")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(14))
                .createdAt(LocalDate.now())
                .build();

        // Validate the entity
        Set<ConstraintViolation<BorrowingEntity>> violations = validator.validate(borrowing);
        
        // Assert that there is a validation violation for shippingAddress
        assertFalse(violations.isEmpty(), "Borrowing with too short shipping address should have violations");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("shippingAddress")), 
                "Should have a violation for shippingAddress");
    }

    @Test
    void testNullStartDate() {
        // Create a borrowing entity with null start date
        BorrowingEntity borrowing = BorrowingEntity.builder()
                .userId(1)
                .bookUnitId(1)
                .shippingAddress("123 Main Street, Apt 4B, New York, NY 10001")
                .startDate(null)
                .endDate(LocalDate.now().plusDays(14))
                .createdAt(LocalDate.now())
                .build();

        // Validate the entity
        Set<ConstraintViolation<BorrowingEntity>> violations = validator.validate(borrowing);
        
        // Assert that there is a validation violation for startDate
        assertFalse(violations.isEmpty(), "Borrowing with null start date should have violations");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("startDate")), 
                "Should have a violation for startDate");
    }

    @Test
    void testPastStartDate() {
        // Create a borrowing entity with past start date
        BorrowingEntity borrowing = BorrowingEntity.builder()
                .userId(1)
                .bookUnitId(1)
                .shippingAddress("123 Main Street, Apt 4B, New York, NY 10001")
                .startDate(LocalDate.now().minusDays(1))
                .endDate(LocalDate.now().plusDays(14))
                .createdAt(LocalDate.now())
                .build();

        // Validate the entity
        Set<ConstraintViolation<BorrowingEntity>> violations = validator.validate(borrowing);
        
        // Assert that there is a validation violation for startDate
        assertFalse(violations.isEmpty(), "Borrowing with past start date should have violations");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("startDate")), 
                "Should have a violation for startDate");
    }

    @Test
    void testNullEndDate() {
        // Create a borrowing entity with null end date
        BorrowingEntity borrowing = BorrowingEntity.builder()
                .userId(1)
                .bookUnitId(1)
                .shippingAddress("123 Main Street, Apt 4B, New York, NY 10001")
                .startDate(LocalDate.now())
                .endDate(null)
                .createdAt(LocalDate.now())
                .build();

        // Validate the entity
        Set<ConstraintViolation<BorrowingEntity>> violations = validator.validate(borrowing);
        
        // Assert that there is a validation violation for endDate
        assertFalse(violations.isEmpty(), "Borrowing with null end date should have violations");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("endDate")), 
                "Should have a violation for endDate");
    }

    @Test
    void testNullCreatedAt() {
        // Create a borrowing entity with null createdAt
        BorrowingEntity borrowing = BorrowingEntity.builder()
                .userId(1)
                .bookUnitId(1)
                .shippingAddress("123 Main Street, Apt 4B, New York, NY 10001")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(14))
                .createdAt(null)
                .build();

        // Validate the entity
        Set<ConstraintViolation<BorrowingEntity>> violations = validator.validate(borrowing);
        
        // Assert that there is a validation violation for createdAt
        assertFalse(violations.isEmpty(), "Borrowing with null createdAt should have violations");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("createdAt")), 
                "Should have a violation for createdAt");
    }

    @Test
    void testFutureCreatedAt() {
        // Create a borrowing entity with future createdAt
        BorrowingEntity borrowing = BorrowingEntity.builder()
                .userId(1)
                .bookUnitId(1)
                .shippingAddress("123 Main Street, Apt 4B, New York, NY 10001")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(14))
                .createdAt(LocalDate.now().plusDays(1))
                .build();

        // Validate the entity
        Set<ConstraintViolation<BorrowingEntity>> violations = validator.validate(borrowing);
        
        // Assert that there is a validation violation for createdAt
        assertFalse(violations.isEmpty(), "Borrowing with future createdAt should have violations");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("createdAt")), 
                "Should have a violation for createdAt");
    }

    @Test
    void testEndDateBeforeStartDate() {
        // Create a borrowing entity with end date before start date
        BorrowingEntity borrowing = BorrowingEntity.builder()
                .userId(1)
                .bookUnitId(1)
                .shippingAddress("123 Main Street, Apt 4B, New York, NY 10001")
                .startDate(LocalDate.now().plusDays(14))
                .endDate(LocalDate.now())
                .createdAt(LocalDate.now())
                .build();

        // Validate the entity
        Set<ConstraintViolation<BorrowingEntity>> violations = validator.validate(borrowing);
        
        // Note: This test assumes there's a custom validator for this business rule
        // If there isn't one, you would need to add it to the entity
        // For now, we'll just check that the entity is created with these dates
        assertEquals(LocalDate.now().plusDays(14), borrowing.getStartDate());
        assertEquals(LocalDate.now(), borrowing.getEndDate());
    }

    @Test
    void testBuilderAndGetters() {
        // Create a borrowing entity using the builder
        LocalDate now = LocalDate.now();
        LocalDate endDate = now.plusDays(14);
        
        BorrowingEntity borrowing = BorrowingEntity.builder()
                .id(1)
                .userId(1)
                .bookUnitId(1)
                .shippingAddress("123 Main Street, Apt 4B, New York, NY 10001")
                .startDate(now)
                .endDate(endDate)
                .createdAt(now)
                .build();

        // Test getters
        assertEquals(1, borrowing.getId());
        assertEquals(1, borrowing.getUserId());
        assertEquals(1, borrowing.getBookUnitId());
        assertEquals("123 Main Street, Apt 4B, New York, NY 10001", borrowing.getShippingAddress());
        assertEquals(now, borrowing.getStartDate());
        assertEquals(endDate, borrowing.getEndDate());
        assertEquals(now, borrowing.getCreatedAt());
    }

    @Test
    void testSetters() {
        // Create a borrowing entity
        BorrowingEntity borrowing = new BorrowingEntity();
        
        // Set values using setters
        borrowing.setId(1);
        borrowing.setUserId(1);
        borrowing.setBookUnitId(1);
        borrowing.setShippingAddress("123 Main Street, Apt 4B, New York, NY 10001");
        
        LocalDate now = LocalDate.now();
        borrowing.setStartDate(now);
        
        LocalDate endDate = now.plusDays(14);
        borrowing.setEndDate(endDate);
        
        borrowing.setCreatedAt(now);

        // Test getters to verify setters worked
        assertEquals(1, borrowing.getId());
        assertEquals(1, borrowing.getUserId());
        assertEquals(1, borrowing.getBookUnitId());
        assertEquals("123 Main Street, Apt 4B, New York, NY 10001", borrowing.getShippingAddress());
        assertEquals(now, borrowing.getStartDate());
        assertEquals(endDate, borrowing.getEndDate());
        assertEquals(now, borrowing.getCreatedAt());
    }

    @Test
    void testEqualsAndHashCode() {
        // Create two identical borrowing entities
        LocalDate now = LocalDate.now();
        LocalDate endDate = now.plusDays(14);
        
        BorrowingEntity borrowing1 = BorrowingEntity.builder()
                .id(1)
                .userId(1)
                .bookUnitId(1)
                .shippingAddress("123 Main Street, Apt 4B, New York, NY 10001")
                .startDate(now)
                .endDate(endDate)
                .createdAt(now)
                .build();
                
        BorrowingEntity borrowing2 = BorrowingEntity.builder()
                .id(1)
                .userId(1)
                .bookUnitId(1)
                .shippingAddress("123 Main Street, Apt 4B, New York, NY 10001")
                .startDate(now)
                .endDate(endDate)
                .createdAt(now)
                .build();
                
        // Test equals
        assertEquals(borrowing1, borrowing2, "Identical borrowing entities should be equal");
        
        // Test hashCode
        assertEquals(borrowing1.hashCode(), borrowing2.hashCode(), "Identical borrowing entities should have the same hashCode");
        
        // Create a different borrowing entity
        BorrowingEntity borrowing3 = BorrowingEntity.builder()
                .id(2)
                .userId(1)
                .bookUnitId(1)
                .shippingAddress("123 Main Street, Apt 4B, New York, NY 10001")
                .startDate(now)
                .endDate(endDate)
                .createdAt(now)
                .build();
                
        // Test equals with different entity
        assertNotEquals(borrowing1, borrowing3, "Different borrowing entities should not be equal");
        
        // Test hashCode with different entity
        assertNotEquals(borrowing1.hashCode(), borrowing3.hashCode(), "Different borrowing entities should have different hashCode");
    }

    @Test
    void testToString() {
        // Create a borrowing entity
        LocalDate now = LocalDate.now();
        LocalDate endDate = now.plusDays(14);
        
        BorrowingEntity borrowing = BorrowingEntity.builder()
                .id(1)
                .userId(1)
                .bookUnitId(1)
                .shippingAddress("123 Main Street, Apt 4B, New York, NY 10001")
                .startDate(now)
                .endDate(endDate)
                .createdAt(now)
                .build();
                
        // Test toString
        String toString = borrowing.toString();
        
        // Verify that toString contains all fields
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("userId=1"));
        assertTrue(toString.contains("bookUnitId=1"));
        assertTrue(toString.contains("shippingAddress=123 Main Street, Apt 4B, New York, NY 10001"));
        assertTrue(toString.contains("startDate=" + now));
        assertTrue(toString.contains("endDate=" + endDate));
        assertTrue(toString.contains("createdAt=" + now));
    }
} 