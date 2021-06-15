package View;

import App.MainState;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TunnelPopUp {

    public static void showPopUp(int tunnels, boolean success){
        Stage stage = new Stage();
        stage.setWidth(450);
        stage.setHeight(200);

        Label topLabel = new Label("You had to build " + tunnels + " tunnels!");
        topLabel.setWrapText(true);
        topLabel.setId("selectTickets");
        String text = success ? "had" : "did not have";
        Label bottomLabel = new Label("You " + text + " enough extra cards!");
        bottomLabel.setWrapText(true);
        bottomLabel.setId("selectTickets");

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> stage.close());

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10));
        vBox.getChildren().addAll(topLabel, bottomLabel, closeButton);
        Scene scene = new Scene(vBox);
        scene.getStylesheets().add(MainState.menuCSS);
        stage.setScene(scene);
        stage.showAndWait();
    }
}
