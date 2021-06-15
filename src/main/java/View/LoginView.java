package View;

import App.MainState;
import Controller.LoginController;
import Observers.LoginObserver;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Text;


public class LoginView extends StackPane implements LoginObserver {
    private final LoginController loginController;
    private Text message;

    public LoginView(Boolean host) {
        loginController = new LoginController(this);
        createView(host);

        // Unfocus textfield when clicked outside the textfield.
        setOnMousePressed(e -> requestFocus());
        MusicPlayerView musicPlayerView = MusicPlayerView.getInstance();
        ImageView musicImageView = musicPlayerView.getMusicImageView();

        getChildren().add(musicImageView);
        musicImageView.setTranslateX(MainState.WINDOW_WIDTH / 2 - musicImageView.getFitWidth() - 45);
        musicImageView.setTranslateY(MainState.WINDOW_HEIGHT / 2 - musicImageView.getFitHeight() - 55);
    }

    private void createView(Boolean host) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(40));
        ImageView title = new ImageView("images/logos/main_menu_logo.png");
        title.setFitWidth(title.getImage().getWidth() * 0.7);
        title.setFitHeight(title.getImage().getHeight() * 0.7);
        grid.add(title, 0, 0, 2, 1);

        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.5);

        ImageView background = new ImageView("images/backgrounds/main_menu_background.jpg");
        background.setFitWidth(MainState.WINDOW_WIDTH);
        background.setFitHeight(MainState.WINDOW_HEIGHT);
        background.setEffect(colorAdjust);

        VBox app = new VBox(10);
        app.setAlignment(Pos.BOTTOM_LEFT);
        app.setPadding(new Insets(40));
        Button returnToMenu = new Button("Return to menu");

        // If player wants to join a game.
        VBox input = (host) ? hostScreen() : joinScreen();

        app.getChildren().add(input);
        app.getChildren().add(returnToMenu);
        getChildren().add(background);
        getChildren().add(grid);
        getChildren().add(app);

        returnToMenu.setOnMouseClicked(e -> loginController.returnToMenu());
    }

    private VBox joinScreen() {
        VBox textFields = new VBox(20);
        textFields.setId("textFields");
        Text explaination = new Text("Enter username and room code to join a game with your friends");
        explaination.setId("text");

        message = new Text("Waiting for user input");
        message.setId("message");

        TextField inputUsername = new TextField();
        inputUsername.setPromptText("Username...");
        inputUsername.setFocusTraversable(false);

        HBox inputWithJoin = new HBox();
        Button join = new Button("Join");
        TextField inputCode = new TextField();
        inputCode.setPromptText("Room code...");
        inputCode.setFocusTraversable(false);
        HBox.setHgrow(inputCode, Priority.ALWAYS);
        HBox.setHgrow(join, Priority.ALWAYS);

        VBox.setMargin(explaination, new Insets(20, 10, 0, 10));
        VBox.setMargin(message, new Insets(20, 0, 0, 10));
        VBox.setMargin(inputWithJoin, new Insets(0, 0, 20, 0));
        VBox.setMargin(textFields, new Insets(40, 0, 40, 0));

        inputWithJoin.getChildren().add(inputCode);
        inputWithJoin.getChildren().add(join);

        textFields.getChildren().add(explaination);
        textFields.getChildren().add(message);
        textFields.getChildren().add(inputUsername);
        textFields.getChildren().add(inputWithJoin);

        inputCode.setOnMouseClicked(e -> {
            inputCode.setPromptText("Room code...");
        });

        join.setOnMouseClicked(e -> {
            loginController.join(inputUsername.getText(), inputCode.getText());
        });

        inputUsername.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                loginController.join(inputUsername.getText(), inputCode.getText());
            }
        });

        inputCode.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                loginController.join(inputUsername.getText(), inputCode.getText());
            }
        });

        return textFields;
    }

    private VBox hostScreen() {
        VBox textFields = new VBox(20);
        textFields.setId("textFields");
        Text explanation = new Text("Enter a username to host a game with your friends");
        explanation.setId("text");

        message = new Text("Waiting for user input");
        message.setId("message");

        HBox inputWithHost = new HBox();
        Button host = new Button("Host");
        TextField inputUsername = new TextField();
        inputUsername.setPromptText("Username...");
        inputUsername.setFocusTraversable(false);
        HBox.setHgrow(inputUsername, Priority.ALWAYS);
        HBox.setHgrow(host, Priority.ALWAYS);

        VBox.setMargin(explanation, new Insets(20, 10, 0, 10));
        VBox.setMargin(message, new Insets(20, 0, 0, 10));
        VBox.setMargin(inputWithHost, new Insets(0, 0, 20, 0));
        VBox.setMargin(textFields, new Insets(40, 0, 40, 0));

        inputWithHost.getChildren().add(inputUsername);
        inputWithHost.getChildren().add(host);

        textFields.getChildren().add(explanation);
        textFields.getChildren().add(message);
        textFields.getChildren().add(inputWithHost);

        host.setOnMouseClicked(e -> loginController.host(inputUsername.getText()));
        inputUsername.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                loginController.host(inputUsername.getText());
            }
        });
        return textFields;
    }

    @Override
    public void update(String message) {
        this.message.setText(message);
    }
}
