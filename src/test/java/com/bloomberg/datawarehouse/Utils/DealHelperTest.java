package com.bloomberg.datawarehouse.Utils;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;
import java.util.Currency;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.bloomberg.datawarehouse.Models.Deal;
import com.bloomberg.datawarehouse.Utils.DealHelper;
import org.springframework.core.io.ClassPathResource;

public class DealHelperTest {
    private static Deal validDealTest;
    private static Deal invalidDealTest;

    @BeforeAll
    public static void setUp() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ClassPathResource validDealResource = new ClassPathResource("/test-data/validDeal.json");
        ClassPathResource invalidDealResource = new ClassPathResource("/test-data/invalidDeal.json");
        validDealTest = objectMapper.readValue(validDealResource.getInputStream(), Deal.class);
        invalidDealTest = objectMapper.readValue(invalidDealResource.getInputStream(), Deal.class);
    }

    @Test
    public void testValidateDealInput_ValidDeal() {


        // Act & Assert
        assertDoesNotThrow(() -> DealHelper.ValidateDealInput(validDealTest));
    }

    @Test
    public void testValidateDealInput_InvalidCurrencyISO() {
        // Arrange
        Deal deal = new Deal();
        deal.setFromCurrencyISO("XYZ"); // Invalid currency ISO

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> DealHelper.ValidateDealInput(deal));
        assertEquals("Invalid currency ISO: XYZ", exception.getMessage());
    }

    @Test
    public void testValidateDealInput_InvalidDealAmount() {


        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> DealHelper.ValidateDealInput(invalidDealTest));
        assertEquals("Invalid deal amount: -1.99", exception.getMessage());
    }

    @Test
    public void testValidateDealInput_TimestampBefore1982() {
        // Arrange
        // Timestamp before 1982
        Deal deal = new Deal("123", "USD", "JOD", Timestamp.valueOf("1980-01-01 00:00:00"),1.99);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> DealHelper.ValidateDealInput(deal));
        assertEquals("Deal timestamp cannot be before the year 1982.", exception.getMessage());
    }

    @Test
    public void testIsValidCurrencyISO_ValidCurrencyISO() {
        // Act & Assert
        assertTrue(DealHelper.isValidCurrencyISO("USD"));
    }

    @Test
    public void testIsValidCurrencyISO_InvalidCurrencyISO() {
        // Act & Assert
        assertFalse(DealHelper.isValidCurrencyISO("XYZ"));
    }

    @Test
    public void testIsTimestampBefore1982_TimestampBefore1982() {
        // Arrange
        Timestamp timestamp = Timestamp.valueOf("1980-01-01 00:00:00"); // Timestamp before 1982

        // Act & Assert
        assertTrue(DealHelper.isTimestampBefore1982(timestamp));
    }

    @Test
    public void testIsTimestampBefore1982_TimestampAfter1982() {
        // Arrange
        Timestamp timestamp = Timestamp.valueOf("1990-01-01 00:00:00"); // Timestamp after 1982

        // Act & Assert
        assertFalse(DealHelper.isTimestampBefore1982(timestamp));
    }
}
