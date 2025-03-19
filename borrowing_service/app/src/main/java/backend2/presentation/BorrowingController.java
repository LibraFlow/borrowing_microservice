package backend2.presentation;

import backend2.domain.BorrowingDTO;
import backend2.business.borrowing.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/borrowings")
@RequiredArgsConstructor
public class BorrowingController {

    private final AddBorrowingUseCase addBorrowingUseCase;
    private final GetBorrowingUseCase getBorrowingUseCase;
    private final GetAllBorrowingsByUserUseCase getAllBorrowingsByUserUseCase;

    @PostMapping
    public ResponseEntity<BorrowingDTO> createBorrowing(@Valid @RequestBody BorrowingDTO borrowingDto) {
        return ResponseEntity.ok(addBorrowingUseCase.createBorrowing(borrowingDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BorrowingDTO> getBorrowing(@PathVariable Integer id) {
        return ResponseEntity.ok(getBorrowingUseCase.getBorrowing(id));
    }

    @GetMapping
    public ResponseEntity<List<BorrowingDTO>> getAllBorrowingsByUser(@RequestParam Integer userId) {
        return ResponseEntity.ok(getAllBorrowingsByUserUseCase.getAllBorrowingsByUser(userId));
    }
}