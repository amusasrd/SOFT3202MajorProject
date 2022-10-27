package com.majorproject.model.queries.currencyscoop;

/**
 * Class to store all connection related info when using the CurrencyScoop api.
 * It is used by Gson as a nested object of GsonCs.
 */
public class Meta
{
    private int code;
    private String disclaimer;
    private String error_detail;
    private String error_type;

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public String getDisclaimer()
    {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer)
    {
        this.disclaimer = disclaimer;
    }

    public String getError_detail()
    {
        return error_detail;
    }

    public void setError_detail(String error_detail)
    {
        this.error_detail = error_detail;
    }

    public String getError_type()
    {
        return error_type;
    }

    public void setError_type(String error_type)
    {
        this.error_type = error_type;
    }
}
