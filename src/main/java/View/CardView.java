package View;

import Controller.CardController;
import Model.OpenCards;
import Model.TrainCard;
import Model.TrainCardDeck;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class CardView extends VBox {
//    public ArrayList<Button> buttons = new ArrayList<Button>();
    private CardController cardController = new CardController(this);
    private TrainCard randomCard = null;

    public CardView() {
        setPadding(new Insets(30));
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

//    public void clickedButtons(){
//        for (Button clickedButton: buttons){
//            clickedButton.setOnAction(e ->{
//                System.out.println(this.cardController.getOpenDeck().openCards.get(buttons.indexOf(clickedButton)));
//                System.out.println(this.cardController.getOpenDeck().openCards.get(buttons.indexOf(clickedButton)).getColor());
//                TrainCard newOpenCard = this.cardController.pickOpenCard(buttons.indexOf(clickedButton));
//                getChildren().remove(clickedButton);
//                buttons.remove(clickedButton);
//
//                Button newButton = new Button(newOpenCard.getColor());
//                buttons.add(newButton);
//                getChildren().add(newButton);
//
//                clickedButtons();
//            });
//        }
//    }
}
