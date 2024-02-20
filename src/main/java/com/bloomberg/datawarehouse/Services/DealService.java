package com.bloomberg.datawarehouse.Services;

import com.bloomberg.datawarehouse.Models.Deal;
import com.bloomberg.datawarehouse.Repositories.IDealRepository;
import com.bloomberg.datawarehouse.Utils.DealHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Service class for dealing with operations related to Deal entities.
 */
@Service
public class DealService implements IDealService {

    @Autowired
    private IDealRepository dealRepository;
    private static final Logger logger = LoggerFactory.getLogger(DealService.class);

    /**
     * Inserts a new Deal into the database.
     *
     * @param deal The Deal object to be inserted.
     * @return The inserted Deal object.
     * @throws IllegalArgumentException        if the provided Deal object is invalid.
     * @throws DataIntegrityViolationException if a Deal with the same ID already exists.
     */
    @Override
    public Deal insertDeal(Deal deal) {
        try {
            DealHelper.ValidateDealInput(deal);
        } catch (Exception e) {
            logger.error("Error while saving Deal [Invalid Deal Object elements]", e);
            throw e;
        }
        if (dealRepository.existsById(deal.getDealId())) {
            throw new DataIntegrityViolationException("Deal with the same ID already exists");
        }
        return dealRepository.save(deal);
    }

    /**
     * Retrieves a Deal from the database by its ID.
     *
     * @param dealId The ID of the Deal to retrieve.
     * @return The retrieved Deal object.
     * @throws NoSuchElementException if no Deal exists with the provided ID.
     */
    public Deal getDealById(String dealId) {
        return dealRepository.findById(dealId)
                .orElseThrow(() -> new NoSuchElementException("Deal not found with ID: " + dealId));
    }


    /**
     * Processes a CSV file and inserts its elements into the database.
     *
     * @param reader The BufferedReader for the CSV file.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void processCSVFile(BufferedReader reader) throws IOException {
        String row;
        while ((row = reader.readLine()) != null) {
            String[] data = row.split(",");
            try {
                Deal deal = new Deal();
                deal.setDealId(data[0]);
                deal.setFromCurrencyISO(data[1]);
                deal.setToCurrencyISO(data[2]);
                deal.setDealTimestamp(Timestamp.valueOf(data[3]));
                deal.setDealAmount(Double.parseDouble(data[4]));

                if (dealRepository.existsById(deal.getDealId())) {
                    throw new DataIntegrityViolationException("Deal with the same ID already exists");
                }
                try {
                    DealHelper.ValidateDealInput(deal);
                    dealRepository.save(deal);
                } catch (IllegalArgumentException e) {
                    logger.error("Error while saving Deal [Invalid Deal Object elements]", e);
                    throw e;
                }
            } catch (DataIntegrityViolationException e) {
                logger.warn("Deal with the same ID already exists");
            } catch (Exception e) {
                logger.error("Error while parsing CSV Row [Invalid Deal Object Format]", e);
            }
        }
    }

    /**
     * Fetches a list of all Deals from the database.
     *
     * @return A list of all Deals.
     */
    @Override
    public List<Deal> fetchDealsList() {
        return (List<Deal>) dealRepository.findAll();
    }

    /**
     * Fetches a list of Deals from the database based on specific rules.
     * For example, SELECT * FROM deal WHERE amount > 10000 can be implemented here.
     * @return A list of Deals based on the specified rules.
     */
    // Add method for fetching deals with specific rules here
}
