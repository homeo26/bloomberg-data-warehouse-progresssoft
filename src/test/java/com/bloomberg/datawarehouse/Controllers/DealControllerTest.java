package com.bloomberg.datawarehouse.Controllers;

import com.bloomberg.datawarehouse.Models.Deal;
import com.bloomberg.datawarehouse.Services.DealService;
import com.bloomberg.datawarehouse.Utils.DealCustomResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DealControllerTest {

    @Mock
    private DealService dealService;

    @InjectMocks
    private DealController dealController;

    @Test
    public void testInsertDeal_Success() {
        // Arrange
        Deal deal = new Deal();
        when(dealService.insertDeal(any())).thenReturn(deal);

        // Act
        ResponseEntity<DealCustomResponse<Deal>> responseEntity = dealController.insertDeal(deal);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("Deal created successfully", responseEntity.getBody().getDetailedStatusCode());
        assertEquals(deal, responseEntity.getBody().getData());
    }

    @Test
    public void testInsertDeal_Conflict() {
        // Arrange
        Deal deal = new Deal();
        when(dealService.insertDeal(any())).thenThrow(DataIntegrityViolationException.class);

        // Act
        ResponseEntity<DealCustomResponse<Deal>> responseEntity = dealController.insertDeal(deal);

        // Assert
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals("Deal with the same ID already exists", responseEntity.getBody().getDetailedStatusCode());
    }

    @Test
    public void testInsertDeal_InvalidObject() {
        // Arrange
        Deal deal = new Deal();
        when(dealService.insertDeal(any())).thenThrow(IllegalArgumentException.class);

        // Act
        ResponseEntity<DealCustomResponse<Deal>> responseEntity = dealController.insertDeal(deal);

        // Assert
        assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
        assertEquals("Invalid Deal Object elements", responseEntity.getBody().getDetailedStatusCode());
    }

    @Test
    public void testFetchDealsList_Success() {
        // Arrange
        List<Deal> deals = new ArrayList<>();
        deals.add(new Deal());
        when(dealService.fetchDealsList()).thenReturn(deals);

        // Act
        ResponseEntity<DealCustomResponse<List<Deal>>> responseEntity = dealController.fetchDealsList();

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Deals fetched successfully", Objects.requireNonNull(responseEntity.getBody()).getDetailedStatusCode());
        assertEquals(deals, responseEntity.getBody().getData());
    }

    @Test
    public void testFetchDealsList_Exception() {
        // Arrange
        when(dealService.fetchDealsList()).thenThrow(RuntimeException.class);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> dealController.fetchDealsList());
    }

    @Test
    public void testUploadCSVFile_EmptyFile() {
        // Arrange
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(true);

        // Act
        ResponseEntity<DealCustomResponse<MultipartFile>> responseEntity = dealController.uploadCSVFile(file);

        // Assert
        assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
        assertEquals("File is empty, Cannot process empty file", responseEntity.getBody().getDetailedStatusCode());
    }

}
