package com.majorproject.model.queries.currencyscoop;

/**
 * This is a plain object used by Gson to convert json from the body of a response.
 * Needed when connecting to CurrencyScoop api.
 */
public class GsonCs
{
    private Meta meta;
    private Response response;

    public Meta getMeta()
    {
        return meta;
    }

    public void setMeta(Meta meta)
    {
        this.meta = meta;
    }

    public Response getResponse()
    {
        return response;
    }

    public void setResponse(Response response)
    {
        this.response = response;
    }
}
