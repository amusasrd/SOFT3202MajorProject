package com.majorproject.model;

/**
 * Java class to store info related to the latest conversion by the user.
 * Used to allow easy access to info when pasting a report to Pastebin.
 */
public class Report
{
    private String fromCurrencyId;
    private String toCurrencyId;

    private String conversionRate;
    private String startValue;
    private String finishingValue;

    public String getFromCurrencyId()
    {
        return fromCurrencyId;
    }

    public void setFromCurrencyId(String fromCurrencyId)
    {
        this.fromCurrencyId = fromCurrencyId;
    }

    public String getToCurrencyId()
    {
        return toCurrencyId;
    }

    public void setToCurrencyId(String toCurrencyId)
    {
        this.toCurrencyId = toCurrencyId;
    }

    public String getConversionRate()
    {
        return conversionRate;
    }

    public void setConversionRate(String conversionRate)
    {
        this.conversionRate = conversionRate;
    }

    public String getStartValue()
    {
        return startValue;
    }

    public void setStartValue(String startValue)
    {
        this.startValue = startValue;
    }

    public String getFinishingValue()
    {
        return finishingValue;
    }

    public void setFinishingValue(String finishingValue)
    {
        this.finishingValue = finishingValue;
    }
}
