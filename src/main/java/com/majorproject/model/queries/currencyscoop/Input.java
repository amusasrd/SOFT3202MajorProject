package com.majorproject.model.queries.currencyscoop;

import java.util.LinkedHashMap;

/**
 * This interface is the template by which the app connects to the Currencyscoop api.
 * It is used to get data to work on, namely information on currencies, and conversions.
 */
public interface Input
{
    LinkedHashMap<String, Fiat> getCurrencies() throws Exception;

    double getConvert(String from, String to, double amount) throws Exception;
}
