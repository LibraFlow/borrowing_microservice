package backend2.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import backend2.domain.BorrowingDTO;

public class EndDateAfterStartDateValidator implements ConstraintValidator<ValidEndDate, BorrowingDTO> {

    @Override
    public void initialize(ValidEndDate constraintAnnotation) {
        // Initialization logic (if needed)
    }

    @Override
    public boolean isValid(BorrowingDTO borrowing, ConstraintValidatorContext context) {
        if (borrowing.getStartDate() == null || borrowing.getEndDate() == null) {
            return true; // Let @NotNull handle null checks
        }
        return borrowing.getEndDate().isAfter(borrowing.getStartDate());
    }
}