package Model;

import Service.FirebaseService;
import Service.Observable;
import View.MainMenuView;
import com.google.cloud.firestore.*;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

// Players can join together in a Lobby before they start the game. This means a Lobby is basically a collection of
// Players and a shared roomCode. Once enough players are ready, the host can start the game.
// Possible methods: join, leave, start, (changeHost?), ..
public class Lobby extends Observable {
    private FirebaseService firebaseService;
    private ListenerRegistration playerEventListener;
    private String roomCode;
    private String player_uuid;
    private Map<String, Objects> players;

    private Stage primaryStage;

    public Lobby(Stage primaryStage, String player_uuid, String roomCode) {
        this.primaryStage = primaryStage;
        this.roomCode = roomCode;
        this.player_uuid = player_uuid;
        this.firebaseService = new FirebaseService();
        attachListener();

        // Disconnect when player closes the program!
        this.primaryStage.setOnCloseRequest(e -> {
            disconnect();
        });
    }

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
        DocumentReference playerDocument = firebaseService.getDocumentReference(roomCode, "players");
        playerEventListener = playerDocument.addSnapshotListener((documentSnapshot, e) -> {

            String players = "";
            if (documentSnapshot != null) {
                Set<String> all_players = Objects.requireNonNull(documentSnapshot.getData()).keySet();

                for (String player_id : all_players) {
                    Object data = documentSnapshot.getData().get(player_id);
                    Map<String, Object> playerData = (Map<String, Object>) data;
                    if ((Boolean) playerData.get("host")) {
                        players += playerData.get("username") + ": Host" + "\n";
                    } else {
                        players += playerData.get("username") + "\n";
                    }
                }
                notifyAllObservers(players);
            }
        });
    }

    public void detachListener() {
        playerEventListener.remove();
    }
}
