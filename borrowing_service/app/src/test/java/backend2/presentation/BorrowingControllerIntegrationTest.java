package backend2.presentation;

import backend2.config.TestConfig;
import backend2.domain.BorrowingDTO;
import backend2.persistence.BorrowingRepository;
import backend2.persistence.entity.BorrowingEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestConfig.class)
public class BorrowingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BorrowingRepository borrowingRepository;

    private Integer userId;
    private Integer bookUnitId;
    private Integer borrowingId;
    private LocalDate today;
    private String shippingAddress;

    @BeforeEach
    void setUp() {
        borrowingRepository.deleteAll();
        userId = 1;
        bookUnitId = 1;
        today = LocalDate.now();
        shippingAddress = "123 Test Street, Test City, 12345";
    }

    private BorrowingDTO createTestBorrowingDTO() {
        return BorrowingDTO.builder()
                .userId(userId)
                .bookUnitId(bookUnitId)
                .startDate(today)
                .endDate(today.plusDays(14))
                .shippingAddress(shippingAddress)
                .active(true)
                .build();
    }

    private BorrowingEntity createTestBorrowingEntity() {
        return BorrowingEntity.builder()
                .userId(userId)
                .bookUnitId(bookUnitId)
                .startDate(today)
                .endDate(today.plusDays(14))
                .shippingAddress(shippingAddress)
                .active(true)
                .createdAt(today)
                .build();
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void testGetBorrowing() throws Exception {
        BorrowingEntity borrowing = borrowingRepository.save(createTestBorrowingEntity());
        borrowingId = borrowing.getId();

        MvcResult result = mockMvc.perform(get("/api/v1/borrowings/{id}", borrowingId))
                .andExpect(status().isOk())
                .andReturn();

        BorrowingDTO retrievedBorrowing = objectMapper.readValue(result.getResponse().getContentAsString(), BorrowingDTO.class);
        assertNotNull(retrievedBorrowing);
        assertEquals(borrowingId, retrievedBorrowing.getId());
        assertEquals(userId, retrievedBorrowing.getUserId());
        assertEquals(bookUnitId, retrievedBorrowing.getBookUnitId());
        assertEquals(shippingAddress, retrievedBorrowing.getShippingAddress());
        assertEquals(today, retrievedBorrowing.getStartDate());
        assertEquals(today.plusDays(14), retrievedBorrowing.getEndDate());
        assertTrue(retrievedBorrowing.isActive());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void testGetAllBorrowingsByUser() throws Exception {
        borrowingRepository.save(createTestBorrowingEntity());

        MvcResult result = mockMvc.perform(get("/api/v1/borrowings")
                .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andReturn();

        List<BorrowingDTO> borrowings = objectMapper.readValue(result.getResponse().getContentAsString(), 
            objectMapper.getTypeFactory().constructCollectionType(List.class, BorrowingDTO.class));
        assertNotNull(borrowings);
        assertFalse(borrowings.isEmpty());
        assertEquals(1, borrowings.size());
        BorrowingDTO borrowing = borrowings.get(0);
        assertEquals(userId, borrowing.getUserId());
        assertEquals(bookUnitId, borrowing.getBookUnitId());
        assertEquals(shippingAddress, borrowing.getShippingAddress());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void testGetActiveBorrowingsByUser() throws Exception {
        borrowingRepository.save(createTestBorrowingEntity());

        MvcResult result = mockMvc.perform(get("/api/v1/borrowings/active")
                .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andReturn();

        List<BorrowingDTO> borrowings = objectMapper.readValue(result.getResponse().getContentAsString(), 
            objectMapper.getTypeFactory().constructCollectionType(List.class, BorrowingDTO.class));
        assertNotNull(borrowings);
        assertFalse(borrowings.isEmpty());
        assertEquals(1, borrowings.size());
        BorrowingDTO borrowing = borrowings.get(0);
        assertTrue(borrowing.isActive());
        assertEquals(userId, borrowing.getUserId());
        assertEquals(bookUnitId, borrowing.getBookUnitId());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void testGetInactiveBorrowingsByUser() throws Exception {
        BorrowingEntity inactiveBorrowing = createTestBorrowingEntity();
        inactiveBorrowing.setActive(false);
        borrowingRepository.save(inactiveBorrowing);

        MvcResult result = mockMvc.perform(get("/api/v1/borrowings/inactive")
                .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andReturn();

        List<BorrowingDTO> borrowings = objectMapper.readValue(result.getResponse().getContentAsString(), 
            objectMapper.getTypeFactory().constructCollectionType(List.class, BorrowingDTO.class));
        assertNotNull(borrowings);
        assertFalse(borrowings.isEmpty());
        assertEquals(1, borrowings.size());
        BorrowingDTO borrowing = borrowings.get(0);
        assertFalse(borrowing.isActive());
        assertEquals(userId, borrowing.getUserId());
        assertEquals(bookUnitId, borrowing.getBookUnitId());
    }
} 