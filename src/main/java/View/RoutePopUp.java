package View;

import App.MainState;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class RoutePopUp {
    private final ArrayList<String> possibleColors;
    private String selectedColor;

    private final double UNSELECTED_OPACITY = 0.6;
    private final double SELECTED_OPACITY = 1;

    public RoutePopUp(ArrayList<String> possibleColors) {
        this.possibleColors = possibleColors;
    }

    public String showRoutePopUp() {
        Stage stage = new Stage();
        stage.getIcons().add(new Image("images/traincards/traincard_back_small.png"));
        stage.setTitle("Traincards");
        stage.setOnCloseRequest(Event::consume);

        HBox hBox = new HBox();

        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10, 10, 10, 10));
        vBox.setAlignment(Pos.TOP_CENTER);

        Label label = new Label("Select your route color.");
        label.setStyle("-fx-font-size:18px");
        vBox.getChildren().add(label);

        ArrayList<ImageView> availableColors = new ArrayList<>();

        for (String color : possibleColors) {
            ImageView trainCard = new ImageView(new Image("images/traincards/traincard_" + color.toLowerCase() + "_small.png"));
            availableColors.add(trainCard);

            trainCard.setOpacity(UNSELECTED_OPACITY);

            trainCard.setOnMouseClicked(e -> {
                for (ImageView imageView : availableColors) {
                    imageView.setOpacity(UNSELECTED_OPACITY);
                }
                selectedColor = color;
                trainCard.setOpacity(SELECTED_OPACITY);
            });

            hBox.getChildren().add(trainCard);
        }

        vBox.getChildren().add(hBox);
        hBox.setAlignment(Pos.CENTER);

        Button closeButton = new Button("Confirm");
        closeButton.setOnAction(e -> {
            if (selectedColor != null) {
                stage.close();
            }
        });

        vBox.getChildren().add(closeButton);

        Scene scene = new Scene(vBox);
        scene.getStylesheets().add(MainState.menuCSS);
        stage.setScene(scene);
        stage.showAndWait();
        stage.setAlwaysOnTop(true);

        return selectedColor;
    }
}
