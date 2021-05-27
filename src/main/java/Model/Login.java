package Model;

import Service.FirebaseService;
import View.LoginObserver;
import View.MainMenuView;
import View.Observer;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class Login {
    private Stage primaryStage;
    private List<LoginObserver> loginObservers = new ArrayList<>();
    private FirebaseService firebaseService;
    private String player_uuid;
    private String room_code;

    public Login(Stage primaryStage) {
        this.firebaseService = new FirebaseService();
        this.primaryStage = primaryStage;

        // Might be moving this to lobby
        this.primaryStage.setOnCloseRequest(e -> {
            disconnect();
        });
    }

    // Methods seen by the controller
    public void returnToMenu() {
        // Remove in production testing purposes
        disconnect();
        Scene scene = new Scene(new MainMenuView(primaryStage), primaryStage.getScene().getWidth(), primaryStage.getScene().getHeight());
        String css = "css/styling.css";
        scene.getStylesheets().add(css);
        primaryStage.setScene(scene);
    }

    // TODO: LobbyView
    public void join(String username, String code) {
        if (username.isBlank() || code.isBlank()) {
            updateInterface("Fill in all the required fields");
            return;
        }

        // Joining lobby... loading animation
        Timer joiningLobby = getLoadingAnimation("Joining lobby");

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

                joiningLobby.cancel();

                if (exception != null) {
                    updateInterface(exception);
                    return;
                }

                // At this point player can join the lobby.
                room_code = code;


            }
        };
        // Run function after 1sec, give space for the fetching animation to run.
        new Timer().schedule(task, 1000);
    }

    public void host(String username) {
        if (username.isBlank()) {
            updateInterface("Fill in all the required fields");
            return;
        }

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

                room_code = code;
                creatingLobbyAnimation.cancel();

                // Go to lobby view
            }
        };
        // Run function after 1sec, give space for the fetching animation to run.
        new Timer().schedule(task, 1000);
    }

    public void disconnect() {
        if (player_uuid != null && room_code != null) {
            firebaseService.removePlayer(player_uuid, room_code);
            player_uuid = null;
            room_code = null;

            // if you were the last player remove the room
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
                updateInterface(message + dots);
            }
        };
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 0, 200);
        return timer;
    }

    // Observable methods
    public void addObserver(LoginObserver loginObserver) {
        this.loginObservers.add(loginObserver);
    }

    public void removeObserver(LoginObserver loginObserver) {
        this.loginObservers.remove(loginObserver);
    }

    public void updateInterface(String message) {
        for (LoginObserver x : loginObservers) {
            x.update(message);
        }
    }
}
