package com.majorproject.view;

import com.majorproject.model.Model;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

/**
 * View class that handles the app's main buttons, including for adding currencies,
 * clearing the view's lists, clearing the cache and playing the theme song.
 */
public class MainButtonsHandler extends View
{
    /** Opens map window to select countries and add their currencies. */
    protected void handleAddCurrencies(Model model, WorldMapWindow worldMapWindow,
                          ListView<String> listView1, ListView<String> listView2)
    {
        worldMapWindow.displayWindow((Stage) listView1.getScene().getWindow());

        try
        {
            HashMap<String, String> currencies = model.getCurrencyInfo(
                    worldMapWindow.getWorldMapView().getSelectedCountries());
            for(Map.Entry<String, String> entry: currencies.entrySet())
            {
                String currency = entry.getValue();

                if(model.validateListViewCurrencyExists(currency))
                {
                    ObservableList<String> current = listView1.getItems();
                    if(model.validateListViewCurrencyIsNew(current, currency))
                    {
                        listView1.getItems().add(currency);
                        listView2.getItems().add(currency);
                    }
                    else
                    {
                        displayAlert("Currency for " + entry.getKey() + " already added: " +
                                model.getSubstring(currency, ":", 0));
                    }
                }
                else
                {
                    displayAlert("No currency found for " + entry.getKey());
                }
            }
        }
        catch(Exception e)
        {
            displayAlert(e.getMessage());
        }
    }

    /** Clears both lists entirely. */
    protected void handleClearLists(ListView<String> listView1, ListView<String> listView2)
    {
        listView1.getItems().clear();
        listView2.getItems().clear();
    }

    /** Clears cache. */
    protected void handleClearCache(Model model)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Rate Cache Clear");
        try
        {
            alert.setHeaderText(model.handleCacheClear());
            alert.showAndWait();
        }
        catch(Exception e)
        {
            displayAlert(e.getMessage());
        }
    }

    /** Plays / pauses audio. */
    protected void handleAudio(Model model)
    {
        model.playAudio();
    }

    /** Opens About window. */
    protected void handleAbout()
    {
        String header = "Input API: CurrencyScoop\nOutput API: Pastebin";
        String content =
                """
                Developer Name: Aidan Musa
                
                Sources:
                Theme Song:
                - DeoxysPrime 16/5/22 "Bubblaine - Super Mario Odyssey [OST]".
                https://www.youtube.com/watch?v=Xb3pJuBzOcY&ab_channel=DeoxysPrime
                
                Code:
                - Ali Faris 13/5/22 "JavaFX Copy text from alert".
                https://stackoverflow.com/a/45621264/13116149
                - Miss Chanandler Bong 14/5/22 "How to change the text of yes/no buttons in JavaFX 8 Alert dialogs".
                https://stackoverflow.com/a/49301730/13116149
                """;

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
