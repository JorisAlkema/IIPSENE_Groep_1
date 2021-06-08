package View;

import Model.City;
import Model.DestinationTicket;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class DestinationPopup {

    public static ArrayList<DestinationTicket> showPopUp(ArrayList<DestinationTicket> destinationTickets) {
        Stage stage = new Stage();
        stage.setWidth(350);
        stage.setHeight(750);
        VBox vBox = new VBox();

        ArrayList<DestinationTicket> selectedTickets = new ArrayList<DestinationTicket>();

        for (int i=0; i < destinationTickets.size();i++){
            String path = destinationTickets.get(i).getFileName();
            ImageView destinationTicket = new ImageView(new Image(path));
            destinationTicket.setOpacity(0.8);

            int index = i;
            int selectedcards;
            destinationTicket.setOnMouseClicked(e -> {
                if(destinationTicket.getOpacity() == 0.8){
                    selectedTickets.add(destinationTickets.get(index));
                    destinationTicket.setOpacity(1);
                } else {
                    selectedTickets.remove(destinationTickets.get(index));
                    destinationTicket.setOpacity(0.8);
                }
            });
            vBox.getChildren().add(destinationTicket);
        }

        Button closeButton = new Button("Ok");
        closeButton.setOnAction(e -> {
            if(selectedTickets.size() != 0){
                stage.close();
            }
        });


        vBox.getChildren().addAll(closeButton);

        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.show(); // or showAndWait
//        stage.showAndWait();

        return selectedTickets;
    }
}
