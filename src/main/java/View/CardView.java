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
    public static ArrayList<Button> buttons = new ArrayList<Button>();
    private CardController cardController = new CardController(this);
    private TrainCard randomCard = null;

    public CardView() {
        super();
        displayCards();
    }

    public void displayCards() {
        setPadding(new Insets(30));
        Button getTrainCard = new Button("Get closed train card");
        getChildren().addAll(getTrainCard);
        getTrainCard.setOnAction(e -> {
            randomCard = this.cardController.getRandomCard();
            System.out.println(randomCard);
            System.out.println(randomCard.getColor());
        });

        createButtons(this.cardController.getOpenDeck());
        clickedButtons();
    }

    public void createButtons(OpenCards openCards){
        //forloop die knoppen maakt van de opencardsarray
        for (int i = 0; i < openCards.getOpenCards().size(); i++) {
            CardView.buttons.add(new Button(openCards.getOpenCards().get(i).getColor()));
        }
        getChildren().addAll(buttons);
    }

    public void clickedButtons(){
        for (Button clickedButton: buttons){
            clickedButton.setOnAction(e ->{
                System.out.println(this.cardController.getOpenDeck().openCards.get(buttons.indexOf(clickedButton)));
                System.out.println(this.cardController.getOpenDeck().openCards.get(buttons.indexOf(clickedButton)).getColor());

                getChildren().remove(clickedButton);
                buttons.remove(clickedButton);

                Button newButton = new Button(this.cardController.getRandomCard().getColor());

                buttons.add(newButton);
                getChildren().add(newButton);

                clickedButtons();
            });
        }
    }
}
