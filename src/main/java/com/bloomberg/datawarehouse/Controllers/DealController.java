package com.bloomberg.datawarehouse.Controllers;

import com.bloomberg.datawarehouse.Models.Deal;
import com.bloomberg.datawarehouse.Services.DealService;
import com.bloomberg.datawarehouse.Utils.DealCustomResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Controller class for handling operations related to Deal entities.
 */
@RestController
@RequestMapping("/api")
public class DealController {

    @Autowired
    private DealService dealService;

    private static final Logger logger = LoggerFactory.getLogger(DealController.class);

    /**
     * Endpoint for inserting a Deal into the system.
     *
     * @param deal The Deal object to be inserted.
     * @return ResponseEntity containing a custom response with details about the operation.
     */
    @PostMapping("/deals")
    public ResponseEntity<DealCustomResponse<Deal>> insertDeal(@Valid @RequestBody Deal deal) {
        DealCustomResponse<Deal> response = new DealCustomResponse<>();
        try {
            Deal insertedDeal = dealService.insertDeal(deal);
            response.setStatusCode(HttpStatus.CREATED.value());
            response.setDetailedStatusCode("Deal created successfully");
            response.setData(insertedDeal);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (DataIntegrityViolationException e) {
            // Handle unique constraint violation
            response.setStatusCode(HttpStatus.CONFLICT.value());
            response.setDetailedStatusCode("Deal with the same ID already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);

        } catch (IllegalArgumentException e) {
            response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
            response.setDetailedStatusCode("Invalid Deal Object elements");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
    }

    /**
     * Endpoint for fetching a list of Deals from the system.
     *
     * @return ResponseEntity containing a custom response with details about the operation.
     */
    @GetMapping("/deals")
    public ResponseEntity<DealCustomResponse<List<Deal>>> fetchDealsList() {
        DealCustomResponse<List<Deal>> response = new DealCustomResponse<>();
        try {
            response.setStatusCode(HttpStatus.OK.value());
            response.setDetailedStatusCode("Deals fetched successfully");
            response.setData(dealService.fetchDealsList());
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (Exception e) {
            logger.warn("Third party partner server might went down");
            logger.error(e.getMessage());
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setDetailedStatusCode("Deals fetched successfully");
            response.setData(dealService.fetchDealsList());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * Endpoint for retrieving a Deal by its ID.
     *
     * @param dealId The ID of the Deal to retrieve.
     * @return ResponseEntity containing a custom response with details about the operation.
     */
    @GetMapping("/deals/{dealId}")
    public ResponseEntity<DealCustomResponse<Deal>> getDealById(@PathVariable String dealId) {
        DealCustomResponse<Deal> response = new DealCustomResponse<>();
        try {
            Deal deal = dealService.getDealById(dealId);
            response.setStatusCode(HttpStatus.OK.value());
            response.setDetailedStatusCode("Deal retrieved successfully");
            response.setData(deal);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (NoSuchElementException e) {
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setDetailedStatusCode("Deal not found with ID: " + dealId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            // Handle other exceptions if needed
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setDetailedStatusCode("Internal Server Error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    /**
     * Endpoint for uploading a CSV file containing Deal data.
     *
     * @param file The CSV file to be uploaded.
     * @return ResponseEntity containing a custom response with details about the operation.
     */
    @PostMapping("/upload")
    public ResponseEntity<DealCustomResponse<MultipartFile>> uploadCSVFile(@RequestParam("file") MultipartFile file) {
        DealCustomResponse<MultipartFile> response = new DealCustomResponse<>();
        try {
            if (file.isEmpty()) {
                response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
                response.setDetailedStatusCode("File is empty, Cannot process empty file");
                response.setData(null);
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            dealService.processCSVFile(reader);

            response.setStatusCode(HttpStatus.CREATED.value());
            response.setDetailedStatusCode("CSV file rows successfully inserted into the database");
            response.setData(null);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            response.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
            response.setDetailedStatusCode("CSV file rows successfully inserted into the database");
            response.setData(null);
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
    }

}
