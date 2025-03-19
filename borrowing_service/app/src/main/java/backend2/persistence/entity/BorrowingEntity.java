package backend2.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.validation.constraints.*;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "borrowings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BorrowingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "User cannot be empty")
    private Integer userId;

    @NotNull(message = "Book cannot be empty")
    private Integer bookUnitId;

    @NotBlank(message = "Shipping address cannot be blank")
    @Size(min = 10, max = 100, message = "shipping address must be between 10 and 100 characters")
    private String shippingAddress;

    @NotNull(message = "Start date cannot be empty")
    @FutureOrPresent(message = "Start date must be today or in the future")
    private LocalDate startDate;

    @NotNull(message = "End date cannot be empty")
    private LocalDate endDate;

    @NotNull
    @PastOrPresent
    @Column(nullable = false)
    private LocalDate createdAt;
}