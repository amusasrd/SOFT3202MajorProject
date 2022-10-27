package com.majorproject.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/** View class that creates the title. */
public class TitlePane
{
    HBox hboxTitle;

    public TitlePane()
    {
        hboxTitle = new HBox();
        Label lblHeader = new Label("Currency Conversions");
        lblHeader.setFont(Font.font("arial", FontWeight.EXTRA_BOLD, 48));
        hboxTitle.getChildren().add(lblHeader);
        hboxTitle.setAlignment(Pos.CENTER);
        hboxTitle.setPadding(new Insets(5, 10, 10, 10));
    }

    /** Returns the title box */
    public HBox getTitle()
    {
        return hboxTitle;
    }
}
