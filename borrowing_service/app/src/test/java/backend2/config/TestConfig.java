package backend2.config;

import backend2.business.borrowing.*;
import backend2.business.mapper.BorrowingMapper;
import backend2.persistence.BorrowingRepository;
import backend2.security.EncryptionService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public EncryptionService encryptionService() {
        return new EncryptionService() {
            @Override
            public String encrypt(String data) {
                return data;
            }

            @Override
            public String decrypt(String data) {
                return data;
            }
        };
    }

    @Bean
    @Primary
    public BorrowingMapper borrowingMapper(EncryptionService encryptionService) {
        return new BorrowingMapper(encryptionService);
    }

    @Bean
    @Primary
    public AddBorrowingUseCase addBorrowingUseCase(
            BorrowingRepository borrowingRepository,
            BorrowingMapper borrowingMapper) {
        return new AddBorrowingUseCase(borrowingRepository, borrowingMapper, null);
    }

    @Bean
    @Primary
    public GetBorrowingUseCase getBorrowingUseCase(
            BorrowingRepository borrowingRepository,
            BorrowingMapper borrowingMapper) {
        return new GetBorrowingUseCase(borrowingRepository, borrowingMapper);
    }

    @Bean
    @Primary
    public GetAllBorrowingsByUserUseCase getAllBorrowingsByUserUseCase(
            BorrowingRepository borrowingRepository,
            BorrowingMapper borrowingMapper) {
        return new GetAllBorrowingsByUserUseCase(borrowingRepository, borrowingMapper);
    }

    @Bean
    @Primary
    public GetActiveBorrowingsByUserUseCase getActiveBorrowingsByUserUseCase(
            BorrowingRepository borrowingRepository,
            BorrowingMapper borrowingMapper) {
        return new GetActiveBorrowingsByUserUseCase(borrowingRepository, borrowingMapper);
    }

    @Bean
    @Primary
    public GetInactiveBorrowingsByUserUseCase getInactiveBorrowingsByUserUseCase(
            BorrowingRepository borrowingRepository,
            BorrowingMapper borrowingMapper) {
        return new GetInactiveBorrowingsByUserUseCase(borrowingRepository, borrowingMapper);
    }

    @Bean
    @Primary
    public GetAllBorrowingsByBookUnitIdUseCase getAllBorrowingsByBookUnitIdUseCase(
            BorrowingRepository borrowingRepository,
            BorrowingMapper borrowingMapper) {
        return new GetAllBorrowingsByBookUnitIdUseCase(borrowingRepository, borrowingMapper);
    }
} 