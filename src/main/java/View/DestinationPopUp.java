package View;

import App.MainState;
import Controller.DestinationTicketController;
import Controller.GameController;
import Model.DestinationTicket;
import Model.GameState;
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
    private final DestinationTicketController destinationTicketController;
    private final int CARDHEIGHT = 161;
    private final double UNSELECTED_OPACITY = 0.6;
    private final double SELECTED_OPACITY = 1;
    private final int WINDOW_X_POSITION = 1640;
    private final int WINDOW_Y_POSITION = 50;
    private final int WINDOW_WIDTH = 300;
    private final int WINDOW_HEIGHT = 936;

    public DestinationPopUp(GameState gameState) {
        ArrayList<DestinationTicket> destinationTickets = gameState.getDestinationDeck();
        this.destinationTicketController = new DestinationTicketController(destinationTickets);
    }

    public void showAtStartOfGame(GameState gameState, GameController gameController) {
        this.showPopUp(this.destinationTicketController.drawTickets(true), gameState, gameController);
    }

    public void showDuringGame(GameState gameState, GameController gameController) {
        this.showPopUp(this.destinationTicketController.drawTickets(false), gameState, gameController);
    }

    private void showPopUp(ArrayList<DestinationTicket> destinationTickets, GameState gameState, GameController gameController) {
        Stage stage = new Stage();
        stage.getIcons().add(new Image("images/traincards/traincard_back_small.png"));
        stage.setWidth(WINDOW_WIDTH);
        stage.setHeight(WINDOW_HEIGHT);
        stage.setOnCloseRequest(Event::consume);

        ArrayList<DestinationTicket> selectedTickets = new ArrayList<>();
        int minimumTickets = destinationTickets.size() / 2;

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.TOP_CENTER);
        Label label = new Label("Select at least " + minimumTickets + " destination tickets");
        label.setStyle("-fx-font-size:18px");
        vBox.getChildren().add(label);

        for (DestinationTicket destinationTicket : destinationTickets) {
            String path = destinationTicket.fileName();
            ImageView ticketImageView = new ImageView(new Image(path));
            ticketImageView.setOpacity(UNSELECTED_OPACITY);
            ticketImageView.setOnMouseClicked(e -> {
                if (!selectedTickets.contains(destinationTicket)) {
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
        Player player = gameController.getLocalPlayerFromGameState();
        closeButton.setOnAction(e -> {
            if (selectedTickets.size() >= minimumTickets) {
                for (DestinationTicket destinationTicket : selectedTickets) {
                    player.addDestinationTicket(destinationTicket);

                    destinationTickets.remove(destinationTicket);
                    gameState.setDestinationDeck(destinationTickets);
                }
                stage.close();
            }
        });
        vBox.getChildren().add(closeButton);

        Scene scene = new Scene(vBox);
        scene.getStylesheets().add(MainState.menuCSS);
        stage.setScene(scene);
        stage.setAlwaysOnTop(true);
        stage.setX(WINDOW_X_POSITION);
        stage.setY(WINDOW_Y_POSITION);
        stage.showAndWait();
    }
}
