package com.majorproject.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.WorldMapView;

/**
 * View Class that builds and displays a window of the world map to select countries.
 * Closing the window or pressing the 'Add Currencies' button adds the currencies for
 * the selected countries, if found.
 */
public class WorldMapWindow extends View
{
    private final WorldMapView worldMapView = new WorldMapView();

    public void displayWindow(Stage primaryStage)
    {
       Stage stage = buildStage(primaryStage);
       stage.showAndWait();
    }
    private Stage buildStage(Stage primaryStage)
    {
        /* create window showing map of countries to select and get currencies */
        Stage stage = new Stage();
        stage.setTitle("World Map");
        stage.setScene(buildScene());
        stage.setWidth(1000);
        stage.setHeight(650);
        stage.setX(primaryStage.getX() - 100);
        stage.setY(10);
        stage.setMinWidth(600);
        stage.setMinHeight(400);

        return stage;
    }

    private Scene buildScene()
    {
        VBox vbox = new VBox();

        Button btnClose = new Button("Add Currencies");
        HBox hboxButtons = new HBox(btnClose);
        hboxButtons.setSpacing(10);
        hboxButtons.setAlignment(Pos.CENTER);

        vbox.getChildren().addAll(hboxButtons, worldMapView);
        VBox.setVgrow(worldMapView, Priority.ALWAYS );

        btnClose.setOnAction(event ->
        {
            Stage stage = (Stage) btnClose.getScene().getWindow();
            stage.close();
        });

        return(new Scene(vbox));
    }

    /** Returns the WorldMapView class that contains the user's selected countries. */
    public WorldMapView getWorldMapView()
    {
        return worldMapView;
    }
}
