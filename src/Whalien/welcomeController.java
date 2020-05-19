package Whalien;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class welcomeController
{
    private static boolean theme = true;

    @FXML
    AnchorPane basePane;

    public void setTheme(boolean isDark) {
        if (isDark) {
            basePane.getStylesheets().add(EncryptController.class.getResource("../UI_CSS/DarkTheme.css").toExternalForm());
            theme = false;
        }        else {
            basePane.getStylesheets().add("../UI_CSS/LightTheme.css");
            theme = true;
        }
    }

    @FXML
    Button startBtn;

    public void onStartBtnClick(MouseEvent e) throws Exception {
        Stage stage = (Stage) startBtn.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/encryptUI.fxml"));/* Exception */
        Parent root = loader.load();

        Controller inputController = loader.getController();
        inputController.setTheme(theme);

        stage.setTitle("Whalien - Encryption");
        stage.setScene(new Scene(root, 800, 500));
        stage.show();
    }
}
