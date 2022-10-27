package com.majorproject.view;

import javafx.scene.control.Alert;

/** Abstract class that provides error handling for all view classes. */
public abstract class View
{
    /**
     * Opens a pop-up window that is used to display the given error message.
     * @param message the error message to show the user
     */
    public void displayAlert(String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);

        alert.showAndWait();
    }
}
