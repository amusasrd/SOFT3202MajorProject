package com.majorproject.model.queries.currencyscoop;

import java.util.LinkedHashMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class InputOfflineTest
{
    /** Test to see offline.json is used. */
    @Test
    public void getCurrencies() throws Exception
    {
        InputOffline inputOffline = new InputOffline();

        LinkedHashMap<String, Fiat> fiats = inputOffline.getCurrencies();

        Assertions.assertNotNull(fiats.get("AUD"));

        Fiat fiat = fiats.get("AUD");

        assertThat(fiat.getCurrency_code(), is("AUD"));
        assertThat(fiat.getCurrency_name(), is("Australian dollar"));
    }

    /** Test to see if the same double is returned. */
    @Test
    public void getConvert()
    {
        InputOffline inputOffline = new InputOffline();

        assertThat(inputOffline.getConvert("t", "est", 5.0), is(5.0));
    }

}
