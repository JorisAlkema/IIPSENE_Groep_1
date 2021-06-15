package View;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TunnelPopUp {
    public static void TunnelPopUp(int tunnels, boolean failed){
        Stage stage = new Stage();
        stage.setWidth(250);
        stage.setHeight(250);

        Label label = new Label("You need to build" + tunnels + "tunnels!");

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> stage.close());

        VBox vBox = new VBox();

        if (failed){
            Label label2 = new Label("You do not have enough extra cards!");
            vBox.getChildren().addAll(label,label2, closeButton);
        } else{
            vBox.getChildren().addAll(label, closeButton);
        }

        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.show(); // or showAndWait
//      stage.showAndWait();
    }

}
