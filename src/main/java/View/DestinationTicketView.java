package View;

import App.MainState;
import Controller.DestinationTicketController;
import Model.DestinationTicket;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class DestinationTicketView extends ScrollPane {
    private ArrayList<ImageView> imageViews;
    private VBox vBox;

    public DestinationTicketView() {
        super();
        this.setHbarPolicy(ScrollBarPolicy.NEVER);
        this.setMaxHeight(174);
        vBox = new VBox();
        for (DestinationTicket destinationTicket: MainState.getLocalPlayer().getDestinationTickets()){
            String path = destinationTicket.getFileName();
            vBox.getChildren().addAll(new ImageView(new Image(path)));
        }
        setContent(vBox);
    }
}
