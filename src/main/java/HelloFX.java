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
    public void start(Stage primaryStage) throws Exception{
        MapView mapView = new MapView();

        BorderPane borderPane = new BorderPane();
        // Center pane
        borderPane.setCenter(mapView);
        // Left pane
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(30));
        Image zoomInImage = new Image("button-zoom-in.png");
        Image zoomOutImage = new Image("button-zoom-out.png");
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
        vBox.getChildren().addAll(new Button("Button 1"), new Button("Button 2"));
        borderPane.setRight(vBox);
        Scene scene = new Scene(borderPane);

        primaryStage.setTitle("Ticket to Ride");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
