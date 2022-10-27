package com.majorproject.model;

import com.majorproject.model.queries.currencyscoop.Fiat;
import com.majorproject.model.queries.currencyscoop.Input;
import com.majorproject.model.queries.pastebin.Output;
import javafx.collections.ObservableList;
import javafx.scene.media.MediaPlayer;
import org.controlsfx.control.WorldMapView;

import java.util.*;

/**
 * The engine class, designed to store all relevant data, manage api queries and process app logic.
 * Implements all methods of the model interface that are shared between the caching and non-caching
 * concrete classes, which includes handling app audio, retrieving info on currencies, managing paste
 * duties and validation for the view.
 */
public abstract class ModelAbstract implements Model
{
    protected final Input input;
    protected final Output output;
    protected LinkedHashMap<String, Fiat> fiats = null;
    protected Report conversionReport;
    private double threshold;

    private final Set<AudioObserver> observers;

    ModelAbstract(Input input, Output output)
    {
        this.input = input;
        this.output = output;

        try
        {
            fiats = input.getCurrencies();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            System.exit(-6);
        }

        conversionReport = new Report();

        observers = new HashSet<>();
    }

    /** Plays / pauses app music. */
    public void playAudio()
    {
        updateObservers();
    }

    /**
     * Finds the currency for each given country and conveys whether one was found or not.
     * Value of a key is instead 'N/A' if a currency is not found.
     * @param countryCodes a list of country codes, as in 'WorldMapView.Country'
     * @return hashmap containing all given countries and their respective currency, if found
     */
    @Override
    public HashMap<String, String> getCurrencyInfo(ObservableList<WorldMapView.Country> countryCodes)
    {
        HashMap<String, String> countryCurrencies = new HashMap<>();

        for(WorldMapView.Country code: countryCodes)
        {
            String countryName = code.getLocale().getDisplayCountry();

            String item = searchCurrency(countryName);

            countryCurrencies.put(countryName, Objects.requireNonNullElse(item, "N/A"));
        }

        return countryCurrencies;
    }

    /**
     * Looks for the provided country in each currency's list of countries that use it.
     * @param countryName the country to find its currency
     * @return the currency that the country uses. null if no currency found
     */
    @Override
    public String searchCurrency(String countryName)
    {
        for(Fiat fiat : fiats.values())
        {
            for(String country : fiat.getCountries())
            {
                if(country.contains(countryName))
                {
                    return fiat.getCurrency_code() + ": " + fiat.getCurrency_name();
                }
            }
        }

        return null;
    }

    /**
     * Prepares the long report into a single string.
     * Executes the pasting of the report by calling the output class.
     * @return the pasted report, whether as a link to pastebin.com (online) or the paste itself (offline)
     * @throws IllegalStateException if a conversion has not occurred yet
     */
    @Override
    public String createPasteToPost() throws Exception
    {
        /* check to see if there is something to paste */
        if(conversionReport.getFromCurrencyId() == null)
            throw new IllegalStateException("Nothing to paste! Please convert first");

        /* Create starting currency paste text */
        String fromCode = conversionReport.getFromCurrencyId();
        Fiat fromFiat = fiats.get(fromCode);
        String fromName = fromFiat.getCurrency_name();
        String[] fromCountries = fromFiat.getCountries();

        String fromPaste = "From " + fromCode + " - " + fromName + ":\nCountries:\n" +
                String.join(", ",fromCountries) + "\n\n";

        /* Create finishing currency paste text */
        String toCode = conversionReport.getToCurrencyId();
        Fiat toFiat = fiats.get(toCode);
        String toName = toFiat.getCurrency_name();
        String[] toCountries = toFiat.getCountries();

        String toPaste = "To " + toCode + " - " + toName + ":\nCountries:\n" +
                String.join(", ",toCountries) + "\n\n";

        /* Create conversion paste text */
        String convertPaste = "Conversion Rate: " + conversionReport.getConversionRate() + "\nStarting value: " +
                conversionReport.getStartValue() + "\nFinishing value: " + conversionReport.getFinishingValue();

        return output.postPaste(fromPaste + toPaste + convertPaste);
    }

    /**
     * Store info of current conversion; this is used in case a paste is made.
     * @param from the starting currency
     * @param to the finishing currency
     * @param amount the starting number
     * @param rateStr the conversion rate, as a String
     * @param convertedStr the converted number, as a String
     */
    protected void updateConversionInfo(String from, String to, String amount, String rateStr, String convertedStr)
    {
        conversionReport.setFromCurrencyId(from);
        conversionReport.setToCurrencyId(to);
        conversionReport.setConversionRate(rateStr);
        conversionReport.setStartValue(amount);
        conversionReport.setFinishingValue(convertedStr);
    }

    /**
     * Gets a cut of a given string.
     * @param string the string to cut
     * @param del where to cut the string
     * @param start where to start the cut
     * @return the cut string
     */
    @Override
    public String getSubstring(String string, String del, int start)
    {
        return string.substring(start, string.indexOf(del));
    }

    /**
     * Check if the input is valid. Needs to be a number that is over 0 and not too large.
     * @param input the given string to check
     */
    @Override
    public void validateUserInput(String input)
    {
        double amountDouble = Double.parseDouble(input);

        if(amountDouble <= 0)
            throw new IllegalArgumentException("Please enter a positive number.");
        else if(amountDouble > 10000000000000000000000000000.0)
            throw new IllegalArgumentException("Number is too large.");
    }

    /**
     * Checks if the currency is already in the list.
     * @param list the list of currencies
     * @param currency the currency to add in if not already in list
     * @return Whether the currency is there. True if currency is NOT in the list
     */
    @Override
    public boolean validateListViewCurrencyIsNew(ObservableList<String> list, String currency)
    {
        return !list.contains(currency);
    }

    /**
     * Checks if a currency has been found for the country.
     * @param currency string that will be a 'N/A' if a currency was not found. Otherwise, it'll be the currency
     * @return whether the country has an associated currency. True if currency found
     */
    @Override
    public boolean validateListViewCurrencyExists(String currency)
    {
        return !currency.equals("N/A");
    }

    /** Connects observer to this model. */
    @Override
    public void registerObserver(AudioObserver gameBoardObserver)
    {
        this.observers.add(gameBoardObserver);
    }

    /** Notifies observers when change has been made. */
    @Override
    public void updateObservers()
    {
        for(AudioObserver observer: observers)
        {
            observer.update();
        }
    }

    /** Checks status of audio. */
    @Override
    public boolean validateAudioStatus(MediaPlayer.Status status)
    {
        return status.equals(MediaPlayer.Status.PLAYING);
    }

    /** Makes sure the user's threshold input abides by the requirements (between 0.1 and 1.0). */
    @Override
    public boolean validateThreshold(String threshold)
    {
        double thresholdDouble = Double.parseDouble(threshold);

        boolean invalidThreshold = (thresholdDouble >= 1.0 || thresholdDouble <= 0.1);
        if(invalidThreshold)
            return false;

        this.threshold = thresholdDouble;
        return true;
    }

    /** Sees if the conversion being done has a rate lower than the threshold; true if lower. */
    @Override
    public boolean checkThresholdPassed(String rate)
    {
        double rateDouble = Double.parseDouble(rate);

        if(rateDouble < threshold)
            return true;

        return false;
    }

}
