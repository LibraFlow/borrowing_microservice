package backend2.business.borrowing;

import backend2.domain.BorrowingDTO;
import backend2.persistence.BorrowingRepository;
import backend2.business.mapper.BorrowingMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetInactiveBorrowingsByUserUseCase {
    
    private final BorrowingRepository borrowingRepository;
    private final BorrowingMapper borrowingMapper;

    @Transactional
    public List<BorrowingDTO> getInactiveBorrowingsByUser(Integer userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        
        return borrowingRepository.findByUserIdAndActiveFalse(userId)
                .stream()
                .map(borrowingMapper::toDTO)
                .collect(Collectors.toList());
    }
} 