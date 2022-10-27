package com.majorproject.model;

import com.majorproject.model.queries.currencyscoop.Input;
import com.majorproject.model.queries.pastebin.Output;
import javafx.util.Pair;

import static com.majorproject.model.RatesDb.*;

/**
 * This is a concrete model class with caching enabled. ..
 * Instead, an offline json file containing rates is used.
 */
public class ModelCache extends ModelAbstract
{
    private final String dbName = "test.db";

    private boolean overwriteRate = false;

    public ModelCache(Input input, Output output)
    {
        super(input, output);
    }

    /**
     * Processes and gets user's inputted number to convert by referring to Input class.
     * Either uses cached rate or fetches rate from CurrencyScoop api.
     * @param from starting currency code
     * @param to finishing currency code
     * @param amount number to convert
     * @return a pair object containing the currency rate between the 2 currencies and the resulting converted number
     * @throws IllegalArgumentException if the number is 0, negative or too big
     */
    @Override
    public Pair<String, String> convertInput(String from, String to, String amount) throws Exception
    {
        double amountDouble = Double.parseDouble(amount);

        double converted;
        double rate;

        String currenciesConcatenated = from + to;
        if(overwriteRate || !checkRateInDb(currenciesConcatenated))
        {
            converted = input.getConvert(from, to, amountDouble);
            rate = converted / amountDouble;

            insertRateDb(currenciesConcatenated, rate, overwriteRate);
        }
        else
        {
            rate = Double.parseDouble(getRateDb(currenciesConcatenated));
            converted = amountDouble * rate;
        }

        String rateStr = String.valueOf(rate);
        String convertedStr = String.valueOf(converted);
        updateConversionInfo(from, to, amount, rateStr, convertedStr);

        overwriteRate = false;

        return new Pair<>(rateStr, convertedStr);
    }

    /** Returns the display text for attempting to clear cache */
    @Override
    public String handleCacheClear() throws Exception
    {
        removeDb();
        return "Cache Cleared.";
    }

    /**
     * Checks to see if rate between the 2 given currencies is already in the database
     * @param from starting currency
     * @param to finishing currency
     * @return whether the rate was found
     */
    public boolean validateRateInCache(String from, String to) throws Exception
    {
        createDb(dbName);

        return checkRateInDb(from + to);
    }

    /** Tells the model to replace cached currency rate with api data */
    public void setToOverwriteCache()
    {
        overwriteRate = true;
    }
}
