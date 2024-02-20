package com.bloomberg.datawarehouse.Utils;

import com.bloomberg.datawarehouse.Models.Deal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Currency;
import java.util.Objects;

public class DealHelper {
    private static final Logger logger = LoggerFactory.getLogger(DealHelper.class);
    public static void ValidateDealInput(Deal deal){
        if (!DealHelper.isValidCurrencyISO(deal.getFromCurrencyISO()) || !DealHelper.isValidCurrencyISO(deal.getToCurrencyISO())) {
            logger.error("Invalid currency ISO");
            throw new IllegalArgumentException(
                    "Invalid currency ISO: " + (
                    DealHelper.isValidCurrencyISO(deal.getFromCurrencyISO()) ? deal.getToCurrencyISO() : deal.getFromCurrencyISO())
            );
        } else if (Objects.equals(deal.getFromCurrencyISO(), deal.getToCurrencyISO())) {
            throw new IllegalArgumentException(
                    "Cannot convert from and to the same ISO: " + (
                            DealHelper.isValidCurrencyISO(deal.getFromCurrencyISO()) ? deal.getToCurrencyISO() : deal.getFromCurrencyISO())
            );
        }

        if (deal.getDealAmount() <= 0) {
            logger.error("Invalid deal amount input");
            throw new IllegalArgumentException("Invalid deal amount: " + deal.getDealAmount());
        }

        if (DealHelper.isTimestampBefore1982(deal.getDealTimestamp())) {
            logger.error("Deal timestamp cannot be before the year 1982");
            throw new IllegalArgumentException("Deal timestamp cannot be before the year 1982.");
        }
    }
    public static boolean isValidCurrencyISO(String currencyISO) {
        try {
            Currency.getInstance(currencyISO);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static boolean isTimestampBefore1982(Timestamp timestamp) {
        return timestamp.before(getTimestamp1982());
    }

    public static Timestamp getTimestamp1982() {
        return Timestamp.valueOf("1982-01-01 00:00:00");
    }
}
