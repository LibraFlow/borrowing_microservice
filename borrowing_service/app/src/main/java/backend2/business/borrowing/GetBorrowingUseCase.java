package backend2.business.borrowing;

import backend2.domain.BorrowingDTO;
import backend2.persistence.BorrowingRepository;
import backend2.business.mapper.BorrowingMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetBorrowingUseCase {

    private final BorrowingRepository borrowingRepository;
    private final BorrowingMapper borrowingMapper;

    @Transactional
    public BorrowingDTO getBorrowing(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Borrowing ID cannot be null");
        }
        
        return borrowingRepository.findById(id)
                .map(borrowingMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Borrowing not found with id: " + id));
    }
}