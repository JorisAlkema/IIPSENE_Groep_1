package Model;

import App.MainState;
import Service.FirebaseService;
import Service.Observable;
import Service.Observer;
import View.MainMenuView;
import com.google.cloud.firestore.*;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.*;

// Players can join together in a Lobby before they start the game. This means a Lobby is basically a collection of
// Players and a shared roomCode. Once enough players are ready, the host can start the game.
// Possible methods: join, leave, start, (changeHost?), ..
public class Lobby implements Observable {
    private ArrayList<Observer> observers = new ArrayList<>();
    private FirebaseService firebaseService;
    private ListenerRegistration playerEventListener;
    private String roomCode;
    private String player_uuid;

    public Lobby(String player_uuid, String roomCode) {
        this.roomCode = roomCode;
        this.player_uuid = player_uuid;
        this.firebaseService = new FirebaseService();

        // Disconnect when player closes the program!
        MainState.primaryStage.setOnCloseRequest(e -> {
            disconnect();
        });

        Platform.runLater(() -> {
            Map<String, Object> partyCode = new HashMap<>();
            partyCode.put("partyCode", roomCode);
            partyCode.put("Retrieving data...\n", roomCode);
            notifyAllObservers(partyCode);

            attachListener();
        });
    }

    // Accessible to controller

    public void disconnect() {
        if (player_uuid != null && roomCode != null) {
            // remove listener
            detachListener();
            // remove yourself from the room
            firebaseService.removePlayer(player_uuid, roomCode);
        }
    }

    public void returnToMenu() {
        Scene scene = new Scene(new MainMenuView(), MainState.SCREEN_WIDTH, MainState.SCREEN_HEIGHT);
        String css = "css/styling.css";
        scene.getStylesheets().add(css);
        MainState.primaryStage.setScene(scene);
    }

    public void attachListener() {
        playerEventListener = firebaseService.getRoomReference(roomCode).addSnapshotListener((document, e) -> {
            if (document != null && document.getData() != null) {
                update(document.getData());
            }
        });
    }

    public void detachListener() {
        playerEventListener.remove();
    }

    public void startRoom() {
        // All players in the room
        Map<String, Object> allPlayers = firebaseService.getAllPlayers(roomCode);

        // Client
        Map<String, Object> player = (Map<String, Object>) allPlayers.get(player_uuid);

        if ((Boolean) player.get("host")) {
            if (allPlayers.size() > 3 && allPlayers.size() <= 5) {
                // Game start
                firebaseService.updateMessageInLobby(roomCode, "The game will be started");
                Map<String, Object> roomData = firebaseService.getRoomData(roomCode);
                roomData.put("ongoing", true);
                firebaseService.updateRoomData(roomData, roomCode);

                // Change view
                
            } else {
                firebaseService.updateMessageInLobby(roomCode, "3 or more players are needed to start the game");
            }
        }
    }

    // Handle new data from eventlistener
    private void update(Map<String, Object> data) {
        notifyAllObservers(data);
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
