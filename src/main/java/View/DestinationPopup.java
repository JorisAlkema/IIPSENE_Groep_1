package View;

import App.MainState;
import Model.DestinationTicket;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class DestinationPopup {

    public static void showPopUp(ArrayList<DestinationTicket> destinationTickets) {
        final int WINDOW_X_POSITION = 1640;
        final int WINDOW_Y_POSITION = 50;
        final int WINDOW_WIDTH = 270;
        final int WINDOW_HEIGHT = 936;

        Stage stage = new Stage();
        stage.setWidth(WINDOW_WIDTH);
        stage.setHeight(WINDOW_HEIGHT);
        stage.setTitle("Tickets");
        stage.getIcons().add(new Image("traincards/traincard_back_small.png"));
        VBox vBox = new VBox();

        ArrayList<DestinationTicket> selectedTickets = new ArrayList<DestinationTicket>();

        for (int i=0; i < destinationTickets.size();i++){
            String path = destinationTickets.get(i).getFileName();
            ImageView destinationTicket = new ImageView(new Image(path));
            destinationTicket.setOpacity(0.7);

            int index = i;
            int selectedcards;
            destinationTicket.setOnMouseClicked(e -> {
                if(destinationTicket.getOpacity() == 0.7){
                    selectedTickets.add(destinationTickets.get(index));
                    destinationTicket.setOpacity(1);
                } else {
                    selectedTickets.remove(destinationTickets.get(index));
                    destinationTicket.setOpacity(0.7);
                }
            });
            vBox.getChildren().add(destinationTicket);
        }

        Button closeButton = new Button("Ok");
        closeButton.setPadding(new Insets(0, 0, 0, 10));
        closeButton.setOnAction(e -> {
            if(selectedTickets.size() != 0){
                stage.close();
            }
            for (DestinationTicket destinationTicket: selectedTickets){
                MainState.getLocalPlayer().addDestinationTicket(destinationTicket);
            }
        });

        vBox.getChildren().addAll(closeButton);
        vBox.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(vBox);
        scene.getStylesheets().add(MainState.MenuCSS);
        stage.setScene(scene);
        stage.show(); // or showAndWait
        stage.setX(WINDOW_X_POSITION);
        stage.setY(WINDOW_Y_POSITION);
    }
}