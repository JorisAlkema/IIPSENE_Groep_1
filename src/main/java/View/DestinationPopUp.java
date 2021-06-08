package View;

import App.MainState;
import Model.DestinationTicket;
import Model.Player;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class DestinationPopUp {
    private static final int WIDTH = 300;
    private static final int CARDHEIGHT = 161;
    private static final double UNSELECTED_OPACITY = 0.6;
    private static final double SELECTED_OPACITY = 1;

    public static void showPopUp(ArrayList<DestinationTicket> destinationTickets) {
        Stage stage = new Stage();
        stage.setWidth(WIDTH);
        stage.setHeight( (destinationTickets.size() + 1) * CARDHEIGHT );
        stage.setOnCloseRequest(Event::consume);

        ArrayList<DestinationTicket> selectedTickets = new ArrayList<>();
        int minimumTickets = destinationTickets.size() / 2;

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        Label label = new Label("Select at least " + minimumTickets + " destination tickets");
        label.setStyle("-fx-font-size:18px");
        vBox.getChildren().add(label);

        for (DestinationTicket destinationTicket : destinationTickets) {
            String path = destinationTicket.getFileName();
            ImageView ticketImageView = new ImageView(new Image(path));
            ticketImageView.setOpacity(UNSELECTED_OPACITY);
            ticketImageView.setOnMouseClicked(e -> {
                if ( ! selectedTickets.contains(destinationTicket)) {
                    selectedTickets.add(destinationTicket);
                    ticketImageView.setOpacity(SELECTED_OPACITY);
                } else {
                    selectedTickets.remove(destinationTicket);
                    ticketImageView.setOpacity(UNSELECTED_OPACITY);
                }
            });
            vBox.getChildren().add(ticketImageView);
        }

        Button closeButton = new Button("Confirm");
        Player player = MainState.getLocalPlayer();
        closeButton.setOnAction(e -> {
            if(selectedTickets.size() >= minimumTickets){
                for (DestinationTicket destinationTicket: selectedTickets){
                    player.addDestinationTicket(destinationTicket);
                }
                stage.close();
            }
        });
        vBox.getChildren().add(closeButton);

        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.show();
    }
}