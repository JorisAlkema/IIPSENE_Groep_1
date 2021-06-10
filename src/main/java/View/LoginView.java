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

        // Unfocus textfield when clicked outside the textfield
        setOnMousePressed(e -> requestFocus());
        MusicPlayerView musicPlayerView = MusicPlayerView.getInstance();
        ImageView musicImageView = musicPlayerView.getMusicImageView();

        getChildren().add(musicImageView);
        musicImageView.setTranslateX(MainState.WINDOW_WIDTH / 2 - musicImageView.getFitWidth() - 45);
        musicImageView.setTranslateY(MainState.WINDOW_HEIGHT / 2 - musicImageView.getFitHeight() - 55);

    }

    private void createView(Boolean host) {
        // Logo
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(40));
        ImageView title = new ImageView("images/logos/main_menu_logo.png");
        title.setFitWidth(title.getImage().getWidth() * 0.7);
        title.setFitHeight(title.getImage().getHeight() * 0.7);
        grid.add(title, 0, 0, 2, 1);

        // Background Effect
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.5);

        // Background for textFields and return to menu button
        ImageView background = new ImageView("images/backgrounds/main_menu_background.jpg");
        background.setFitWidth(MainState.WINDOW_WIDTH);
        background.setFitHeight(MainState.WINDOW_HEIGHT);
        background.setEffect(colorAdjust);

        // Layout
        VBox app = new VBox(10);
        app.setAlignment(Pos.BOTTOM_LEFT);
        app.setPadding(new Insets(40));
        Button returnToMenu = new Button("Return to menu");

        // if player wants to join a game
        VBox input = (host) ? hostScreen() : joinScreen();

        app.getChildren().add(input);
        app.getChildren().add(returnToMenu);
        getChildren().add(background);
        getChildren().add(grid);
        getChildren().add(app);

        /*
         * Functions for buttons
         * */
        returnToMenu.setOnMouseClicked(e -> loginController.returnToMenu());
    }

    private VBox joinScreen() {
        // Box with the explanation, input username, input roomcode and join button
        VBox textFields = new VBox(20);
        textFields.setId("textFields");
        Text explaination = new Text("Enter username and room code to join a game with your friends");
        explaination.setId("text");

        message = new Text("Waiting for user input");
        message.setId("message");

        TextField inputUsername = new TextField();
        inputUsername.setPromptText("Username...");
        inputUsername.setFocusTraversable(false);

        // input and join button next to each other
        HBox inputWithJoin = new HBox();
        Button join = new Button("Join");
        TextField inputCode = new TextField();
        inputCode.setPromptText("Room code...");
        inputCode.setFocusTraversable(false);
        HBox.setHgrow(inputCode, Priority.ALWAYS);
        HBox.setHgrow(join, Priority.ALWAYS);

        // Add margins
        VBox.setMargin(explaination, new Insets(20, 10, 0, 10));
        VBox.setMargin(message, new Insets(20, 0, 0, 10));
        VBox.setMargin(inputWithJoin, new Insets(0, 0, 20, 0));
        VBox.setMargin(textFields, new Insets(40, 0, 40, 0));

        // Add all
        inputWithJoin.getChildren().add(inputCode);
        inputWithJoin.getChildren().add(join);

        textFields.getChildren().add(explaination);
        textFields.getChildren().add(message);
        textFields.getChildren().add(inputUsername);
        textFields.getChildren().add(inputWithJoin);

        // Event Handlers
        inputCode.setOnMouseClicked(e -> {
            inputCode.setPromptText("Room code...");
        });

        //join either by pressing button or typing enter
        join.setOnMouseClicked(e -> {
            loginController.join(inputUsername, inputCode);
        });

        inputUsername.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                loginController.join(inputUsername, inputCode);
            }
        });

        inputCode.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                loginController.join(inputUsername, inputCode);
            }
        });

        return textFields;
    }

    private VBox hostScreen() {
        // Box with the explanation, input username, input roomcode and join button
        VBox textFields = new VBox(20);
        textFields.setId("textFields");
        Text explanation = new Text("Enter a username to host a game with your friends");
        explanation.setId("text");

        message = new Text("Waiting for user input");
        message.setId("message");

        // input and join button next to each other
        HBox inputWithHost = new HBox();
        Button host = new Button("Host");
        TextField inputUsername = new TextField();
        inputUsername.setPromptText("Username...");
        inputUsername.setFocusTraversable(false);
        HBox.setHgrow(inputUsername, Priority.ALWAYS);
        HBox.setHgrow(host, Priority.ALWAYS);

        // Add margins
        VBox.setMargin(explanation, new Insets(20, 10, 0, 10));
        VBox.setMargin(message, new Insets(20, 0, 0, 10));
        VBox.setMargin(inputWithHost, new Insets(0, 0, 20, 0));
        VBox.setMargin(textFields, new Insets(40, 0, 40, 0));

        // Add all
        inputWithHost.getChildren().add(inputUsername);
        inputWithHost.getChildren().add(host);

        textFields.getChildren().add(explanation);
        textFields.getChildren().add(message);
        textFields.getChildren().add(inputWithHost);

        // Event Handlers

        //host
        host.setOnMouseClicked(e -> loginController.host(inputUsername));
        inputUsername.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                loginController.host(inputUsername);
            }
        });
        return textFields;
    }

    @Override
    public void update(String message) {
        this.message.setText(message);
    }
}
