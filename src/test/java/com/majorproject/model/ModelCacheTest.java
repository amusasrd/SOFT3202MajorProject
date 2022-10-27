package com.majorproject.model;

import com.majorproject.model.queries.currencyscoop.Fiat;
import com.majorproject.model.queries.currencyscoop.Input;
import com.majorproject.model.queries.pastebin.Output;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.stubbing.Answer;

import java.util.LinkedHashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class ModelCacheTest
{
    private String dbName = "test.db";

    private Fiat createFiatAud()
    {
        String[] countries = {"Australia"};

        Fiat fiat = new Fiat();
        fiat.setCountries(countries);
        fiat.setCurrency_code("AUD");
        fiat.setCurrency_name("Australian dollar");
        fiat.setDecimal_units("2");

        return fiat;
    }

    private Fiat createFiatUsd()
    {
        String[] countries = {"United States"};

        Fiat fiat = new Fiat();
        fiat.setCountries(countries);
        fiat.setCurrency_code("USD");
        fiat.setCurrency_name("United States dollar");
        fiat.setDecimal_units("2");

        return fiat;
    }

    /** Test to see if correct values are returned for rate and result. */
    @Test
    public void convertInput() throws Exception
    {
        try(MockedStatic<RatesDb> mock = mockStatic(RatesDb.class))
        {
            LinkedHashMap<String, Fiat> fiats = new LinkedHashMap<>();
            fiats.put("test1", createFiatAud());
            fiats.put("test2", createFiatUsd());

            Input inputMock = mock(Input.class);
            Output outputMock = mock(Output.class);

            when(inputMock.getCurrencies()).thenReturn(fiats);

            ModelCache model = new ModelCache(inputMock, outputMock);

            /* mock static methods of RatesDb */
            mock.when(() -> RatesDb.getRateDb("AUDUSD"))
                    .thenReturn("1.0");
            mock.when(() -> RatesDb.checkRateInDb("AUDUSD"))
                    .thenReturn(false);
            /*
             * The following was modified based on
             * https://stackoverflow.com/a/64464855/13116149
             */
            mock.when(() -> RatesDb.insertRateDb("AUDUSD", 1.0, false))
                    .thenAnswer((Answer<Void>) invocation -> null);
            /* end of copied code */


            when(inputMock.getConvert("AUD", "USD", 5.755)).thenReturn(5.755);
            Pair<String, String> pair = model.convertInput("AUD", "USD", "5.755");
            assertThat(pair.getKey(), is("1.0"));
            assertThat(pair.getValue(), is("5.755"));

            when(inputMock.getConvert("AUD", "USD", 5000000.0)).thenReturn(5000000.0);
            Pair<String, String> pair2 = model.convertInput("AUD", "USD", "5000000.0");
            assertThat(pair2.getKey(), is("1.0"));
            assertThat(pair2.getValue(), is("5000000.0"));

            verify(inputMock, times(1)).getConvert("AUD", "USD", 5000000.0);
        }

    }

    /** Test that the database method is called and correct string is given. */
    @Test
    public void handleCacheClear() throws Exception
    {
        try(MockedStatic<RatesDb> mock = mockStatic(RatesDb.class))
        {
            mock.when(RatesDb::removeDb).thenAnswer((Answer<Void>) invocation -> null);

            LinkedHashMap<String, Fiat> fiats = new LinkedHashMap<>();
            fiats.put("test1", createFiatAud());
            fiats.put("test2", createFiatUsd());

            Input inputMock = mock(Input.class);
            Output outputMock = mock(Output.class);

            when(inputMock.getCurrencies()).thenReturn(fiats);

            ModelCache model = new ModelCache(inputMock, outputMock);

            model.handleCacheClear();
            mock.verify(RatesDb::removeDb, times(1));

            assertThat(model.handleCacheClear(), is("Cache Cleared."));
        }
    }

    /** Test to see if it confirms that the rate is found in the database. */
    @Test
    public void validateRateInCache() throws Exception
    {
        try(MockedStatic<RatesDb> mock = mockStatic(RatesDb.class))
        {
            mock.when(() -> RatesDb.createDb(dbName)).thenAnswer((Answer<Void>) invocation -> null);
            mock.when(() -> RatesDb.checkRateInDb("testtest"))
                    .thenReturn(true);

            LinkedHashMap<String, Fiat> fiats = new LinkedHashMap<>();
            fiats.put("test1", createFiatAud());
            fiats.put("test2", createFiatUsd());

            Input inputMock = mock(Input.class);
            Output outputMock = mock(Output.class);

            when(inputMock.getCurrencies()).thenReturn(fiats);

            ModelCache model = new ModelCache(inputMock, outputMock);

            model.validateRateInCache("test", "test");
            mock.verify(() -> RatesDb.createDb(dbName), times(1));

            assertThat(model.validateRateInCache("test", "test"), is(true));
        }
    }

    /** Test to see if overwrite was correctly changed to true. */
    @Test
    public void setToOverwriteCache() throws Exception
    {
        try(MockedStatic<RatesDb> mock = mockStatic(RatesDb.class))
        {
            LinkedHashMap<String, Fiat> fiats = new LinkedHashMap<>();
            fiats.put("test1", createFiatAud());
            fiats.put("test2", createFiatUsd());

            Input inputMock = mock(Input.class);
            Output outputMock = mock(Output.class);

            when(inputMock.getCurrencies()).thenReturn(fiats);
            when(inputMock.getConvert(anyString(), anyString(), anyDouble())).thenReturn(50.0);

            ModelCache model = new ModelCache(inputMock, outputMock);

            /* mock static methods of RatesDb */
            mock.when(() -> RatesDb.getRateDb("TEST"))
                    .thenReturn("1.0");
            mock.when(() -> RatesDb.checkRateInDb("TEST"))
                    .thenReturn(true);
            /*
             * The following was modified based on
             * https://stackoverflow.com/a/64464855/13116149
             */
            mock.when(() -> RatesDb.insertRateDb("TEST", 5.0, false))
                    .thenAnswer((Answer<Void>) invocation -> null);
            mock.when(() -> RatesDb.insertRateDb("TEST", 5.0, true))
                    .thenAnswer((Answer<Void>) invocation -> null);
            /* end of copied code */

            model.convertInput("TE", "ST", "50");

            mock.verify(() -> RatesDb.checkRateInDb("TEST"), times(1));
            mock.verify(() -> RatesDb.getRateDb("TEST"), times(1));
            mock.verify(() -> RatesDb.insertRateDb("TEST", 5.0, false),
                    never());
            mock.verify(() -> RatesDb.insertRateDb("TEST", 5.0, true),
                    never());

            model.setToOverwriteCache();
            model.convertInput("TE", "ST", "50");

            mock.verify(() -> RatesDb.checkRateInDb("TEST"), times(1));
            mock.verify(() -> RatesDb.getRateDb("TEST"), times(1));
            mock.verify(() -> RatesDb.insertRateDb("TEST", 5.0, false),
                    never());
            mock.verify(() -> RatesDb.insertRateDb(anyString(), anyDouble(), eq(true)),
                    times(1));
        }
    }
}
