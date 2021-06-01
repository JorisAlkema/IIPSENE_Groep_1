package View;

import Model.OpenCards;
import Model.TrainCard;
import Model.TrainCardDeck;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class CardView extends VBox {
public static ArrayList<Button> buttons = new ArrayList<Button>();

    public CardView() {
        super();
        displayCards();
    }
    public void displayCards() {
        setPadding(new Insets(30));
        Button getTrainCard = new Button("Get closed train card");
        getChildren().addAll(getTrainCard);

        TrainCardDeck deck = new TrainCardDeck();
        OpenCards openCards = new OpenCards(deck);

        getTrainCard.setOnAction(e -> {
            TrainCard randomCard = deck.getRandomCard();
            System.out.println(randomCard);
            System.out.println(randomCard.getColor());
        });

        createButtons(openCards);

        clickedButtons(deck,openCards);
    }

    public void createButtons(OpenCards openCards){
        //forloop die knoppen maakt van de opencardsarray
        for (int i = 0; i < openCards.getOpenCards().size(); i++) {
            CardView.buttons.add(new Button(openCards.getOpenCards().get(i).getColor()));
        }
        getChildren().addAll(buttons);
    }

    public void clickedButtons(TrainCardDeck deck,OpenCards openCards){
        for (Button clickedButton: buttons){
            clickedButton.setOnAction(e ->{
                System.out.println(openCards.openCards.get(buttons.indexOf(clickedButton)));
                System.out.println(openCards.openCards.get(buttons.indexOf(clickedButton)).getColor());
                getChildren().remove(clickedButton);
                buttons.remove(clickedButton);
                Button newButton = new Button(deck.getRandomCard().getColor());
                buttons.add(newButton);
                getChildren().add(newButton);
                clickedButtons(deck,openCards);
            });
        }
    }
}
