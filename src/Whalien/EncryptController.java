package Whalien;

import com.sun.tools.javac.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.fxml.FXML;

import javafx.scene.control.*;


import java.awt.event.ActionEvent;
import java.io.File;



public class EncryptController extends Controller{

    private static boolean theme = true;
    private String[] musicLst = {"mp3", "wav"};
    private String[] videoLst = {"mp4", "wav"};
    private String[] pictureLst = {"png", "jpg", "jpeg", "gif", "tiff"};


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
    HBox fileBox;

    @FXML
    TextField outPath;

    public void onPathBtnClick(MouseEvent e) throws  Exception{
        DirectoryChooser dirChooser = new DirectoryChooser();
        File selectedDirectory = dirChooser.showDialog(new Stage());
        String path = selectedDirectory.getAbsolutePath();
        if (!path.isBlank())
            outPath.setText(path);
    }

    public void onOpenDirBtn(MouseEvent e) throws Exception {
        // Open new windows to get directory path
        DirectoryChooser dirChooser = new DirectoryChooser();
        File selectedDirectory = dirChooser.showDialog(new Stage());
        String path = "";

        if (selectedDirectory.getAbsolutePath() != null)
            path = selectedDirectory.getAbsolutePath();

        if (!path.isBlank()) {
            // Get all file in list
            File folder = new File(path);
            File[] fileList = folder.listFiles();
            for (File f : fileList) {
                if (!f.isDirectory()) {
                    addFileBtn(f.getName());
                }
            }
        }
    }

    public void onOpenFileBtn(MouseEvent e) throws Exception {
        // Open new windows to get directory path
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        String path = "";

        if (selectedFile.getAbsolutePath() != null)
            path = selectedFile.getAbsolutePath();

        if (!path.isBlank()) {
            // Get all file in list
            File file = new File(path);
            if (!file.isDirectory()) {
                addFileBtn(file.getName());
            }
        }
    }

    public void addFileBtn(String filename) {
        Button fileBtn = new Button(filename);
        String img = "";

        ImageView image = new ImageView(getClass().getResource("../Image/file.png").toExternalForm());
        image.setFitHeight(80);
        image.setFitWidth(80);
        fileBtn.setGraphic(image);
        fileBtn.getStyleClass().add("file-button");

        fileBtn.setOnMouseClicked(e -> {
            try {

                String isPick = fileBtn.getId();
                if (isPick == null) {
                    fileBtn.setId("pick");
                    if (theme)
                        fileBtn.setStyle("-fx-border-color: #53567e; -fx-border-width: 2px");
                    else
                        fileBtn.setStyle("-fx-border-color: linear-gradient(#a884dd 0%, #8bacf1 100%); -fx-border-width: 2px");
                }
                else {
                    fileBtn.setStyle("-fx-border-color: transparent");
                    fileBtn.setId(null);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        fileBox.getChildren().add(fileBtn);
    }

    @FXML
    MenuButton menuBtn;

    public void onMenuItemClick(MouseEvent e) throws Exception{

    }

}
