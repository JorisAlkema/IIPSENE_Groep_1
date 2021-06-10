package View;

import App.MainState;
import Controller.DestinationTicketController;
import Model.DestinationTicket;
import Model.Player;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Locale;

public class RoutePopUp {
    private ArrayList<String> equalColors = new ArrayList<>();
    private String selectedColor;

    private final double UNSELECTED_OPACITY = 0.6;
    private final double SELECTED_OPACITY = 1;
    private final int WINDOW_X_POSITION = 1;
    private final int WINDOW_Y_POSITION = 1;

    public RoutePopUp(ArrayList<String> equalColors) {
        this.equalColors = equalColors;
    }

    public String showRoutePopUp() {
        Stage stage = new Stage();
        stage.getIcons().add(new Image("traincards/traincard_back_small.png"));
        stage.setOnCloseRequest(Event::consume);

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.TOP_CENTER);
        Label label = new Label("Select your route color.");
        label.setStyle("-fx-font-size:18px");
        vBox.getChildren().add(label);

        ArrayList<ImageView> availableColors = new ArrayList<>();

        for (String color : equalColors) {
            ImageView trainCard = new ImageView(new Image("traincard_" + color.toLowerCase() + "_small.png"));
            availableColors.add(trainCard);

            trainCard.setOpacity(UNSELECTED_OPACITY);

            trainCard.setOnMouseClicked(e -> {
                for (ImageView imageView : availableColors) {
                    imageView.setOpacity(UNSELECTED_OPACITY);
                }
                selectedColor = color;
                trainCard.setOpacity(SELECTED_OPACITY);
            });
            vBox.getChildren().add(trainCard);
        }

        Button closeButton = new Button("Confirm");
        closeButton.setOnAction(e -> {
            if(selectedColor != null){
                stage.close();
            }
        });
        vBox.getChildren().add(closeButton);

        Scene scene = new Scene(vBox);
        scene.getStylesheets().add(MainState.MenuCSS);
        stage.setScene(scene);
        stage.showAndWait();
        stage.setAlwaysOnTop(true);
        stage.setX(WINDOW_X_POSITION);
        stage.setY(WINDOW_Y_POSITION);

        return selectedColor;
    }
}