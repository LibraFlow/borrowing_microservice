package backend2.business.borrowing;

import backend2.persistence.entity.BorrowingEntity;
import backend2.domain.BorrowingDTO;
import backend2.persistence.BorrowingRepository;
import backend2.business.mapper.BorrowingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.owasp.encoder.Encode;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AddBorrowingUseCase {
    private final BorrowingRepository borrowingRepository;
    private final BorrowingMapper borrowingMapper;
    private final Logger logger = LoggerFactory.getLogger(AddBorrowingUseCase.class);

    @Transactional
    public BorrowingDTO createBorrowing(BorrowingDTO borrowingDTO) {
        if (borrowingDTO == null) {
            throw new IllegalArgumentException("BorrowingDTO cannot be null");
        }
        
        BorrowingEntity borrowingEntity = borrowingMapper.toEntity(borrowingDTO);
        BorrowingEntity savedBorrowing = borrowingRepository.save(borrowingEntity);
        // Audit log: userId and timestamp
        logger.info("AUDIT: Borrowing created - userId={}, borrowingId={}, timestamp={}",
            borrowingDTO.getUserId(), savedBorrowing.getId(), java.time.Instant.now());
        return borrowingMapper.toDTO(savedBorrowing);
    }
}