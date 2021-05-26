package View;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class CardView extends VBox {

    public CardView() {
        super();
        displayCards();
    }

    public void displayCards(){
        setPadding(new Insets(30));

        Button getTrainCard = new Button("Get closed train card");
        getChildren().addAll(getTrainCard);
    }




}
