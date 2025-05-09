package backend2.presentation;

import backend2.domain.BorrowingDTO;
import backend2.business.borrowing.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

@RestController
@RequestMapping("api/v1/borrowings")
@RequiredArgsConstructor
public class BorrowingController {

    private final AddBorrowingUseCase addBorrowingUseCase;
    private final GetBorrowingUseCase getBorrowingUseCase;
    private final GetAllBorrowingsByUserUseCase getAllBorrowingsByUserUseCase;
    private final GetActiveBorrowingsByUserUseCase getActiveBorrowingsByUserUseCase;
    private final GetInactiveBorrowingsByUserUseCase getInactiveBorrowingsByUserUseCase;

    private boolean isAdminOrLibrarian(Authentication authentication) {
        return authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(role -> role.equals("ROLE_ADMINISTRATOR") || role.equals("ROLE_LIBRARIAN"));
    }

    private boolean isOwnBorrowing(Authentication authentication, Integer userId) {
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            Object claim = jwt.getClaim("userId");
            Integer authenticatedUserId = null;
            if (claim instanceof Integer) {
                authenticatedUserId = (Integer) claim;
            } else if (claim instanceof Long) {
                authenticatedUserId = ((Long) claim).intValue();
            }
            return authenticatedUserId != null && authenticatedUserId.equals(userId);
        }
        return false;
    }

    private boolean hasAccess(Authentication authentication, Integer userId) {
        return isAdminOrLibrarian(authentication) || isOwnBorrowing(authentication, userId);
    }

    @PostMapping
    public ResponseEntity<BorrowingDTO> createBorrowing(@Valid @RequestBody BorrowingDTO borrowingDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!hasAccess(authentication, borrowingDto.getUserId())) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(addBorrowingUseCase.createBorrowing(borrowingDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BorrowingDTO> getBorrowing(@PathVariable Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        BorrowingDTO borrowing = getBorrowingUseCase.getBorrowing(id);
        if (!hasAccess(authentication, borrowing.getUserId())) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(borrowing);
    }

    @GetMapping
    public ResponseEntity<List<BorrowingDTO>> getAllBorrowingsByUser(@RequestParam Integer userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!hasAccess(authentication, userId)) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(getAllBorrowingsByUserUseCase.getAllBorrowingsByUser(userId));
    }

    @GetMapping("/active")
    public ResponseEntity<List<BorrowingDTO>> getActiveBorrowingsByUser(@RequestParam Integer userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!hasAccess(authentication, userId)) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(getActiveBorrowingsByUserUseCase.getActiveBorrowingsByUser(userId));
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<BorrowingDTO>> getInactiveBorrowingsByUser(@RequestParam Integer userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!hasAccess(authentication, userId)) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(getInactiveBorrowingsByUserUseCase.getInactiveBorrowingsByUser(userId));
    }
}