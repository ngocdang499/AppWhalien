package Whalien;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class SettingController extends Controller{

    private boolean theme = true;

    @FXML
    AnchorPane basePane;

    public void setTheme(boolean isDark) {
        if (isDark) {
            basePane.getStylesheets().add(EncryptController.class.getResource("../UI_CSS/DarkTheme.css").toExternalForm());
            theme = true;
        }
        else {
            basePane.getStylesheets().add(EncryptController.class.getResource("../UI_CSS/LightTheme.css").toExternalForm());
            theme = false;
        }
    }

    @FXML
    Button settingBtn;

    @FXML
    Button decryptBtn;

    @FXML
    Button encryptBtn;

    public void onMenuBtnClick(MouseEvent e) throws Exception {
        Button button = (Button) e.getSource();

        Stage stage = (Stage)button.getScene().getWindow();

        String nextScene = "";
        String title = "";

        switch (button.getId()) {
            case "settingBtn":
                nextScene = "settingUI.fxml";
                title = "Settings";
                break;
            case "decryptBtn":
                nextScene = "decryptUI.fxml";
                title = "Decryption";
                break;
            case "encryptBtn":
                nextScene = "encryptUI.fxml";
                title = "Encryption";
                break;
            case "hashBtn":
                nextScene = "hashUI.fxml";
                title = "Integrity Check";
                break;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/"+nextScene));/* Exception */
        Parent root = loader.load();

        Controller inputController = loader.getController();
        inputController.setTheme(theme);

        stage.setTitle("Whalien - " + title);
        stage.setScene(new Scene(root, 800, 500));
        stage.show();
    }

    @FXML
    Button darktmBtn;

    public void onThemeBtnClick(MouseEvent e) throws Exception {
        Button button = (Button) e.getSource();
        theme = (button.getId() == darktmBtn.getId());
        Stage stage = (Stage) button.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/settingUI.fxml"));/* Exception */
        Parent root = loader.load();

        Controller inputController = loader.getController();
        inputController.setTheme(theme);

        stage.setTitle("Whalien - Settings");
        stage.setScene(new Scene(root, 800, 500));
        stage.show();

    }
}
