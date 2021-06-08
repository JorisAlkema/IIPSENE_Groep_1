package View;

import Controller.DestinationTicketController;
import javafx.scene.control.ScrollPane;
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
        vBox.getChildren().addAll(new ImageView("destination_tickets/eu-amsterdam-pamplona_small.png"));
        vBox.getChildren().addAll(new ImageView("destination_tickets/eu-amsterdam-wilno_small.png"));
        vBox.getChildren().addAll(new ImageView("destination_tickets/eu-angora-kharkov_small.png"));
        setContent(vBox);
    }
}
