package com.bloomberg.datawarehouse.Services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataIntegrityViolationException;

import com.bloomberg.datawarehouse.Models.Deal;
import com.bloomberg.datawarehouse.Repositories.IDealRepository;

@ExtendWith(MockitoExtension.class)
public class DealServiceTest {

    @Mock
    private IDealRepository dealRepository;

    @InjectMocks
    private DealService dealService;
    private static Deal validDealTest;
    private static Deal invalidDealTest;

    @BeforeAll
    public static void setup() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ClassPathResource validDealResource = new ClassPathResource("/test-data/validDeal.json");
        ClassPathResource invalidDealResource = new ClassPathResource("/test-data/invalidDeal.json");
        validDealTest = objectMapper.readValue(validDealResource.getInputStream(), Deal.class);
        invalidDealTest = objectMapper.readValue(invalidDealResource.getInputStream(), Deal.class);
    }

    @Test
    public void testInsertDeal_Success() {
        // Arrange
        validDealTest.setDealId("123");
        validDealTest.setFromCurrencyISO("USD");
        validDealTest.setToCurrencyISO("EUR");
        validDealTest.setDealTimestamp(new Timestamp(System.currentTimeMillis()));
        validDealTest.setDealAmount(100.0);

        when(dealRepository.existsById("123")).thenReturn(false);
        when(dealRepository.save(any(Deal.class))).thenReturn(validDealTest);

        // Act
        Deal savedDeal = dealService.insertDeal(validDealTest);

        // Assert
        assertEquals(validDealTest, savedDeal);
    }

    @Test
    public void testInsertDeal_DuplicateDealId() {
        // Arrange
        validDealTest.setDealId("123");

        when(dealRepository.existsById("123")).thenReturn(true);

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class, () -> dealService.insertDeal(validDealTest));
    }

    @Test
    public void testInsertDeal_InvalidDealInput() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> dealService.insertDeal(invalidDealTest));
    }

    @Test
    public void testFetchDealsList_Success() {
        // Arrange
        Deal deal1 = new Deal();
        deal1.setDealId("123");
        Deal deal2 = new Deal();
        deal2.setDealId("456");

        List<Deal> deals = Arrays.asList(deal1, deal2);

        when(dealRepository.findAll()).thenReturn(deals);

        // Act
        List<Deal> fetchedDeals = dealService.fetchDealsList();

        // Assert
        assertEquals(deals.size(), fetchedDeals.size());
    }

    @Test
    public void testGetDealById_WhenDealExists() {
        // Arrange
        String dealId = "123";
        Deal mockDeal = new Deal();
        mockDeal.setDealId(dealId);
        when(dealRepository.findById(dealId)).thenReturn(Optional.of(mockDeal));

        // Act
        Deal retrievedDeal = dealService.getDealById(dealId);

        // Assert
        assertEquals(dealId, retrievedDeal.getDealId());
    }

    @Test
    public void testGetDealById_WhenDealDoesNotExist() {
        // Arrange
        String dealId = "123";
        when(dealRepository.findById(dealId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(NoSuchElementException.class, () -> {
            dealService.getDealById(dealId);
        });
    }
}
