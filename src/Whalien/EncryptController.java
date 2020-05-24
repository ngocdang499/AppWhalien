package Whalien;

import Cryptosystems.*;
import javafx.application.Platform;
import javafx.beans.binding.StringBinding;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Arc;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.fxml.FXML;

import javafx.scene.control.*;


import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;


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
    HBox fileBox;

    @FXML
    TextField outPath;

    public void onPathBtnClick(MouseEvent e) throws  Exception{
        DirectoryChooser dirChooser = new DirectoryChooser();
        File selectedDirectory = dirChooser.showDialog(new Stage());
        if (selectedDirectory != null) {
            String path = selectedDirectory.getAbsolutePath();
            if (!path.isBlank())
                outPath.setText(path);
        }
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

    @FXML
    TextField keyPath;

    @FXML
    Button keyBtn;

    @FXML
    Button genKeyBtn;

    public void onMenuItemClick(ActionEvent e) throws Exception{
        MenuItem item = (MenuItem) e.getSource();
        menuBtn.setText(item.getText());
        if(item.getText().contains("RSA")) {
            keyPath.setDisable(false);
            keyBtn.setDisable(false);
            genKeyBtn.setDisable(false);
        }
        else {
            keyPath.setDisable(true);
            keyBtn.setDisable(true);
            genKeyBtn.setDisable(true);
        }

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

    public void onGenKeyBtn(MouseEvent e) throws Exception {
        DirectoryChooser dirChooser = new DirectoryChooser();
        File selectedDirectory = dirChooser.showDialog(new Stage());
        String path = "";

        if (selectedDirectory != null)
            path = selectedDirectory.getAbsolutePath();

        if (!path.isBlank()) {
            // Get all file in list
            RSA.generateRSAKeyPair(path + File.separator + "RSAkey");
            AlertBox aBox = new AlertBox();
            aBox.display("RSA Key Generated", "Successfully generated RSA Key and save at " + path);
        }
    }

    public void onKeyBtnClick(MouseEvent e) throws  Exception{
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            String path = selectedFile.getAbsolutePath();
            if (!path.isBlank())
                keyPath.setText(path);
        }
    }

    @FXML
    Arc innerCircle;

    @FXML
    Label progressLb;


    public void onEncryptBtnClick(MouseEvent e) throws Exception {

        int total = 0;

        int idx = 0;

        ArrayList<File> pickFile = new ArrayList<>();

        for (Node btn : fileBox.getChildren()) {
            if (btn.getId().contains("-pick")) {

                String filePath = importFile.get(idx);
                idx += 1;
                File inFile = new File(filePath);
                if(inFile.isFile()) {
                    pickFile.add(inFile);
                    total += inFile.length();
                }
            }
        }

        if (total == 0)
            return;

        if (menuBtn.getText().contains("Cryptosystems")) {
            AlertBox aBox = new AlertBox();
            aBox.display("Missing Information", "Please choose cryptosystem for encryption.");
            return;
        }

        if (outPath.getText().isBlank()) {
            AlertBox aBox = new AlertBox();
            aBox.display("Missing Information", "Please input path to save encrypted file and key.");
            return;
        }

        if (menuBtn.getText().contains("RSA") && keyPath.getText().isBlank()) {
            AlertBox aBox = new AlertBox();
            aBox.display("Missing Information", "Please input public key for encryption or generate new key to use.");
            return;
        }

        WriteProgress wp = new WriteProgress(pickFile, total, outPath.getText(), menuBtn.getText(), keyPath.getText(), Cipher.ENCRYPT_MODE);

        // Bind Node property to progress property
        progressLb.textProperty().unbind();
        innerCircle.lengthProperty().unbind();
        progressLb.textProperty().bind(wp.progressProperty().multiply(100).asString());
        innerCircle.lengthProperty().bind(wp.progressProperty().multiply(360));
        new Thread(wp).start();

    }

}
