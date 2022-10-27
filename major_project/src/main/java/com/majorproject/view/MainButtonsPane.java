package com.majorproject.view;

import com.majorproject.model.Model;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

/**
 * The view class that creates the pane to display the app's main buttons.
 * These are for adding currencies, clearing the list view,
 * clearing the cache and playing / pausing the theme song.
 */
public class MainButtonsPane extends View
{
    private final MainButtonsHandler mainButtonsHandler;
    HBox hbox;
    public MainButtonsPane(ListView<String> currencyList1, ListView<String> currencyList2,
                           Model model, WorldMapWindow worldMapWindow)
    {
        mainButtonsHandler = new MainButtonsHandler();

        Button btnAdd = new Button("Add Currencies");
        Button btnClearLists = new Button("Clear Lists");
        Button btnClearCache = new Button("Clear Cache");
        Button btnAudio = new Button("Play / Pause Audio");

        hbox = new HBox();
        hbox.getChildren().addAll(btnAdd, btnClearLists, btnClearCache, btnAudio);
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(10);

        handleButtons(currencyList1, currencyList2,
                btnAdd, btnClearLists, btnClearCache, btnAudio,
                worldMapWindow, model);
    }

    private void handleButtons(ListView<String> listView1, ListView<String> listView2,
                               Button btnAdd, Button btnClearLists, Button btnClearCache, Button btnAudio,
                               WorldMapWindow worldMapWindow, Model model)
    {
        btnAdd.setOnAction(event -> mainButtonsHandler.handleAddCurrencies(model, worldMapWindow, listView1, listView2));

        btnClearLists.setOnAction(event -> mainButtonsHandler.handleClearLists(listView1, listView2));

        btnClearCache.setOnAction(event -> mainButtonsHandler.handleClearCache(model));

        btnAudio.setOnAction(event -> mainButtonsHandler.handleAudio(model));
    }

    /** Returns the main button pane. */
    public HBox getPane()
    {
        return hbox;
    }
}
