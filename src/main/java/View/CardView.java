package View;

import Controller.CardController;
import Controller.GameController;
import Model.TrainCard;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class CardView extends VBox {
    private CardController cardController;

    public CardView(GameController gameController) {
        setPadding(new Insets(0, 35, 0, 35));
        cardController = new CardController(this, gameController);
    }

    public void createButtons(ArrayList<TrainCard> openCards){
        Image image = new Image("traincards/traincard_back_small_rotated.png");
        ImageView closedTrainCard = new ImageView(image);
        //forloop die knoppen maakt van de opencardsarray

        closedTrainCard.setOnMouseClicked(e -> {
            this.cardController.pickClosedCard();
        });
        getChildren().removeAll(getChildren());
        getChildren().add(closedTrainCard);

        for (int i = 0; i < openCards.size(); i++) {
            String color = openCards.get(i).getColor();
            String path = "traincards/traincard_" + color + "_small_rotated.png";
            ImageView openCard = new ImageView(new Image(path));
//            openCard.setRotate(-90);
            int index = i;
            openCard.setOnMouseClicked(e -> {
                if (this.cardController.pickOpenCard(index)) {
                    getChildren().remove(openCard);
                }
            });
            getChildren().add(openCard);
        }
    }
}
