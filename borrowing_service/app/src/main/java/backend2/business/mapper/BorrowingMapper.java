package backend2.business.mapper;

import backend2.domain.BorrowingDTO;
import backend2.persistence.entity.BorrowingEntity;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class BorrowingMapper {

    public BorrowingDTO toDTO(BorrowingEntity entity) {
        if (entity == null) {
            return null;
        }

        return BorrowingDTO.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .bookUnitId(entity.getBookUnitId())
                .shippingAddress(entity.getShippingAddress())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .active(entity.isActive())
                .build();
    }

    public BorrowingEntity toEntity(BorrowingDTO dto) {
        if (dto == null) {
            return null;
        }

        return BorrowingEntity.builder()
                .id(dto.getId())
                .userId(dto.getUserId())
                .bookUnitId(dto.getBookUnitId())
                .shippingAddress(dto.getShippingAddress())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .createdAt(dto.getId() == null ? LocalDate.now() : null) // Set createdAt only for new entities
                .active(dto.isActive())
                .build();
    }
}