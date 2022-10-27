package com.majorproject.model;

import com.majorproject.model.queries.currencyscoop.Input;
import com.majorproject.model.queries.pastebin.Output;
import javafx.util.Pair;

/**
 * This is a concrete model class that has caching disabled. No api connection  is present, and
 * no sqlite is used to store conversion rates. Instead, an offline json file storing rates is used.
 */
public class ModelNoCache extends ModelAbstract
{
    public ModelNoCache(Input input, Output output)
    {
        super(input, output);
    }

    /**
     * Processes and gets user's inputted number to convert by referring to Input class.
     * @param from starting currency code
     * @param to finishing currency code
     * @param amount number to convert
     * @return pair object containing the relevant currency rate and resulting converted number
     * @throws IllegalArgumentException if the number is 0, negative or too big.
     */
    @Override
    public Pair<String, String> convertInput(String from, String to, String amount) throws Exception
    {
        double amountDouble = Double.parseDouble(amount);

        if(amountDouble <= 0)
            throw new IllegalArgumentException("Please enter a positive number.");
        else if(amountDouble > 10000000000000000000000000000.0)
            throw new IllegalArgumentException("Number is too large.");

        double converted = input.getConvert(from, to, amountDouble);

        String convertedStr = String.valueOf(converted);
        updateConversionInfo(from, to, amount, convertedStr, convertedStr);

        return new Pair<>(convertedStr, convertedStr);
    }

    /** Returns the display text for attempting to clear cache */
    @Override
    public String handleCacheClear()
    {
        return "No caching is done with the offline api.";
    }

    /**
     * Tells view there will be no hit with cache.
     * @param from starting currency
     * @param to finishing currency
     * @return no cache hit
     */
    public boolean validateRateInCache(String from, String to)
    {
        return false;
    }
}
