package Model;

import Service.FirebaseService;
import View.MainMenuView;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class Login {
    private FirebaseService firebaseService;
    private String player_uuid;
    public Login() {
        try {
            firebaseService = new FirebaseService();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void returnToMenu(Stage primaryStage) {
        Scene scene = new Scene(new MainMenuView(primaryStage), primaryStage.getScene().getWidth(), primaryStage.getScene().getHeight());
        String css = "css/styling.css";
        scene.getStylesheets().add(css);
        primaryStage.setScene(scene);
    }

    public void join(TextField inputUsername, TextField inputCode) {
        // Values from input fields
        String username = inputUsername.getText();
        String code = inputCode.getText();

        // Get room info from firebase
        List<QueryDocumentSnapshot> lobbyDocs = firebaseService.fetchRoom(code);
        if (lobbyDocs.size() != 0) {
            // If room is found
            player_uuid = generateUUID();
            firebaseService.addPlayer(player_uuid, createPlayer(username, false), code);
        } else {
            System.out.println("Room not found");
            inputCode.setText("");
            inputCode.setPromptText("Room not found");
        }
    }

    private Map<String, Object> createPlayer(String username, Boolean host) {
        Map<String, Object> playerData = new HashMap<>();
        playerData.put("username", username);
        playerData.put("host", host);
        playerData.put("data", new HashMap<>());
        return playerData;
    }

    private String generateUUID() {
        return UUID.randomUUID().toString();
    }
}
