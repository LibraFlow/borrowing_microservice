package backend2.business.retention;

import backend2.config.RetentionConfig;
import backend2.persistence.BorrowingRepository;
import backend2.persistence.entity.BorrowingEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataRetentionService {
    private final BorrowingRepository borrowingRepository;

    @Scheduled(cron = "0 0 0 * * ?") // Run at midnight every day
    @Transactional
    public void cleanupExpiredData() {
        LocalDate borrowingRetentionDate = LocalDate.now().minusDays(RetentionConfig.BORROWING_HISTORY_RETENTION_DAYS);

        // Clean up expired borrowings
        List<BorrowingEntity> expiredBorrowings = borrowingRepository.findByEndDateBeforeAndActiveFalse(borrowingRetentionDate);
        for (BorrowingEntity borrowing : expiredBorrowings) {
            borrowingRepository.delete(borrowing);
        }
    }
} 