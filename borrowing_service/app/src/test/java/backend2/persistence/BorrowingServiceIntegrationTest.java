package backend2.persistence;

import backend2.persistence.entity.BorrowingEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BorrowingServiceIntegrationTest {

    @Autowired
    private BorrowingRepository borrowingRepository;

    private BorrowingEntity testBorrowing;
    private LocalDate startDate;
    private LocalDate endDate;

    @BeforeEach
    void setUp() {
        // Clear the database before each test
        borrowingRepository.deleteAll();

        // Initialize test data
        startDate = LocalDate.now();
        endDate = LocalDate.now().plusDays(14);

        testBorrowing = BorrowingEntity.builder()
                .userId(100)
                .bookUnitId(200)
                .shippingAddress("123 Test Street")
                .startDate(startDate)
                .endDate(endDate)
                .active(true)
                .createdAt(LocalDate.now())
                .build();
    }

    @Test
    void addAndFetchBorrowing() {
        // Act
        BorrowingEntity savedBorrowing = borrowingRepository.save(testBorrowing);
        Optional<BorrowingEntity> foundBorrowing = borrowingRepository.findById(savedBorrowing.getId());

        // Assert
        assertTrue(foundBorrowing.isPresent());
        assertEquals(testBorrowing.getUserId(), foundBorrowing.get().getUserId());
        assertEquals(testBorrowing.getBookUnitId(), foundBorrowing.get().getBookUnitId());
        assertEquals(testBorrowing.getShippingAddress(), foundBorrowing.get().getShippingAddress());
        assertEquals(testBorrowing.getStartDate(), foundBorrowing.get().getStartDate());
        assertEquals(testBorrowing.getEndDate(), foundBorrowing.get().getEndDate());
        assertTrue(foundBorrowing.get().isActive());
    }

    @Test
    void updateBorrowing() {
        // Arrange
        BorrowingEntity savedBorrowing = borrowingRepository.save(testBorrowing);
        String newAddress = "456 New Street";
        LocalDate newEndDate = endDate.plusDays(7);

        // Act
        savedBorrowing.setShippingAddress(newAddress);
        savedBorrowing.setEndDate(newEndDate);
        borrowingRepository.save(savedBorrowing);
        Optional<BorrowingEntity> updatedBorrowing = borrowingRepository.findById(savedBorrowing.getId());

        // Assert
        assertTrue(updatedBorrowing.isPresent());
        assertEquals(newAddress, updatedBorrowing.get().getShippingAddress());
        assertEquals(newEndDate, updatedBorrowing.get().getEndDate());
    }

    @Test
    void deleteBorrowing() {
        // Arrange
        BorrowingEntity savedBorrowing = borrowingRepository.save(testBorrowing);

        // Act
        borrowingRepository.deleteById(savedBorrowing.getId());
        Optional<BorrowingEntity> deletedBorrowing = borrowingRepository.findById(savedBorrowing.getId());

        // Assert
        assertFalse(deletedBorrowing.isPresent());
    }

    @Test
    void findAllByUserId() {
        // Arrange
        BorrowingEntity borrowing1 = borrowingRepository.save(testBorrowing);
        BorrowingEntity borrowing2 = BorrowingEntity.builder()
                .userId(testBorrowing.getUserId())
                .bookUnitId(201)
                .shippingAddress("456 Another Street")
                .startDate(startDate)
                .endDate(endDate)
                .active(true)
                .createdAt(LocalDate.now())
                .build();
        borrowingRepository.save(borrowing2);

        // Act
        List<BorrowingEntity> userBorrowings = borrowingRepository.findByUserId(testBorrowing.getUserId());

        // Assert
        assertEquals(2, userBorrowings.size());
        assertTrue(userBorrowings.stream().allMatch(b -> b.getUserId().equals(testBorrowing.getUserId())));
    }

    @Test
    void findAllActiveBorrowings() {
        // Arrange
        BorrowingEntity activeBorrowing = borrowingRepository.save(testBorrowing);
        BorrowingEntity inactiveBorrowing = BorrowingEntity.builder()
                .userId(101)
                .bookUnitId(202)
                .shippingAddress("789 Third Street")
                .startDate(startDate)
                .endDate(endDate)
                .active(false)
                .createdAt(LocalDate.now())
                .build();
        borrowingRepository.save(inactiveBorrowing);

        // Act
        List<BorrowingEntity> activeBorrowings = borrowingRepository.findByUserIdAndActiveTrue(testBorrowing.getUserId());

        // Assert
        assertEquals(1, activeBorrowings.size());
        assertTrue(activeBorrowings.get(0).isActive());
    }

    @Test
    void saveInvalidBorrowingShouldFail() {
        // Arrange
        BorrowingEntity invalidBorrowing = BorrowingEntity.builder()
                .userId(100)
                .bookUnitId(200)
                .shippingAddress("123 Main Street")
                .startDate(startDate)
                .endDate(endDate)
                .active(true)
                .build();

        // Act & Assert
        assertThrows(Exception.class, () -> {
            borrowingRepository.save(invalidBorrowing);
        });
    }
} 