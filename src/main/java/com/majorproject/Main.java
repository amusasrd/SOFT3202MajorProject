package com.majorproject;

import com.majorproject.model.Model;
import com.majorproject.model.ModelCache;
import com.majorproject.model.ModelNoCache;
import com.majorproject.view.StartWindow;
import com.majorproject.view.ThemeSong;
import com.majorproject.model.queries.currencyscoop.Input;
import com.majorproject.model.queries.currencyscoop.InputOffline;
import com.majorproject.model.queries.currencyscoop.InputOnline;
import com.majorproject.model.queries.pastebin.Output;
import com.majorproject.model.queries.pastebin.OutputOffline;
import com.majorproject.model.queries.pastebin.OutputOnline;
import com.majorproject.view.MainWindow;
import com.majorproject.view.ThresholdWindow;
import javafx.application.Application;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application
{
    Model model;
    MainWindow mainWindow;

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage)
    {
        if(getParameters().getUnnamed().size() != 2)
        {
            System.out.println("2 parameters needed: online / offline\n");
            System.exit(-1);
        }

        String param1 = getParameters().getUnnamed().get(0);
        String param2 = getParameters().getUnnamed().get(1);
        initModel(param1, param2);


        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        URL file = classloader.getResource("Bubblaine Super Mario Odyssey Music-[AudioTrimmer.com].mp3");
        if(file == null)
        {
            System.out.println("Missing audio file!");
            System.exit(-8);
        }

        ThemeSong themeSong = new ThemeSong(model, file);
        StartWindow thresholdWindow = new ThresholdWindow(model);
        mainWindow = new MainWindow(model, themeSong, thresholdWindow);

        primaryStage.setTitle("Major Project App");
        primaryStage.setScene(mainWindow.getMainScene());
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(500);
        primaryStage.show();
    }

    private void initModel(String param1, String param2)
    {
        if(param1 == null || param2 == null || !param1.matches("offline|online") ||
                !param2.matches("offline|online"))
        {
            System.out.println("Invalid parameters!\n");
            System.exit(-5);
        }

        Output outputKey;
        Input inputKey;
        if(param2.equals("online"))
            outputKey = new OutputOnline(System.getenv("PASTEBIN_API_KEY"));
        else
            outputKey = new OutputOffline();

        if(param1.equals("online"))
        {
            inputKey = new InputOnline(System.getenv("INPUT_API_KEY"));
            model = new ModelCache(inputKey, outputKey);
        }
        else
        {
            inputKey = new InputOffline();
            model = new ModelNoCache(inputKey, outputKey);
        }
    }
}