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
public class GetAllBorrowingsByBookUnitIdUseCase {
    private final BorrowingRepository borrowingRepository;
    private final BorrowingMapper borrowingMapper;

    @Transactional
    public List<BorrowingDTO> getAllBorrowingsByBookUnitId(Integer bookUnitId) {
        return borrowingRepository.findByBookUnitId(bookUnitId)
            .stream()
            .map(borrowingMapper::toDTO)
            .collect(Collectors.toList());
    }
}