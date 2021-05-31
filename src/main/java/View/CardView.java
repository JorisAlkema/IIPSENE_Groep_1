package View;

import Model.OpenCards;
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
            //todo: give the player a card app.getRandomCard(deck);
        });

        buttons = createButtons(buttons,openCards);

        for (Button clickedButton: buttons){
            clickedButton.setOnAction(e ->{
                System.out.println(clickedButton);
                updateButtons(clickedButton);
            });
        }


//        for (int i = 0; i < 5; i++) {
//            buttons.get(i).setOnAction(e -> {
//                //dit is een schande voor de mensheid, maar ik mag geen normale for-loop gebruiken want ik kan geen [i] gebuikren in een event. heluuup!
//                System.out.println(openCards.getOpenCards().get(i));
//                openCards.getOpenCards().remove(i);
//                openCards.fillOpenCards();
//            });
//        }
    }

    public ArrayList<Button> createButtons(ArrayList<Button> buttons, OpenCards openCards){
        //forloop die knoppen maakt van de opencardsarray
        for (int i = 0; i < openCards.getOpenCards().size(); i++) {
            CardView.buttons.add(new Button(openCards.getOpenCards().get(i).getColor()));
        }
        getChildren().addAll(buttons);
        return buttons;
    }

    public void updateButtons(Button clickedButton){
        getChildren().remove(clickedButton);

    }
}
