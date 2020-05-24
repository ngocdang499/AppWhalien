package Whalien;

import Cryptosystems.AlertBox;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;


public class HashController extends Controller{

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
    Button pickInit;

    @FXML
    Button pickDecrypt;

    @FXML
    TextField hashInitValue;

    @FXML
    TextField hashDecryptValue;

    @FXML
    Button initFile;

    @FXML
    Button decryptFile;

    private String getMD5(File inFile) throws Exception{
        MessageDigest md5Digest = MessageDigest.getInstance("MD5");

        //Get file input stream for reading the file content
        FileInputStream fis = new FileInputStream(inFile);

        //Create byte array to read data in chunks
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        //Read file data and update in message digest
        while ((bytesCount = fis.read(byteArray)) != -1) {
            md5Digest.update(byteArray, 0, bytesCount);
        };

        //close the stream; We don't need it now.
        fis.close();

        //Get the hash's bytes
        byte[] bytes = md5Digest.digest();

        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for(int i=0; i< bytes.length ;i++)
        {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        //return complete hash
        return sb.toString();
    }

    public void onPickBtnClick(MouseEvent e) throws Exception {
        Button button = (Button) e.getSource();

        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        String path = "";

        if (selectedFile != null)
            path = selectedFile.getAbsolutePath();

        if (!path.isBlank()) {
            // Get all file in list
            File file = new File(path);
            if (!file.isDirectory()) {
                String hashValue = getMD5(file);
                if (button.getId() == pickInit.getId()) {
                    initFile.setText(file.getName());
                    hashInitValue.setText(hashValue);
                } else {
                    decryptFile.setText(file.getName());
                    hashDecryptValue.setText(hashValue);
                }
            }
        }
    }

    public void onCheckBtn(MouseEvent e) {
        if(hashInitValue.getText().isBlank() || hashDecryptValue.getText().isBlank()) {
            AlertBox aBox = new AlertBox();
            aBox.display("Missing File", "Please pick Initial File and Decrypted File to check.");
            return;
        }

        if(hashInitValue.getText().equals(hashDecryptValue.getText())) {
            AlertBox aBox = new AlertBox();
            aBox.display("Check Completed", "The two MD5 values is identical.");
        }
        else {
            AlertBox aBox = new AlertBox();
            aBox.display("Check Completed", "The two MD5 values is NOT identical.");
        }

    }


}
