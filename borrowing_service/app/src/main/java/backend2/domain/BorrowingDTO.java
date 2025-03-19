package backend2.domain;

import lombok.*;
import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
public class BorrowingDTO {
    private Integer id;
    private Integer userId;
    private Integer bookUnitId;
    private String shippingAddress;
    private LocalDate startDate;
    private LocalDate endDate;
}