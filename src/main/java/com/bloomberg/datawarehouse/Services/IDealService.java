package com.bloomberg.datawarehouse.Services;

import com.bloomberg.datawarehouse.Models.Deal;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

/**
 * Interface defining operations related to Deal entities.
 */
public interface IDealService {

    /**
     * Performs the operation of inserting a Deal into the system.
     * @param deal The Deal object to be inserted.
     * @return The inserted Deal object.
     */
    Deal insertDeal(Deal deal);

    /**
     * Fetches a list of all Deals from the system.
     * @return A list of all Deals.
     */
    List<Deal> fetchDealsList();

    /**
     * Processes a CSV file and inserts its elements into the system.
     * @param reader The BufferedReader for the CSV file.
     * @throws IOException if an I/O error occurs.
     */
    void processCSVFile(BufferedReader reader) throws IOException;
}
