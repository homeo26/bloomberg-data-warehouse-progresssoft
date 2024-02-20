package com.bloomberg.datawarehouse.Models;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.core.io.ClassPathResource;

import java.sql.Timestamp;

class DealTest {
    private static ObjectMapper objectMapper;
    private static Deal validDealTest;
    private static Deal invalidDealTest;

    @BeforeAll
    public static void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        ClassPathResource validDealResource = new ClassPathResource("/test-data/validDeal.json");
        ClassPathResource invalidDealResource = new ClassPathResource("/test-data/invalidDeal.json");
        validDealTest = objectMapper.readValue(validDealResource.getInputStream(), Deal.class);
        invalidDealTest = objectMapper.readValue(invalidDealResource.getInputStream(), Deal.class);
    }

    @Test
    @DisplayName("Test the Deal constructor with valid Data")
    public void testDealConstructor() {
        String dealId = "RaNdom-Deal-Id-xxx00";
        String fromCurrencyISO = "USD";
        String toCurrencyISO = "EUR";
        Timestamp dealTimestamp = Timestamp.valueOf("2024-02-20 14:17:19.924");
        double dealAmount = 99.99;

        Assertions.assertEquals(dealId, validDealTest.getDealId());
        Assertions.assertEquals(fromCurrencyISO, validDealTest.getFromCurrencyISO());
        Assertions.assertEquals(toCurrencyISO, validDealTest.getToCurrencyISO());
        Assertions.assertEquals(dealTimestamp, validDealTest.getDealTimestamp());
        Assertions.assertEquals(dealAmount, validDealTest.getDealAmount());
    }

    @Test
    @DisplayName("Test deal.toString() with valid data")
    public void testDealToString() {
        String dealId = "RaNdom-Deal-Id-xxx00";
        String fromCurrencyISO = "USD";
        String toCurrencyISO = "EUR";
        String dealTimestamp = "2024-02-20 14:17:19.924";
        double dealAmount = 99.99;

        String expectedToString = "Deal{dealId='" + dealId + '\'' +
                ", fromCurrencyISO='" + fromCurrencyISO + '\'' +
                ", toCurrencyISO='" + toCurrencyISO + '\'' +
                ", dealTimestamp=" + dealTimestamp +
                ", dealAmount=" + dealAmount +
                '}';

        Assertions.assertEquals(expectedToString, validDealTest.toString());
    }
}
