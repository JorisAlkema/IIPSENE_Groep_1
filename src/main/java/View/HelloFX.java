package View;

import Model.GameInfo;
import View.CardView;
import View.LoginView;
import View.MainMenuView;
import View.MapView;
import Model.Observer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelloFX extends Application implements Observer {

    Label label;
    GameInfo gameInfo = new GameInfo();

    @Override
    public void start(Stage primaryStage) throws Exception {
        MapView mapView = new MapView();

        BorderPane borderPane = new BorderPane();

        // Top pane
        label = new Label("0:00");
        borderPane.setTop(label);

        // Center pane
        borderPane.setCenter(mapView);

        // Left pane
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(30));
        Image zoomInImage = new Image("icons/button_zoom_in.png");
        Image zoomOutImage = new Image("icons/button_zoom_out.png");
        ImageView imageView = new ImageView(zoomInImage);

        imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if (mapView.isZoomedIn()) {
                mapView.zoomOut();
                imageView.setImage(zoomInImage);
            } else {
                mapView.zoomIn();
                imageView.setImage(zoomOutImage);
            }
        });

        vBox.getChildren().addAll(imageView);
        borderPane.setLeft(vBox);

        // Right pane
        vBox = new VBox();
        vBox.setPadding(new Insets(30));
        Scene scene = new Scene(borderPane);

        Button mainmenu = new Button("Return to menu");


        mainmenu.setOnAction(e -> {
            Scene newScene = new Scene(new LoginView(primaryStage, true), scene.getWidth(), scene.getHeight());
            String css = "css/styling.css";
            newScene.getStylesheets().add(css);
            primaryStage.setScene(newScene);
        });

        vBox.getChildren().addAll(imageView,mainmenu);
        borderPane.setLeft(vBox);

        borderPane.setRight(new CardView());
        primaryStage.setTitle("Ticket to Ride");
        primaryStage.setResizable(false);
        primaryStage.setHeight(720);
        primaryStage.setWidth(1280);
        primaryStage.show();

        //gameInfo.addObserver(this);
        //gameInfo.countdownTimer();
        //gameInfo.setTimerText(gameInfo.getTimer());
    }

    @Override
    public void update(Object timerText) {
        label.setText((String) timerText);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
