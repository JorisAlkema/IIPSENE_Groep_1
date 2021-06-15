package View;

import Controller.HandController;
import Model.DestinationTicket;
import Observers.HandObserver;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HandView extends HBox implements HandObserver {
    private final ImageView blackCard = new ImageView("images/traincards/traincard_black_small.png");
    private final ImageView blueCard = new ImageView("images/traincards/traincard_blue_small.png");
    private final ImageView greenCard = new ImageView("images/traincards/traincard_green_small.png");
    private final ImageView orangeCard = new ImageView("images/traincards/traincard_orange_small.png");
    private final ImageView purpleCard = new ImageView("images/traincards/traincard_purple_small.png");
    private final ImageView redCard = new ImageView("images/traincards/traincard_red_small.png");
    private final ImageView whiteCard = new ImageView("images/traincards/traincard_white_small.png");
    private final ImageView yellowCard = new ImageView("images/traincards/traincard_yellow_small.png");
    private final ImageView locoCard = new ImageView("images/traincards/traincard_loco_small.png");

    private final ArrayList<ImageView> cardImageViews;
    private final ArrayList<StackPane> trainCardPanes;
    private final ScrollPane destinationTicketPane;

    public HandView() {
        HandController handController = new HandController();
        handController.registerObserver(this);
        setAlignment(Pos.CENTER_RIGHT);

        this.cardImageViews = new ArrayList<>();
        fillCardImageViews();

        this.trainCardPanes = createStackPaneList();
        this.destinationTicketPane = new ScrollPane();
        initDestinationTicketPane();

        getChildren().add(destinationTicketPane);
        getChildren().addAll(trainCardPanes);
    }

    private void initDestinationTicketPane() {
        destinationTicketPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        destinationTicketPane.setMaxHeight(174); // TrainCard height in hand.
        destinationTicketPane.setMinWidth(203); // Ticket width in hand.
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

        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setSaturation(-0.5);
        imageView.setEffect(colorAdjust);
        imageView.setOpacity(0.3);
        stackPane.getChildren().addAll(imageView, outcerCircle, innerCircle, amount);

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
    public void update(HashMap<String, Integer> trainCardMap, ArrayList<String> ticketFileNamesSmall) {
        updateDestinationTickets(ticketFileNamesSmall);
        updateTrainCards(trainCardMap);
    }

    private void updateDestinationTickets(ArrayList<String> ticketFileNamesSmall) {
        VBox vBox = new VBox();

        for (String path : ticketFileNamesSmall) {
            vBox.getChildren().addAll(new ImageView(path));
        }

        destinationTicketPane.setContent(vBox);
    }

    private void updateTrainCards(HashMap<String, Integer> map) {
        for (StackPane stackPane : trainCardPanes) {
            Node node = stackPane.getChildren().get(0);
            ImageView imageView = (ImageView) node;
            String amount = "0";
            double saturation = -0.5;
            double opacity = 0.3;
            boolean canHover = false;

            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                if (imageView.getImage().getUrl().contains(entry.getKey().toLowerCase())) {
                    if (entry.getValue() != 0) {
                        amount = Integer.toString(entry.getValue());
                        saturation = 0;
                        opacity = 1;
                        canHover = true;
                    }
                }
            }

            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setSaturation(saturation);
            imageView.setOpacity(opacity);
            imageView.setEffect(colorAdjust);
            setTextOnStackPane(stackPane, amount);

            if (canHover) {
                stackPane.setId("onHoverUp");
            } else {
                stackPane.setId("");
            }
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
