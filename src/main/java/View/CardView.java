package View;

import Controller.CardController;
import Controller.GameController;
import Model.TrainCard;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class CardView extends VBox {
//    public ArrayList<Button> buttons = new ArrayList<Button>();
    private CardController cardController;
    private TrainCard randomCard = null;


    public CardView(GameController gameController) {
        setPadding(new Insets(30));
        cardController = new CardController(this, gameController);
    }

    public void createButtons(ArrayList<TrainCard> openCards){
        Image image = new Image("traincards/traincard_back_small.png");
        ImageView closedTrainCard = new ImageView(image);
        closedTrainCard.setRotate(-90);
        //forloop die knoppen maakt van de opencardsarray

        closedTrainCard.setOnMouseClicked(e -> {
            this.cardController.pickClosedCard();
        });
        getChildren().removeAll(getChildren());
        getChildren().add(closedTrainCard);

        for (int i = 0; i < openCards.size(); i++) {
            Button openCard = new Button(openCards.get(i).getColor());
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
