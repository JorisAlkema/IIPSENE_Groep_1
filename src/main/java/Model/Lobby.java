package Model;

import Service.FirebaseService;
import Service.Observable;
import Service.Observer;
import View.MainMenuView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.cloud.firestore.*;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    private Player[] players;
    private Stage primaryStage;

    public Lobby(Stage primaryStage, String player_uuid, String roomCode) {
        this.primaryStage = primaryStage;
        this.roomCode = roomCode;
        this.player_uuid = player_uuid;
        this.firebaseService = new FirebaseService();

        // Disconnect when player closes the program!
        this.primaryStage.setOnCloseRequest(e -> {
            disconnect();
        });

        Platform.runLater(() -> {
            attachListener();
            updatePartyCode(roomCode);
            updateMessage("Retrieving room data...\n");
        });
    }

    // Accessible to controller

    public void disconnect() {
        if (player_uuid != null && roomCode != null) {
            // remove listener
            detachListener();
            // remove yourself from the room
            firebaseService.removePlayer(player_uuid, roomCode);

            // if you were the last player remove the room


        }
    }

    public void returnToMenu() {
        Scene scene = new Scene(new MainMenuView(primaryStage), primaryStage.getScene().getWidth(), primaryStage.getScene().getHeight());
        String css = "css/styling.css";
        scene.getStylesheets().add(css);
        primaryStage.setScene(scene);
    }

    public void attachListener() {
        playerEventListener = firebaseService.getDocumentReference("rooms", roomCode).addSnapshotListener((document, e) -> {
            if (document != null && document.getData() != null) {
                updatePlayers(document.getData());
            }
        });
    }

    public void detachListener() {
        playerEventListener.remove();
    }

    // Handle new data from eventlistener
    private void updatePlayers(Map<String, Object> data) {
        Map<String, Object> all_players = (Map<String, Object>) data.get("players");

        Set<String> uuids = all_players.keySet();
        Player[] players = new Player[uuids.size()];

        int index = 0;
        for (String id : uuids) {
            Map<String, Object> playerData = (Map<String, Object>) all_players.get(id);
            String username = (String) playerData.get("username");
            players[index] = new Player(username, id);
            index++;
        }

        this.players = players;

        Map<String, Object> viewData = new HashMap<>();
        viewData.put("players", players);
        viewData.put("message", "Waiting for host to start the game");
        notifyAllObservers(viewData);

    }

    // Update message on the view
    private void updateMessage(String message) {
        Map<String, Object> viewData = new HashMap<>();
        viewData.put("message", message);
        notifyAllObservers(viewData);
    }

    // Update partycode on the view
    private void updatePartyCode(String partycode) {
        Map<String, Object> viewData = new HashMap<>();
        viewData.put("partycode", partycode);
        notifyAllObservers(viewData);
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
