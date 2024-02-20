package com.bloomberg.datawarehouse.Utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class DealCustomResponseTest {

    @Test
    public void testStatusCodeGetterAndSetter() {
        // Arrange
        DealCustomResponse<String> response = new DealCustomResponse<>();

        // Act
        response.setStatusCode(200);
        int statusCode = response.getStatusCode();

        // Assert
        assertEquals(200, statusCode);
    }

    @Test
    public void testDetailedStatusCodeGetterAndSetter() {
        // Arrange
        DealCustomResponse<String> response = new DealCustomResponse<>();

        // Act
        response.setDetailedStatusCode("Success");
        String detailedStatusCode = response.getDetailedStatusCode();

        // Assert
        assertEquals("Success", detailedStatusCode);
    }

    @Test
    public void testDataGetterAndSetter() {
        // Arrange
        DealCustomResponse<Integer> response = new DealCustomResponse<>();

        // Act
        response.setData(123);
        int data = response.getData();

        // Assert
        assertEquals(123, data);
    }
}
