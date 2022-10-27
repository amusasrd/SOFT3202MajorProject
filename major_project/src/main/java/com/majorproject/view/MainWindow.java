package com.majorproject.view;

import com.majorproject.model.AudioObserver;
import com.majorproject.model.Model;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

/**
 * The main view class that builds and brings everything together for the starting window.
 * Adds all elements into the main scene to be displayed.
 * It also listens for updates to the theme song.
 */
public class MainWindow extends View implements AudioObserver
{
    WorldMapWindow worldMapWindow;
    private final Scene mainScene;

    ThemeSong themeSong;

    public MainWindow(Model model, ThemeSong themeSong, StartWindow startWindow)
    {
        model.registerObserver(this);

        ListView<String> currencyList1 = new ListView<>();
        currencyList1.setPrefWidth(300);
        ListView<String> currencyList2 = new ListView<>();
        currencyList2.setPrefWidth(300);

        worldMapWindow = new WorldMapWindow();

        startWindow.displayWindow();

        mainScene = buildScene(model, currencyList1, currencyList2);

        this.themeSong = themeSong;
        update();
    }

    private Scene buildScene(Model model, ListView<String> currencyList1, ListView<String> currencyList2)
    {
        final Scene mainScene;
        ButtonMenu buttonMenu = new ButtonMenu(currencyList1, currencyList2, model, worldMapWindow);
        TitlePane titlePane = new TitlePane();
        MainButtonsPane mainButtonsPane = new MainButtonsPane(currencyList1, currencyList2, model, worldMapWindow);
        ListPane listPane = new ListPane(currencyList1, currencyList2);
        ConvertPane convertPane = new ConvertPane(model, currencyList1, currencyList2);

        MenuBar menuBar = buttonMenu.getMenuBar();
        HBox hboxTitle = titlePane.getTitle();
        HBox hboxmainBtns = mainButtonsPane.getPane();
        HBox hboxLists = listPane.getListPane();
        VBox hboxConvert = convertPane.getPane();

        VBox vbox = new VBox();
        vbox.setSpacing(20);
        vbox.getChildren().addAll(menuBar, hboxTitle, hboxmainBtns, hboxLists, hboxConvert);

        ScrollPane spMain = new ScrollPane(vbox);
        spMain.setFitToHeight(true);
        spMain.setFitToWidth(true);

        mainScene = new Scene(spMain);
        return mainScene;
    }

    /** Returns the main window's scene. */
    public Scene getMainScene()
    {
        return mainScene;
    }

    /** Calls on ThemeSong to handle audio. */
    @Override
    public void update()
    {
        themeSong.playAudio();
    }
}