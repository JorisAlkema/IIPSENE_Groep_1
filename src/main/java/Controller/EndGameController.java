package Controller;

import App.MainState;
import Model.GameState;
import Model.Player;
import View.MainMenuView;
import javafx.scene.Scene;

import java.util.*;

public class EndGameController {
    private final GameState gameState;

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

    public ArrayList<Player> getSortedPlayersByPoints() {
        ArrayList<Player> players = gameState.getPlayers();
        Comparator<Player> playerComparator = Comparator.comparingInt(Player::getPoints).reversed();
        players.sort(playerComparator);
        return players;
    }
}
