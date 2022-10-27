package com.majorproject.view;

import com.majorproject.model.Model;
import com.majorproject.model.ModelCache;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import javafx.util.Pair;

import java.util.Optional;

/**
 * View class that creates the ui pane for the bottom section of the window that allows
 * the user to input a number, convert it to be displayed, and paste it if desired.
 */
public class ConvertPane extends View
{
    private final Model model;
    VBox vbox;

    public ConvertPane(Model model, ListView<String> currencyList1, ListView<String> currencyList2)
    {
        this.model = model;

        Label lblRate = new Label("Rate: ");
        Label lblResult = new Label("Result: ");
        lblRate.setMinWidth(200);
        lblResult.setMinWidth(200);

        HBox hboxOutput = new HBox();
        hboxOutput.getChildren().addAll(lblRate, lblResult);
        hboxOutput.setAlignment(Pos.CENTER);

        Label lblAmount = new Label("Amount: ");
        TextField txtAmount = new TextField();
        Button btnConvert = new Button("Convert");
        Button btnPaste = new Button("Post to Pastebin");
        
        HBox hboxActions = new HBox();
        hboxActions.getChildren().addAll(lblAmount, txtAmount, btnConvert, btnPaste);
        hboxActions.setAlignment(Pos.CENTER);
        hboxActions.setSpacing(10);

        handleButtons(currencyList1, currencyList2, txtAmount, btnConvert, lblRate, lblResult, btnPaste);

        vbox = new VBox();
        vbox.getChildren().addAll(hboxOutput, hboxActions);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(0, 0, 15, 0));
    }

    private void handleButtons(ListView<String> currencyList1, ListView<String> currencyList2, TextField txtAmount,
                               Button btnConvert, Label lblRate, Label lblResult, Button btnPaste)
    {
        btnConvert.setOnAction(event ->
        {
            try
            {
                String selected1 = currencyList1.getSelectionModel().getSelectedItem();
                String selected2 = currencyList2.getSelectionModel().getSelectedItem();

                if(currencyList1.getItems().isEmpty())
                {
                    displayAlert("Please add at least 2 currencies.");
                    return;
                }
                if(selected1 == null || selected2 == null)
                {
                    displayAlert("Please select 2 different currencies.");
                    return;
                }
                else if(selected1.equals(selected2))
                {
                    displayAlert("Please select different currencies to convert between.");
                    return;
                }

                String code1 = model.getSubstring(selected1, ":", 0);
                String code2 = model.getSubstring(selected2, ":", 0);

                model.validateUserInput(txtAmount.getText());

                if(checkCacheOrRefresh(code1, code2)) return;

                /*  Pair<rate, result> */
                Pair<String, String> convert = model.convertInput(code1, code2, txtAmount.getText());

                checkAlertForThreshold(convert.getKey());

                lblRate.setText("Rate: " + convert.getKey());
                lblResult.setText("Result: " + convert.getValue());
            }
            catch(NumberFormatException e)
            {
                displayAlert("Please enter a valid number to convert.");
            }
            catch(Exception e)
            {
                displayAlert(e.getMessage());
            }
        });

        btnPaste.setOnAction(event ->
        {
            try
            {
                String paste = model.createPasteToPost();

                /* The following was modified based on https://stackoverflow.com/a/45621264/13116149 */
                TextArea textArea = new TextArea(paste);
                textArea.setEditable(false);
                textArea.setWrapText(true);
                HBox hboxLink = new HBox(textArea);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Pastebin Report Link");
                alert.setHeaderText("Paste successful!");
                alert.getDialogPane().setContent(hboxLink);

                alert.showAndWait();
                /* end of copied code */
            }
            catch(Exception e)
            {
                displayAlert(e.getMessage());
            }
        });
    }

    private boolean checkCacheOrRefresh(String code1, String code2)
    {
        try
        {
            if(model.validateRateInCache(code1, code2))
            {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Overwrite Warning");
                alert.setHeaderText("Cache hit for this conversion rate. " +
                        "Use cache, or refresh data from the API?");

                Window window = alert.getDialogPane().getScene().getWindow();
                window.setOnCloseRequest(event2 -> window.hide());

                /* The following was modified based on https://stackoverflow.com/a/49301730/13116149 */
                ((Button) alert.getDialogPane()
                        .lookupButton(ButtonType.OK)).setText("Refresh Data");
                ((Button) alert.getDialogPane()
                        .lookupButton(ButtonType.CANCEL)).setText("Use Cache");
                /* end of copied code */

                /*
                 * The following was modified based on
                 * https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/Dialog.html
                 */
                Optional<ButtonType> result = alert.showAndWait();
                if(result.isPresent() && result.get() == ButtonType.OK)
                    /* end of copied code */
                    ((ModelCache) model).setToOverwriteCache();
                else if(result.isPresent() && result.get() == ButtonType.CANCEL)
                {}
                else
                    return true;
            }
        }
        catch(Exception e)
        {
            displayAlert(e.getMessage());
        }
        return false;
    }

    private void checkAlertForThreshold(String rate)
    {
        if(model.checkThresholdPassed(rate))
        {
            Alert thresholdAlert = new Alert(Alert.AlertType.INFORMATION);
            thresholdAlert.setTitle("Threshold Alert");
            thresholdAlert.setHeaderText("Large difference in currency values.");
            thresholdAlert.showAndWait();
        }
    }

    /** Returns the bar that is used to convert and paste the user's input */
    public VBox getPane()
    {
        return vbox;
    }
}
