package Whalien;

import com.sun.tools.javac.Main;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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


import java.io.File;
import java.util.ArrayList;


public class EncryptController extends Controller{

    private static boolean theme = true;
    private ArrayList<String> importFile = new ArrayList<String>();

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

        if (selectedDirectory != null)
            path = selectedDirectory.getAbsolutePath();

        if (!path.isBlank()) {
            // Get all file in list
            File folder = new File(path);
            File[] fileList = folder.listFiles();
            for (File f : fileList) {
                if (!f.isDirectory()) {
                    importFile.add(f.getPath());
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

        if (selectedFile != null)
            path = selectedFile.getAbsolutePath();

        if (!path.isBlank()) {
            // Get all file in list
            File file = new File(path);
            if (!file.isDirectory()) {
                importFile.add(file.getPath());
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
        fileBtn.setId(Integer.toString(importFile.size()));
        fileBtn.setOnMouseClicked(e -> {
            try {

                String isPick = fileBtn.getId();
                if (!isPick.contains("-pick")) {
                    fileBtn.setId(fileBtn.getId() + "-pick");
                    if (theme)
                        fileBtn.setStyle("-fx-border-color: #53567e; -fx-border-width: 2px");
                    else
                        fileBtn.setStyle("-fx-border-color: linear-gradient(#a884dd 0%, #8bacf1 100%); -fx-border-width: 2px");
                }
                else {
                    fileBtn.setStyle("-fx-border-color: transparent");
                    fileBtn.setId(fileBtn.getId().replace("-pick",""));
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        fileBox.getChildren().add(fileBtn);
    }

    @FXML
    MenuButton menuBtn;

    public void onMenuItemClick(ActionEvent e) throws Exception{
        MenuItem item = (MenuItem) e.getSource();
        menuBtn.setText(item.getText());

    }

    public void onSelectAll(ActionEvent e) throws Exception {
        ObservableList<Node> btnList = fileBox.getChildren();
        RadioButton radioBtn = (RadioButton) e.getSource();
        if (radioBtn.isSelected()) {
            for (Node btn : fileBox.getChildren()) {
                if (!btn.getId().contains("-pick")) {
                    btn.setId(btn.getId() + "-pick");
                    if (theme)
                        btn.setStyle("-fx-border-color: #53567e; -fx-border-width: 2px");
                    else
                        btn.setStyle("-fx-border-color: linear-gradient(#a884dd 0%, #8bacf1 100%); -fx-border-width: 2px");
                }

            }
        }
        else {
            for (Node btn : fileBox.getChildren()) {
                if (btn.getId().contains("-pick")) {
                    btn.setId(btn.getId().replace("-pick",""));
                    btn.setStyle("-fx-border-color: transparent");
                }

            }
        }

    }

}
