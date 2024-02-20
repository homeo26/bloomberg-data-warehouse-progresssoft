package com.bloomberg.datawarehouse.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * Model class representing a Deal entity.
 */
@Getter
@Setter
@Entity
@Table(name = "deal", uniqueConstraints = @UniqueConstraint(columnNames = "deal_id"))
public class Deal {

    @NotNull
    @Id
    private String dealId;

    @NotNull
    private String fromCurrencyISO;

    @NotNull
    private String toCurrencyISO;

    @NotNull
    @Column(columnDefinition = "TIMESTAMP(6) WITHOUT TIME ZONE")
    private Timestamp dealTimestamp;

    @NotNull
    private double dealAmount;

    /**
     * Default constructor.
     */
    public Deal() {
    }

    /**
     * Parameterized constructor.
     *
     * @param dealId          The unique identifier for the Deal.
     * @param fromCurrencyISO The ISO code for the currency from which the Deal is made.
     * @param toCurrencyISO   The ISO code for the currency to which the Deal is made.
     * @param dealTimestamp   The timestamp when the Deal occurred.
     * @param dealAmount      The amount of the Deal.
     * @throws IllegalArgumentException if any of the parameters are null.
     */
    public Deal(String dealId, String fromCurrencyISO, String toCurrencyISO, Timestamp dealTimestamp, double dealAmount) throws IllegalArgumentException {
        this.dealId = dealId;
        this.fromCurrencyISO = fromCurrencyISO;
        this.toCurrencyISO = toCurrencyISO;
        this.dealTimestamp = dealTimestamp;
        this.dealAmount = dealAmount;
    }

    /**
     * Provides a string representation of the Deal object.
     *
     * @return A string representation of the Deal object.
     */
    @Override
    public String toString() {
        return "Deal{" +
                "dealId='" + dealId + '\'' +
                ", fromCurrencyISO='" + fromCurrencyISO + '\'' +
                ", toCurrencyISO='" + toCurrencyISO + '\'' +
                ", dealTimestamp=" + dealTimestamp +
                ", dealAmount=" + dealAmount +
                '}';
    }
}
