package View;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DestinationPopUp {
    public static void showPopUp(String message) {
        Stage stage = new Stage();
        stage.setWidth(250);
        stage.setHeight(250);

        Label label = new Label(message);

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> stage.close());

        VBox vBox = new VBox();
        vBox.getChildren().addAll(label, closeButton);

        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.show(); // or showAndWait
//        stage.showAndWait();
    }
}