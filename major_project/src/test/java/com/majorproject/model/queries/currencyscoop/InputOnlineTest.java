package com.majorproject.model.queries.currencyscoop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InputOnlineTest
{
    /** Test with fake key. */
    @Test
    public void getCurrencies1()
    {
        InputOnline inputOnline = new InputOnline("fake_key");

        assertThrows(Exception.class, inputOnline::getCurrencies);
    }

    /** Test with real key. Commented due to:
     *  https://edstem.org/au/courses/7942/discussion/835807?answer=1882717
     */
//    @Test
//    public void getCurrencies2() throws Exception
//    {
//        InputOnline inputOnline = new InputOnline(System.getenv("INPUT_API_KEY"));
//
//        LinkedHashMap<String, Fiat> fiats = inputOnline.getCurrencies();
//
//        Assertions.assertNotNull(fiats.get("AUD"));
//
//        Fiat fiat = fiats.get("AUD");
//
//        assertThat(fiat.getCurrency_code(), is("AUD"));
//        assertThat(fiat.getCurrency_name(), is("Australian dollar"));
//    }

    /** Test with fake key. */
    @Test
    public void getConvert1()
    {
        InputOnline inputOnline = new InputOnline("fake_key");

        assertThrows(Exception.class, () -> inputOnline.getConvert("AUD", "USD", 50));
    }

    /** Test with real key. Commented due to:
     *  https://edstem.org/au/courses/7942/discussion/835807?answer=1882717
     */
//    @Test
//    public void getConvert2() throws Exception
//    {
//        InputOnline inputOnline = new InputOnline(System.getenv("INPUT_API_KEY"));
//
//        assertDoesNotThrow(() -> inputOnline.getConvert("AUD", "USD", 50));
//
//        double result = inputOnline.getConvert("AUD", "USD", 50);
//        assertTrue("Error", 40 >= result);
//        assertTrue("Error",  30  <= result);
//    }
}
