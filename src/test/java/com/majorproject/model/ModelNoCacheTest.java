package com.majorproject.model;

import com.majorproject.model.queries.currencyscoop.Fiat;
import com.majorproject.model.queries.currencyscoop.Input;
import com.majorproject.model.queries.pastebin.Output;

import java.util.HashMap;
import java.util.LinkedHashMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.media.MediaPlayer;
import javafx.util.Pair;

import org.controlsfx.control.WorldMapView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.hamcrest.core.Is.is;

/** Test class for ModelNoCache, as well as for the ModelAbstract abstract class.
 *  Additionally, this class contains tests for threshold methods.
 */
public class ModelNoCacheTest
{
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

    /**
     * Test for handling of app audio. Observers need to be notified.
     * Relies on registerObserver() and updateObservers() to be correct.
     */
    @Test
    public void playAudio() throws Exception
    {
        LinkedHashMap<String, Fiat> fiats = new LinkedHashMap<>();
        fiats.put("test1", createFiatAud());
        fiats.put("test2", createFiatUsd());

        Input inputMock = mock(Input.class);
        Output outputMock = mock(Output.class);

        when(inputMock.getCurrencies()).thenReturn(fiats);

        Model model = new ModelNoCache(inputMock, outputMock);

        AudioObserver observerMock = mock(AudioObserver.class);

        model.registerObserver(observerMock);
        model.playAudio();

        verify(observerMock, times(1)).update();
    }

    /** Test to make sure the right currency is being returned for the country. */
    @Test
    public void getCurrencyInfo() throws Exception
    {
        LinkedHashMap<String, Fiat> fiats = new LinkedHashMap<>();
        fiats.put("test1", createFiatAud());
        fiats.put("test2", createFiatUsd());

        Input inputMock = mock(Input.class);
        Output outputMock = mock(Output.class);

        when(inputMock.getCurrencies()).thenReturn(fiats);

        Model model = new ModelNoCache(inputMock, outputMock);

        ObservableList<WorldMapView.Country> countryCodes = FXCollections.observableArrayList();
        countryCodes.add(WorldMapView.Country.AU);
        countryCodes.add(WorldMapView.Country.US);

        HashMap<String, String> items = model.getCurrencyInfo(countryCodes);
        assertThat(items.get("Australia"), is("AUD: Australian dollar"));
        assertThat(items.get("United States"), is("USD: United States dollar"));
    }

    /** Test to make sure searches for currencies are valid and not null. */
    @Test
    public void searchCurrency() throws Exception
    {
        LinkedHashMap<String, Fiat> fiats = new LinkedHashMap<>();
        fiats.put("test1", createFiatAud());
        fiats.put("test2", createFiatUsd());

        Input inputMock = mock(Input.class);
        Output outputMock = mock(Output.class);

        when(inputMock.getCurrencies()).thenReturn(fiats);

        Model model = new ModelNoCache(inputMock, outputMock);

        Assertions.assertNull(model.searchCurrency("Canada"));
        assertThat(model.searchCurrency("Australia"), is("AUD: Australian dollar"));
    }

    /** Test to check the correct paste is being compiled and sent. */
    @Test
    public void createPasteToPost() throws Exception
    {
        LinkedHashMap<String, Fiat> fiats = new LinkedHashMap<>();
        fiats.put("AUD", createFiatAud());
        fiats.put("USD", createFiatUsd());

        Input inputMock = mock(Input.class);
        Output outputMock = mock(Output.class);

        when(inputMock.getCurrencies()).thenReturn(fiats);

        Model model = new ModelNoCache(inputMock, outputMock);

//        need to convert first
        assertThrows(IllegalStateException.class, model::createPasteToPost);

        when(inputMock.getConvert("AUD", "USD", 5.755)).thenReturn(5.755);
        model.convertInput("AUD", "USD", "5.755");

        when(outputMock.postPaste(anyString())).thenReturn("Test");
        assertThat(model.createPasteToPost(), is("Test"));

        String report = """
        From AUD - Australian dollar:
        Countries:
        Australia
         
        To USD - United States dollar:
        Countries:
        United States
         
        Conversion Rate: 1.0
        Starting value: 5.755
        Finishing value: 5.755""";
        verify(outputMock, times(1)).postPaste(report);
    }

    /** Test to see update is correct to report. */
    @Test
    public void updateConversionInfo() throws Exception
    {
        LinkedHashMap<String, Fiat> fiats = new LinkedHashMap<>();
        fiats.put("test1", createFiatAud());
        fiats.put("test2", createFiatUsd());

        Input inputMock = mock(Input.class);
        Output outputMock = mock(Output.class);

        when(inputMock.getCurrencies()).thenReturn(fiats);

        Model model = new ModelNoCache(inputMock, outputMock);

        assertThat(model.searchCurrency("Australia"), is("AUD: Australian dollar"));

    }

    /** Test to see substring is correct. */
    @Test
    public void getSubstring() throws Exception
    {
        LinkedHashMap<String, Fiat> fiats = new LinkedHashMap<>();
        fiats.put("test1", createFiatAud());
        fiats.put("test2", createFiatUsd());

        Input inputMock = mock(Input.class);
        Output outputMock = mock(Output.class);

        when(inputMock.getCurrencies()).thenReturn(fiats);

        Model model = new ModelNoCache(inputMock, outputMock);

        String test = "Test. This";
        String del = ".";
        int start = 0;

        assertThat(model.getSubstring(test, del, start), is("Test"));
    }

    /** Test to see exceptions are thrown for invalid numbers. */
    @Test
    public void validateUserInput() throws Exception
    {
        LinkedHashMap<String, Fiat> fiats = new LinkedHashMap<>();
        fiats.put("test1", createFiatAud());
        fiats.put("test2", createFiatUsd());

        Input inputMock = mock(Input.class);
        Output outputMock = mock(Output.class);

        when(inputMock.getCurrencies()).thenReturn(fiats);

        Model model = new ModelNoCache(inputMock, outputMock);

        String test1 = "0";
        assertThrows(IllegalArgumentException.class, () -> model.validateUserInput(test1));
        String test2 = "-0.1111";
        assertThrows(IllegalArgumentException.class, () -> model.validateUserInput(test2));
        String test3 = "-44236757575211";
        assertThrows(IllegalArgumentException.class, () -> model.validateUserInput(test3));
        String test4 = "4423675673763737367609876542211";
        assertThrows(IllegalArgumentException.class, () -> model.validateUserInput(test4));
    }

    /** Test if the filtering of items is correct. */
    @Test
    public void validateListViewCurrencyIsNew() throws Exception
    {
        LinkedHashMap<String, Fiat> fiats = new LinkedHashMap<>();
        fiats.put("test1", createFiatAud());
        fiats.put("test2", createFiatUsd());

        Input inputMock = mock(Input.class);
        Output outputMock = mock(Output.class);

        when(inputMock.getCurrencies()).thenReturn(fiats);

        Model model = new ModelNoCache(inputMock, outputMock);

        ObservableList<String> countryCodes = FXCollections.observableArrayList();
        countryCodes.add("Test1");
        countryCodes.add("Test2");

        boolean testFalse = model.validateListViewCurrencyIsNew(countryCodes, "Test1");
        assertThat(testFalse, is(false));

        boolean testTrue = model.validateListViewCurrencyIsNew(countryCodes, "Test3");
        assertThat(testTrue, is(true));
    }

    /** Test if the right string is being filtered. */
    @Test
    public void validateListViewCurrencyExists() throws Exception
    {
        LinkedHashMap<String, Fiat> fiats = new LinkedHashMap<>();
        fiats.put("test1", createFiatAud());
        fiats.put("test2", createFiatUsd());

        Input inputMock = mock(Input.class);
        Output outputMock = mock(Output.class);

        when(inputMock.getCurrencies()).thenReturn(fiats);

        Model model = new ModelNoCache(inputMock, outputMock);

        boolean testTrue = model.validateListViewCurrencyExists("Canada");
        assertThat(testTrue, is(true));

        boolean testTrue2 = model.validateListViewCurrencyExists("abadfedsfsd");
        assertThat(testTrue2, is(true));

        boolean testFalse = model.validateListViewCurrencyExists("N/A");
        assertThat(testFalse, is(false));

        boolean testTrue3 = model.validateListViewCurrencyExists("n/A");
        assertThat(testTrue3, is(true));

        boolean testTrue4 = model.validateListViewCurrencyExists("N/a");
        assertThat(testTrue4, is(true));
    }

    /** Test if observers are correctly added. */
    @Test
    public void registerObserver() throws Exception
    {
        LinkedHashMap<String, Fiat> fiats = new LinkedHashMap<>();
        fiats.put("test1", createFiatAud());
        fiats.put("test2", createFiatUsd());

        Input inputMock = mock(Input.class);
        Output outputMock = mock(Output.class);

        when(inputMock.getCurrencies()).thenReturn(fiats);

        Model model = new ModelNoCache(inputMock, outputMock);

        AudioObserver observerMock1 = mock(AudioObserver.class);
        AudioObserver observerMock2 = mock(AudioObserver.class);

        model.registerObserver(observerMock1);
        model.updateObservers();

        verify(observerMock1, times(1)).update();
        verify(observerMock2, never()).update();
    }

    /** Test if observers are correctly notified of change. */
    @Test
    public void updateObservers() throws Exception
    {
        LinkedHashMap<String, Fiat> fiats = new LinkedHashMap<>();
        fiats.put("test1", createFiatAud());
        fiats.put("test2", createFiatUsd());

        Input inputMock = mock(Input.class);
        Output outputMock = mock(Output.class);

        when(inputMock.getCurrencies()).thenReturn(fiats);

        Model model = new ModelNoCache(inputMock, outputMock);

        AudioObserver observerMock1 = mock(AudioObserver.class);
        AudioObserver observerMock2 = mock(AudioObserver.class);

        model.registerObserver(observerMock1);
        model.registerObserver(observerMock2);
        model.updateObservers();

        verify(observerMock1, times(1)).update();
        verify(observerMock2, times(1)).update();
    }

    /** Test if correct result and rate are given. */
    @Test
    public void convertInput() throws Exception
    {
        LinkedHashMap<String, Fiat> fiats = new LinkedHashMap<>();
        fiats.put("test1", createFiatAud());
        fiats.put("test2", createFiatUsd());

        Input inputMock = mock(Input.class);
        Output outputMock = mock(Output.class);

        when(inputMock.getCurrencies()).thenReturn(fiats);

        Model model = new ModelNoCache(inputMock, outputMock);

        when(inputMock.getConvert("AUD", "USD", 5.755)).thenReturn(5.755);
        Pair<String, String> pair = model.convertInput("AUD", "USD", "5.755");
        assertThat(pair.getKey(), is("1.0"));
        assertThat(pair.getValue(), is("5.755"));

        when(inputMock.getConvert("AUD", "USD", 5000000.0)).thenReturn(10000000.0);
        Pair<String, String> pair2 = model.convertInput("AUD", "USD", "5000000.0");
        assertThat(pair2.getKey(), is("2.0"));
        assertThat(pair2.getValue(), is("1.0E7"));

        verify(inputMock, times(1)).getConvert("AUD", "USD", 5000000.0);
    }

    /** Test if correct string is returned. */
    @Test
    public void handleCacheClear() throws Exception
    {
        LinkedHashMap<String, Fiat> fiats = new LinkedHashMap<>();
        fiats.put("test1", createFiatAud());
        fiats.put("test2", createFiatUsd());

        Input inputMock = mock(Input.class);
        Output outputMock = mock(Output.class);

        when(inputMock.getCurrencies()).thenReturn(fiats);

        Model model = new ModelNoCache(inputMock, outputMock);

        String test = "No caching is done with the offline api.";
        assertThat(model.handleCacheClear(), is(test));
    }

    /** Test if correct string is returned. */
    @Test
    public void validateRateInCache() throws Exception
    {
        LinkedHashMap<String, Fiat> fiats = new LinkedHashMap<>();
        fiats.put("test1", createFiatAud());
        fiats.put("test2", createFiatUsd());

        Input inputMock = mock(Input.class);
        Output outputMock = mock(Output.class);

        when(inputMock.getCurrencies()).thenReturn(fiats);

        Model model = new ModelNoCache(inputMock, outputMock);

        assertThat(model.validateRateInCache("test", "test"), is(false));
    }

    /** Test to make sure correct status of audio is returned. */
    @Test
    public void validateAudioStatus() throws Exception
    {
        LinkedHashMap<String, Fiat> fiats = new LinkedHashMap<>();
        fiats.put("test1", createFiatAud());
        fiats.put("test2", createFiatUsd());

        Input inputMock = mock(Input.class);
        Output outputMock = mock(Output.class);

        when(inputMock.getCurrencies()).thenReturn(fiats);

        Model model = new ModelNoCache(inputMock, outputMock);

        assertThat(model.validateAudioStatus(MediaPlayer.Status.PLAYING), is(true));
        assertThat(model.validateAudioStatus(MediaPlayer.Status.PAUSED), is(false));
    }

    /** Test to make sure threshold is correctly identified to be used. */
    @Test
    public void validateThreshold() throws Exception
    {
        LinkedHashMap<String, Fiat> fiats = new LinkedHashMap<>();
        fiats.put("test1", createFiatAud());
        fiats.put("test2", createFiatUsd());

        Input inputMock = mock(Input.class);
        Output outputMock = mock(Output.class);

        when(inputMock.getCurrencies()).thenReturn(fiats);

        Model model = new ModelNoCache(inputMock, outputMock);

        assertThat(model.validateThreshold("0"), is(false));
        assertThat(model.validateThreshold("10000000"), is(false));
        assertThat(model.validateThreshold("-100000"), is(false));
        assertThat(model.validateThreshold("1.0"), is(false));
        assertThat(model.validateThreshold("0.1"), is(false));
        assertThat(model.validateThreshold("0.5"), is(true));
    }

    /** Test to see if the threshold value has been passed by the conversion rate. */
    @Test
    public void checkThresholdPassed() throws Exception
    {
        LinkedHashMap<String, Fiat> fiats = new LinkedHashMap<>();
        fiats.put("test1", createFiatAud());
        fiats.put("test2", createFiatUsd());

        Input inputMock = mock(Input.class);
        Output outputMock = mock(Output.class);

        when(inputMock.getCurrencies()).thenReturn(fiats);

        Model model = new ModelNoCache(inputMock, outputMock);
        model.validateThreshold("0.5");

        assertThat(model.checkThresholdPassed("1.11"), is(false));
        assertThat(model.checkThresholdPassed("0.2"), is(true));
    }
}
