package Controller;

import App.Main;
import App.MainState;
import Model.Login;
import Model.Player;
import View.LobbyView;
import View.LoginView;
import View.MainMenuView;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextField;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class LoginController {
    private Login login = new Login();;
    private final int CHARACTER_MAX = 15;
    private final int CHARACTER_MIN = 5;
    private final int ROOMCODE_CHARACTERS = 6;
    public LoginController(LoginView loginView) {
        login.registerObserver(loginView);
    }

    // Return to Menu
    public void returnToMenu() {
        MainMenuView menuView = new MainMenuView();
        Scene scene = new Scene(menuView, MainState.SCREEN_WIDTH, MainState.SCREEN_HEIGHT);
        scene.getStylesheets().add(MainState.MenuCSS);
        MainState.primaryStage.setScene(scene);
    }

    // Check characters of username
    public boolean checkUsername(String username) {
        return username.length() >= CHARACTER_MAX || username.length() < CHARACTER_MIN;
    }

    public boolean checkRoomCode(String code) {
        int characters = code.length();
        for (int i = 0; i < characters; i ++) {
            if(Character.isLetter(code.charAt(i))) {
                return false;
            }
        }

        return characters == ROOMCODE_CHARACTERS;
    }

    // Join game
    public void join(TextField inputUsername, TextField inputCode) {
        String username = inputUsername.getText();
        String code = inputCode.getText();

        if (username.isBlank() || code.isBlank()) {
            login.notifyAllObservers("Fill in all the required fields");
            return;
        }

        if(this.checkUsername(username)) {
            login.notifyAllObservers("Your username must be between " + Integer.toString(CHARACTER_MIN) + " and " + Integer.toString(CHARACTER_MAX) + " characters long");
            return;
        }

        if(!this.checkRoomCode(code)) {
            login.notifyAllObservers("Enter a valid roomcode");
            return;
        }

        if (!login.getBusy()) {
            // Spam protection
            login.setBusy(true);

            // Joining lobby... loading animation
            Timer joiningLobbyAnimation = getLoadingAnimation("Joining lobby");

            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    String exception = null;
                    String player_uuid = generateUUID();
                    Player player = new Player(username, player_uuid, false);
                    // Tries to add player to the lobby
                    try {
                        MainState.firebaseService.addPlayerToLobby(code, player);
                    } catch (Exception e) {
                        exception = e.getMessage();
                    }

                    joiningLobbyAnimation.cancel();

                    // Process finished
                    login.setBusy(false);

                    if (exception != null) {
                        login.notifyAllObservers(exception);
                        return;
                    }

                    // At this point player can join the lobby.
                    MainState.player_uuid = player_uuid;
                    MainState.roomCode = code;
                    Platform.runLater(() -> showLobby());
                }
            };
            // Run function after 1sec, give space for the fetching animation to run.
            new Timer().schedule(task, 1000);
        }
    }

    //Host game
    public void host(TextField inputUsername) {
        String username = inputUsername.getText();
        if (username.isBlank()) {
            login.notifyAllObservers("Fill in all the required fields");
            return;
        }

        if(this.checkUsername(username)) {
            login.notifyAllObservers("Your username must be between " + Integer.toString(CHARACTER_MIN) + " and " + Integer.toString(CHARACTER_MAX) + " characters long");
            return;
        }

        if (!login.getBusy()) {
            // Spam protection
            login.setBusy(true);

            // Creating lobby... loading animation
            Timer creatingLobbyAnimation = getLoadingAnimation("Creating lobby");

            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    try {
                        String player_uuid = generateUUID();
                        Player host = new Player(username, player_uuid, true);
                        String code = MainState.firebaseService.addLobby(host);
                        login.setBusy(false);
                        creatingLobbyAnimation.cancel();

                        MainState.player_uuid = player_uuid;
                        MainState.roomCode = code;
                        Platform.runLater(() -> showLobby());
                    } catch (Exception e) {
                        creatingLobbyAnimation.cancel();
                        login.notifyAllObservers(e.getMessage());
                        e.printStackTrace();
                    }
                }
            };

            // Run function after 1sec, give space for the fetching animation to run.
            new Timer().schedule(task, 1000);
        }
    }

    // Private

    private void showLobby() {
        Scene scene = new Scene(new LobbyView(), MainState.SCREEN_WIDTH, MainState.SCREEN_HEIGHT);
        scene.getStylesheets().add(MainState.MenuCSS);
        MainState.primaryStage.setScene(scene);
    }

    private Timer getLoadingAnimation(String message) {
        TimerTask timerTask = new TimerTask() {
            int n = 0;
            @Override
            public void run() {
                n = (n + 1) % 4;
                String dots = new String(new char[n]).replace("\0", ".");
                login.notifyAllObservers(message + dots);
            }
        };
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 0, 200);
        return timer;
    }

    private String generateCode() {
        return Integer.toString((int) Math.floor(Math.random() * (999999 - 100000 + 1) + 100000));
    }

    private String generateUUID() {
        return UUID.randomUUID().toString();
    }
}
