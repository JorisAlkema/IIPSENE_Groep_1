package View;

import Controller.CardController;
<<<<<<< HEAD
import Controller.GameController;
import Model.OpenCards;
=======
>>>>>>> 952fe7216330f748017739dd88d629e55b64415f
import Model.TrainCard;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class CardView extends VBox {
<<<<<<< HEAD
//    public ArrayList<Button> buttons = new ArrayList<Button>();
    private CardController cardController;
    private TrainCard randomCard = null;
=======
    private CardController cardController = new CardController(this);
>>>>>>> 952fe7216330f748017739dd88d629e55b64415f


    public CardView(GameController gameController) {
        setPadding(new Insets(30));
        cardController = new CardController(this,gameController);
    }

    public void createButtons(ArrayList<TrainCard> openCards){
        //forloop die knoppen maakt van de opencardsarray
        Button getTrainCard = new Button("Get closed train card");
        getTrainCard.setOnAction(e -> {
            this.cardController.pickClosedCard();
        });
        getChildren().removeAll(getChildren());
        getChildren().add(getTrainCard);

        for (int i = 0; i < openCards.size(); i++) {
            Button openCard = new Button(openCards.get(i).getColor());
            int index = i;
            openCard.setOnMouseClicked(e -> {
                this.cardController.pickOpenCard(index);
                getChildren().remove(openCard);
            });
            getChildren().add(openCard);
        }
    }
}
