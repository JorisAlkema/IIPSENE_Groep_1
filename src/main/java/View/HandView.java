package View;

import Controller.HandController;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class HandView extends HBox {
    private final HandController handController;

    private static final ImageView blackCard = new ImageView("traincards/traincard_black_small.png");
    private static final ImageView blueCard = new ImageView("traincards/traincard_blue_small.png");
    private static final ImageView greenCard = new ImageView("traincards/traincard_green_small.png");
    private static final ImageView locoCard = new ImageView("traincards/traincard_loco_small.png");
    private static final ImageView orangeCard = new ImageView("traincards/traincard_orange_small.png");
    private static final ImageView purpleCard = new ImageView("traincards/traincard_purple_small.png");
    private static final ImageView redCard = new ImageView("traincards/traincard_red_small.png");
    private static final ImageView whiteCard = new ImageView("traincards/traincard_white_small.png");
    private static final ImageView yellowCard = new ImageView("traincards/traincard_yellow_small.png");


    public HandView() {
        this.handController = new HandController(this);
        getChildren().add(new DestinationTicketView());
        getChildren().addAll(blackCard, blueCard, greenCard, locoCard, orangeCard, purpleCard, redCard, whiteCard, yellowCard);
        for (Node node : getChildren()) {
            node.setOnMouseClicked(e -> node.setOpacity(Math.abs(1 - node.getOpacity())));
        }

    }
}
