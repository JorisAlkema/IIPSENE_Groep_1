package Controller;

import App.MainState;
import Model.GameState;
import Model.Player;
import View.MainMenuView;
import javafx.scene.Scene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EndGameController {
    private final GameState gameState;
    ArrayList<String> top3 = new ArrayList<>();

    public EndGameController(GameState gameState) {
        this.gameState = gameState;
    }

    public void toMenu() {
        MainState.player_uuid = null;
        MainState.roomCode = null;
        Scene scene = new Scene(new MainMenuView(), MainState.WINDOW_WIDTH, MainState.WINDOW_HEIGHT);
        scene.getStylesheets().add(MainState.menuCSS);
        MainState.primaryStage.setScene(scene);
    }

    private <K, V> K getKey(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public ArrayList<String> topThreePlayers() {
        HashMap<String, Integer> map = new HashMap<>();
        ArrayList<Integer> topPoints = new ArrayList<>();

        map.put("15charactername", 100);
        map.put("15characternama", 50);
        map.put("15characternami", 0);

        for (Player player : gameState.getPlayers()) {
            map.put(player.getName(), player.getPoints());
        }

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            topPoints.add(entry.getValue());
        }

        Collections.sort(topPoints);

        int playerAmount = gameState.getPlayers().size();
        for (int i = 0; i < playerAmount; i++) {
            top3.add(getKey(map, topPoints.get(i)));
        }

        return top3;
    }
}
