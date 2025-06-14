package backend2.business.borrowing;

import backend2.persistence.entity.BorrowingEntity;
import backend2.domain.BorrowingDTO;
import backend2.persistence.BorrowingRepository;
import backend2.business.mapper.BorrowingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import backend2.domain.BorrowingCreatedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import backend2.config.RabbitMQConfig;

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
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public BorrowingDTO createBorrowing(BorrowingDTO borrowingDTO) {
        if (borrowingDTO == null) {
            throw new IllegalArgumentException("BorrowingDTO cannot be null");
        }
        
        // Save the borrowing to the database
        BorrowingEntity borrowingEntity = borrowingMapper.toEntity(borrowingDTO);
        BorrowingEntity savedBorrowing = borrowingRepository.save(borrowingEntity);

        // Log audit trail
        logger.info("AUDIT: Borrowing created - userId={}, bookUnitId={}, startDate={}, endDate={}",
            borrowingDTO.getUserId(),
            borrowingDTO.getBookUnitId(),
            borrowingDTO.getStartDate(),
            borrowingDTO.getEndDate());

        // Publish the event
        BorrowingCreatedEvent event = new BorrowingCreatedEvent(savedBorrowing.getBookUnitId());
        rabbitTemplate.convertAndSend(RabbitMQConfig.BORROWING_CREATED_QUEUE, event);

        // Return the saved DTO (with generated ID, etc.)
        return borrowingMapper.toDTO(savedBorrowing);
    }
}