package com.majorproject.model.queries.currencyscoop;

/**
 * This is a plain object to store all info on each currency provided through
 * https://api.currencyscoop.com/v1/currencies of the CurrencyScoop api.
 */
public class Fiat
{
    private String[] countries;
    private String currency_code;
    private String currency_name;
    private String decimal_units;

    public String[] getCountries()
    {
        return countries;
    }

    public void setCountries(String[] countries)
    {
        this.countries = countries;
    }

    public String getCurrency_code()
    {
        return currency_code;
    }

    public void setCurrency_code(String currency_code)
    {
        this.currency_code = currency_code;
    }

    public String getCurrency_name()
    {
        return currency_name;
    }

    public void setCurrency_name(String currency_name)
    {
        this.currency_name = currency_name;
    }

    public String getDecimal_units()
    {
        return decimal_units;
    }

    public void setDecimal_units(String decimal_units)
    {
        this.decimal_units = decimal_units;
    }
}
