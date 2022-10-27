package com.majorproject.model.queries.currencyscoop;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedHashMap;

/**
 * Input implementation that requires an internet connection. Real-time and accurate results.
 * Connects to CurrencyScoop api to retrieve required info.
 */
public class InputOnline implements Input
{
    private final Gson gson = new Gson();
    private final String key;
    private final HttpClient client = HttpClient.newBuilder().build();
    public InputOnline(String key)
    {
        this.key = key;
    }

    /**
     * Connect to get up-to-date info on all currencies in api. Stores it in a hashmap of Fiat classes.
     * @return the hashmap containing info on all given fiats
     */
    @Override
    public LinkedHashMap<String, Fiat> getCurrencies() throws Exception
    {
        try
        {
            String uri = "https://api.currencyscoop.com/v1/currencies?api_key=" + key;

            HttpRequest request = HttpRequest.newBuilder(new URI(uri)).GET().build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            GsonCs gsonCs = gson.fromJson(response.body(), GsonCs.class);

            Meta meta = gsonCs.getMeta();
            if(meta.getCode() == 200)
            {
                return gsonCs.getResponse().getFiats();
            }
            else
            {
                throw new Exception(meta.getCode() + ": " + meta.getError_detail());
            }

        }
        catch(Exception e)
        {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * converts given number into new currency using real-time rates from api.
     * @param from starting currency code
     * @param to ending currency code
     * @param amount number to convert
     * @return converted number
     */
    @Override
    public double getConvert(String from, String to, double amount) throws Exception
    {
        try
        {
            String uri = "https://api.currencyscoop.com/v1/convert?api_key=" +
                    key + "&from=" + from + "&to=" + to + "&amount=" + amount;

            HttpRequest request = HttpRequest.newBuilder(new URI(uri)).GET().build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            GsonCs gsonCs = gson.fromJson(response.body(), GsonCs.class);

            Meta meta = gsonCs.getMeta();
            if(meta.getCode() == 200)
            {
                return gsonCs.getResponse().getValue();
            }
            else
            {
                throw new Exception(meta.getCode() + ": " + meta.getError_detail());
            }
        }
        catch(Exception e)
        {
            throw new Exception(e.getMessage());
        }
    }
}
