package com.majorproject.model.queries.currencyscoop;

import java.util.LinkedHashMap;

/**
 * Class to store all query related info when using the CurrencyScoop api.
 * It is used by Gson as a nested object of GsonCs.
 */
public class Response
{
    private double amount;
    private double value;

    private String date;
    private String from;
    private String timestamp;
    private String to;
    private LinkedHashMap<String, Fiat> fiats;

    public double getAmount()
    {
        return amount;
    }

    public void setAmount(double amount)
    {
        this.amount = amount;
    }

    public double getValue()
    {
        return value;
    }

    public void setValue(double value)
    {
        this.value = value;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getFrom()
    {
        return from;
    }

    public void setFrom(String from)
    {
        this.from = from;
    }

    public String getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(String timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getTo()
    {
        return to;
    }

    public void setTo(String to)
    {
        this.to = to;
    }

    public LinkedHashMap<String, Fiat> getFiats()
    {
        return fiats;
    }

    public void setFiats(LinkedHashMap<String, Fiat> fiats)
    {
        this.fiats = fiats;
    }
}
