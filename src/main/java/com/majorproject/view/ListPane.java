package com.majorproject.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;

/**
 * View class to set up the list pane where currencies are shown in 2 listviews.
 * both list views show the same list but the user can select different currencies;
 * the first is for the starting currency; the second for the ending currency.
 * Additionally, it creates 2 helper currency removing buttons, one for each view.
 * The removed currency is taken out of both lists.
 */
public class ListPane extends View
{
    HBox hbox;

    public ListPane(ListView<String> currencyList1, ListView<String> currencyList2)
    {
        Label lbl1 = new Label("Convert From:");
        Button btnRemove1 = new Button("Remove 'From' Currency");

        Label lbl2 = new Label("Convert To:");
        Button btnRemove2 = new Button("Remove 'To' Currency");

        GridPane gpListView = new GridPane();
        gpListView.setHgap(10);
        gpListView.setVgap(5);

        AnchorPane apFrom = buildAnchorPane(lbl1, btnRemove1);
        AnchorPane apTo = buildAnchorPane(lbl2, btnRemove2);

        gpListView.add(apFrom, 0, 0);
        gpListView.add(currencyList1, 0, 1);
        gpListView.add(apTo, 1, 0);
        gpListView.add(currencyList2, 1, 1);

        gpListView.setPadding(new Insets(0, 10, 0, 10));
        VBox.setVgrow(gpListView, Priority.ALWAYS );

        hbox = new HBox(gpListView);
        hbox.setAlignment(Pos.CENTER);

        handleButtonsRemove(currencyList1, currencyList2, btnRemove1, btnRemove2);
    }

    private AnchorPane buildAnchorPane(Label label, Button button)
    {
        AnchorPane anchorPane = new AnchorPane(label, button);
        AnchorPane.setLeftAnchor(label, 5.0);
        AnchorPane.setRightAnchor(button, 2.0);

        return anchorPane;
    }

    private void handleButtonsRemove(ListView<String> listView1, ListView<String> listView2,
                                     Button btnRemove1, Button btnRemove2)
    {

        btnRemove1.setOnAction(event ->
        {
            if(!listView1.getSelectionModel().isEmpty())
            {
                int selected = listView1.getSelectionModel().getSelectedIndex();

                listView1.getItems().remove(selected);
                listView2.getItems().remove(selected);
            }
            else
                displayAlert("Select a currency to remove.");
        });

        btnRemove2.setOnAction(event ->
        {
            if(!listView2.getSelectionModel().isEmpty())
            {
                int selected = listView2.getSelectionModel().getSelectedIndex();

                listView1.getItems().remove(selected);
                listView2.getItems().remove(selected);
            }
            else
                displayAlert("Select a currency to remove.");
        });
    }

    /** Returns the pane containing the 2 list views, and the 2 remove buttons for each view */
    public HBox getListPane()
    {
        return hbox;
    }
}
