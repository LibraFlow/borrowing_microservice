package backend2.persistence;

import backend2.persistence.entity.BorrowingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowingRepository extends JpaRepository<BorrowingEntity, Integer> {
    List<BorrowingEntity> findByUserId(Integer userId);
    List<BorrowingEntity> findByUserIdAndActiveTrue(Integer userId);
    List<BorrowingEntity> findByUserIdAndActiveFalse(Integer userId);
}
