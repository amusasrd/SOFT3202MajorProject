package com.majorproject.view;

import com.majorproject.model.Model;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

/**
 * The view class that builds the menu bar for the top of the window.
 * Includes all the app's main buttons of adding currencies, clearing the list view,
 * clearing the cache and playing / pausing the theme song.
 * Additionally, it allows the user to view the 'About' page under 'Misc'.
 */
public class ButtonMenu extends View
{
    MainButtonsHandler mainButtonsHandler;
    private final MenuBar menuBar;

    public ButtonMenu(ListView<String> listView1, ListView<String> listView2,
                      Model model, WorldMapWindow worldMapWindow)
    {
        mainButtonsHandler = new MainButtonsHandler();

        Menu currencyMenu = buildCurrencyMenu(listView1, listView2, model, worldMapWindow);
        Menu cacheMenu = buildCacheMenu(model);
        Menu audioMenu = buildAudioMenu(model);
        Menu aboutMenu = buildAboutMenu();

        this.menuBar = new MenuBar();
        menuBar.getMenus().addAll(currencyMenu, cacheMenu, audioMenu, aboutMenu);
    }

    private Menu buildCurrencyMenu(ListView<String> listView1, ListView<String> listView2,
                                   Model model, WorldMapWindow worldMapWindow)
    {
        Menu menu = new Menu("Currencies");

        final MenuItem itemAdd = new MenuItem("Add Currencies");
        itemAdd.setOnAction((event) ->
                mainButtonsHandler.handleAddCurrencies(model, worldMapWindow, listView1, listView2));

        MenuItem itemClearLists = new MenuItem("Clear Lists");
        itemClearLists.setOnAction((event) ->
                mainButtonsHandler.handleClearLists(listView1, listView2));

        menu.getItems().addAll(itemAdd, itemClearLists);

        return menu;
    }

    private Menu buildCacheMenu(Model model)
    {
        Menu menu = new Menu("Cache");

        MenuItem itemClearCache = new MenuItem("Clear Cache");
        itemClearCache.setOnAction((event) -> mainButtonsHandler.handleClearCache(model));

        menu.getItems().addAll(itemClearCache);

        return menu;
    }

    private Menu buildAudioMenu(Model model)
    {
        Menu menu = new Menu("Audio");

        MenuItem itemAudio = new MenuItem("Play / Pause");
        itemAudio.setOnAction((event) -> mainButtonsHandler.handleAudio(model));

        menu.getItems().addAll(itemAudio);

        return menu;
    }

    private Menu buildAboutMenu()
    {
        Menu menu = new Menu("Misc");

        MenuItem itemAbout = new MenuItem("About");
        itemAbout.setOnAction((event) -> mainButtonsHandler.handleAbout());

        menu.getItems().addAll(itemAbout);

        return menu;
    }

    /** Returns the built top menu bar */
    public MenuBar getMenuBar()
    {
        return menuBar;
    }
}
