package Cryptosystems;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {
    public static void display(String tittle, String message){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(tittle);
        window.setWidth(500);
        window.setHeight(200);

        Label label = new Label();
        label.setStyle("-fx-font-family: Lato;" +
                " -fx-text-fill: #245198;" +
                " -fx-font-weight: bold;" +
                " -fx-padding:20 20 20 20;");
        label.setWrapText(true);
        label.setFont(Font.font("Lato", 18));
        label.setTextAlignment(TextAlignment.CENTER);
        label.setText(message);

        HBox layout = new HBox(100);
        layout.getChildren().add(label);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
}