import View.CardView;
import View.MainMenuView;
import View.MapView;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelloFX extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        MapView mapView = new MapView();

        BorderPane borderPane = new BorderPane();

        // Center pane
        borderPane.setCenter(mapView);
        // Left pane
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(30));
        Image zoomInImage = new Image("icons/button_zoom_in.png");
        Image zoomOutImage = new Image("icons/button_zoom_out.png");
        ImageView imageView = new ImageView(zoomInImage);
        Scene scene = new Scene(borderPane);

        imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if (mapView.isZoomedIn()) {
                mapView.zoomOut();
                imageView.setImage(zoomInImage);
            } else {
                mapView.zoomIn();
                imageView.setImage(zoomOutImage);
            }
        });
        Button mainmenu = new Button("Return to menu");


        mainmenu.setOnAction(e -> {
            Scene newScene = new Scene(new MainMenuView(), scene.getWidth(), scene.getHeight());
            String css = this.getClass().getResource("css/styling.css").toString();
            newScene.getStylesheets().add(css);
            primaryStage.setScene(newScene);
        });

        vBox.getChildren().addAll(imageView,mainmenu);
        borderPane.setLeft(vBox);

        borderPane.setRight(new CardView());
        primaryStage.setTitle("Ticket to Ride");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setHeight(720);
        primaryStage.setWidth(1280);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
