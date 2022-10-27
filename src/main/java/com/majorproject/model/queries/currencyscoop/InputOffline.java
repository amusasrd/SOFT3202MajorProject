package com.majorproject.model.queries.currencyscoop;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;

import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;

/**
 * An offline implementation of the Input interface that does not require an internet connection.
 * Useful for testing, but not for accuracy; the getConvert method simply returns the given number.
 */
public class InputOffline implements Input
{
    private final Gson gson = new Gson();

    /**
     * Gets info on all currencies in json file and loads it into a hashmap.
     * @return a hashmap containing Fiat classes for each currency, detailing its info
     */
    public LinkedHashMap<String, Fiat> getCurrencies() throws Exception
    {
        try
        {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream reader = classloader.getResourceAsStream("offline.json");

            if(reader == null)
                throw new FileNotFoundException("No json file found for InputOffline");

            String fiatsJson = IOUtils.toString(reader, StandardCharsets.UTF_8);

            Type type = new TypeToken<LinkedHashMap<String, Fiat>>(){}.getType();

            return gson.fromJson(fiatsJson, type);
        }
        catch(Exception e)
        {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Simple method when testing app offline.
     * @param from starting currency code
     * @param to finishing currency code
     * @param amount number to convert to new currency
     * @return the given amount
     */
    public double getConvert(String from, String to, double amount)
    {
        return amount;
    }
}
