package com.majorproject.view;

import com.majorproject.model.Model;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.Optional;

/**
 * The class responsible for prompting the user to input a threshold value for the app.
 * The window pops up before the main window is seen.
 * It prevents progressing to the main window until a valid value is provided.
 */
public class ThresholdWindow extends View implements StartWindow
{
    private String threshold;
    private final Model model;

    public ThresholdWindow(Model model)
    {
        this.model = model;
    }

    public void displayWindow()
    {
        Stage stage = buildStage();
        stage.showAndWait();
    }

    private Stage buildStage()
    {
        /* create window showing map of countries to select and get currencies */
        Stage stage = new Stage();
        stage.setTitle("Threshold");
        stage.setScene(buildScene(stage));
        stage.setWidth(500);
        stage.setHeight(250);
        stage.setResizable(false);

        return stage;
    }

    private Scene buildScene(Stage stage)
    {
        Label lblExplainThreshold = new Label("A threshold is required to run this app. " +
                "A conversion rate that is lower than the threshold will notify the user.");
        lblExplainThreshold.setWrapText(true);
        Label lblGetThreshold = new Label("Please enter a threshold value between 0.1 and 1.0: ");

        TextField txtThreshold = new TextField();
        Button btnAdd = new Button("Add Threshold");

        HBox hboxInput = new HBox(txtThreshold, btnAdd);
        hboxInput.setSpacing(10);
        hboxInput.setAlignment(Pos.CENTER);

        VBox vbox = new VBox();
        vbox.getChildren().addAll(lblExplainThreshold, lblGetThreshold, hboxInput);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(0, 10, 15, 10));

        handleButtons(txtThreshold, btnAdd, stage);

        return (new Scene(vbox));
    }

    private void handleButtons(TextField txtThreshold, Button btnAdd, Stage stage)
    {
        btnAdd.setOnAction(event ->
        {
            try
            {
                threshold = txtThreshold.getText();

                if(model.validateThreshold(threshold))
                    stage.close();
                else
                    displayAlert("Invalid threshold value.");
            }
            catch(IllegalArgumentException e)
            {
                displayAlert("Please enter a threshold value.");
            }
            catch(Exception e)
            {
                displayAlert(e.getMessage());
            }
        });

        stage.setOnCloseRequest(e ->
        {
            Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION);
            exitAlert.setTitle("Exit Warning");
            exitAlert.setHeaderText("This will exit the app, as a threshold is required to run the app. " +
                    "Are you sure you want to do this?");

            /* The following was modified based on https://stackoverflow.com/a/49301730/13116149 */
            ((Button) exitAlert.getDialogPane()
                    .lookupButton(ButtonType.OK)).setText("Exit");
            /* end of copied code */

            /*
             * The following was modified based on
             * https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/Dialog.html
             */
            Optional<ButtonType> result = exitAlert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK)
                /* end of copied code */
            {
                Platform.exit();
            }
            else if(result.isPresent() && result.get() == ButtonType.CANCEL)
            {
                e.consume();
            }
        });
    }
}
