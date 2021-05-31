package Model;

import Service.FirebaseService;
import Service.Observable;
import Service.Observer;
import View.LobbyView;
import View.MainMenuView;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.*;

public class Login implements Observable {
    private Stage primaryStage;
    private ArrayList<Observer> observers = new ArrayList<>();
    private FirebaseService firebaseService;

    private String player_uuid;
    private String roomCode;

    private Boolean busy = false;

    public Login(Stage primaryStage) {
        this.firebaseService = new FirebaseService();
        this.primaryStage = primaryStage;
    }

    // Methods seen by the controller
    public void returnToMenu() {
        MainMenuView menuView = new MainMenuView(primaryStage);
        double sceneWidth = primaryStage.getScene().getWidth();
        double sceneHeight = primaryStage.getScene().getHeight();
        Scene scene = new Scene(menuView, sceneWidth, sceneHeight);
        scene.getStylesheets().add("css/styling.css");
        primaryStage.setScene(scene);
    }

    public void join(String username, String code) {
        if (username.isBlank() || code.isBlank()) {
            showMessage("Fill in all the required fields");
            return;
        }

        if (!busy) {
            // Spam protection
            busy = true;

            // Joining lobby... loading animation
            Timer joiningLobbyAnimation = getLoadingAnimation("Joining lobby");

            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    String exception = null;
                    player_uuid = generateUUID();

                    // Tries to add player to the lobby
                    try {
                        firebaseService.addPlayer(generatePlayerMap(player_uuid, username, false), code);
                    } catch (Exception e) {
                        exception = e.getMessage();
                    }

                    joiningLobbyAnimation.cancel();

                    // Process finished
                    busy = false;

                    if (exception != null) {
                        showMessage(exception);
                        return;
                    }

                    // At this point player can join the lobby.
                    roomCode = code;
                    Platform.runLater(() -> showLobbyView(player_uuid, roomCode));
                }
            };
            // Run function after 1sec, give space for the fetching animation to run.
            new Timer().schedule(task, 1000);
        }
    }

    public void host(String username) {
        if (username.isBlank()) {
            showMessage("Fill in all the required fields");
            return;
        }

        if (!busy) {
            // Spam protection
            busy = true;

            // Creating lobby... loading animation
            Timer creatingLobbyAnimation = getLoadingAnimation("Creating lobby");

            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    player_uuid = generateUUID();
                    // If a room already exists with a random code then create a new one
                    String code = generateCode();
                    boolean created = firebaseService.addLobby(code, generatePlayerMap(player_uuid, username, true));
                    while(!created) {
                        code = generateCode();
                        created = firebaseService.addLobby(code, generatePlayerMap(player_uuid, username, true));
                    }

                    roomCode = code;

                    // Process finished
                    busy = false;

                    creatingLobbyAnimation.cancel();
                    // Go to lobby view
                    Platform.runLater(() -> showLobbyView(player_uuid, roomCode));
                }
            };

            // Run function after 1sec, give space for the fetching animation to run.
            new Timer().schedule(task, 1000);
        }
    }

    // Private methods
    private Map<String, Object> generatePlayerMap(String player_uuid, String username, Boolean host) {
        Map<String, Object> playerData = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        data.put("username", username);
        data.put("host", host);
        data.put("data", new HashMap<>());
        playerData.put(player_uuid, data);
        return playerData;
    }

    private String generateUUID() {
        return UUID.randomUUID().toString();
    }

    private String generateCode() {
        return Integer.toString((int) Math.floor(Math.random() * (999999 - 100000 + 1) + 100000));
    }

    private Timer getLoadingAnimation(String message) {
        TimerTask timerTask = new TimerTask() {
            int n = 0;
            @Override
            public void run() {
                n = (n + 1) % 4;
                String dots = new String(new char[n]).replace("\0", ".");
                notifyAllObservers(message + dots);
            }
        };
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 0, 200);
        return timer;
    }

    private void showLobbyView(String player_uuid, String roomCode) {
        Scene scene = new Scene(new LobbyView(primaryStage, player_uuid, roomCode), primaryStage.getScene().getWidth(), primaryStage.getScene().getHeight());
        String css = "css/styling.css";
        scene.getStylesheets().add(css);
        primaryStage.setScene(scene);
    }

    private void showMessage(String message) {
        notifyAllObservers(message);
    }

    /*
    Observer functions
    */

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void unregisterObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyAllObservers(Object o) {
        for (Observer observer : observers) {
            observer.update(this, o);
        }
    }
}
