package View;

import Controller.HandController;
import Model.TrainCard;
import Observers.HandObserver;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.util.*;

public class HandView extends HBox implements HandObserver {
    private final HandController handController;

    private final ImageView blackCard = new ImageView("traincards/traincard_black_small.png");
    private final ImageView blueCard = new ImageView("traincards/traincard_blue_small.png");
    private final ImageView greenCard = new ImageView("traincards/traincard_green_small.png");
    private final ImageView orangeCard = new ImageView("traincards/traincard_orange_small.png");
    private final ImageView purpleCard = new ImageView("traincards/traincard_purple_small.png");
    private final ImageView redCard = new ImageView("traincards/traincard_red_small.png");
    private final ImageView whiteCard = new ImageView("traincards/traincard_white_small.png");
    private final ImageView yellowCard = new ImageView("traincards/traincard_yellow_small.png");
    private final ImageView locoCard = new ImageView("traincards/traincard_loco_small.png");
    private final ArrayList<ImageView> cardImageViews;
    private final ArrayList<StackPane> stackPanes;

    public HandView() {
        this.handController = new HandController();
        handController.registerObserver(this);

        this.cardImageViews = new ArrayList<>();
        fillCardImageViews();
        this.stackPanes = createStackPaneList();

        getChildren().add(new DestinationTicketView());
        getChildren().addAll(stackPanes);
    }

    private ArrayList<StackPane> createStackPaneList() {
        ArrayList<StackPane> stackPanes = new ArrayList<>();
        for (ImageView cardImageView : cardImageViews) {
            stackPanes.add(createStackPane(cardImageView));
        }
        return stackPanes;
    }

    private StackPane createStackPane(ImageView imageView) {
        StackPane stackPane = new StackPane();

        Circle outcerCircle = new Circle(15);
        outcerCircle.setFill(Color.BLACK);
        outcerCircle.setTranslateX(35);
        outcerCircle.setTranslateY(-70);
        Circle innerCircle = new Circle(13);
        innerCircle.setFill(Color.WHITE);
        innerCircle.setTranslateX(35);
        innerCircle.setTranslateY(-70);
        Text amount = new Text("0");
        amount.setTranslateX(35);
        amount.setTranslateY(-70);

        stackPane.getChildren().addAll(imageView, outcerCircle, innerCircle, amount);
        stackPane.setVisible(false);
        return stackPane;
    }

    private void setTextOnStackPane(StackPane stackPane, String message) {
        Node node = stackPane.getChildren().get(stackPane.getChildren().size() - 1);
        Text text = (Text) node;
        text.setText(message);
    }

    /**
     * Calculates the amount of each card color, then updates the text for the corresponding stackpane
     */
    @Override
    public void update(ArrayList<TrainCard> trainCards) {
        HashMap<String, Integer> map = new HashMap<>();
        for (TrainCard trainCard : trainCards) {
            String color = trainCard.getColor();
            if (map.containsKey(color)) {
                map.put(color, map.get(color) + 1);
            } else {
                map.put(color, 1);
            }
        }
        for (StackPane stackPane : stackPanes) {
            Node node = stackPane.getChildren().get(0);
            ImageView imageView = (ImageView) node;
            boolean visible = false;
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                if (imageView.getImage().getUrl().contains(entry.getKey().toLowerCase())) {
                    if (entry.getValue() != 0) {
                        setTextOnStackPane(stackPane, Integer.toString(entry.getValue()));
                        visible = true;
                    }
                }
            }
            stackPane.setVisible(visible);
        }
    }

    private void fillCardImageViews() {
        this.cardImageViews.add(this.blackCard);
        this.cardImageViews.add(this.blueCard);
        this.cardImageViews.add(this.greenCard);
        this.cardImageViews.add(this.orangeCard);
        this.cardImageViews.add(this.purpleCard);
        this.cardImageViews.add(this.redCard);
        this.cardImageViews.add(this.whiteCard);
        this.cardImageViews.add(this.yellowCard);
        this.cardImageViews.add(this.locoCard);
    }
}
