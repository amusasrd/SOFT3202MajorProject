package com.majorproject.model;

import javafx.collections.ObservableList;
import javafx.scene.media.MediaPlayer;
import javafx.util.Pair;
import org.controlsfx.control.WorldMapView;
import java.util.HashMap;

/**
 * Interface for app engine.
 */
public interface Model
{
    void playAudio();

    HashMap<String, String> getCurrencyInfo(ObservableList<WorldMapView.Country> countryCodes);

    String searchCurrency(String countryName);

    void validateUserInput(String input);

    Pair<String, String> convertInput(String from, String to, String amount) throws Exception;

    String createPasteToPost() throws Exception;

    String getSubstring(String string, String del, int start);

    boolean validateListViewCurrencyExists(String currency);

    boolean validateListViewCurrencyIsNew(ObservableList<String> list, String currency);

    String handleCacheClear() throws Exception;

    boolean validateRateInCache(String from, String to) throws Exception;

    void registerObserver(AudioObserver gameBoardObserver);

    void updateObservers();

    boolean validateAudioStatus(MediaPlayer.Status status);

    boolean validateThreshold(String threshold);

    boolean checkThresholdPassed(String rate);
}
