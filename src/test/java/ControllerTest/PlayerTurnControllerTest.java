package ControllerTest;

import Controller.PlayerTurnController;
import Model.GameState;
import Model.Player;
import org.junit.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PlayerTurnControllerTest {

    @Test
    public void isNextPlayerInPlayerTurnControllerCalculatedRight() {
        final String FIRST_PLAYER_UUID = UUID.randomUUID().toString();
        final String NEXT_PLAYER_UUID = UUID.randomUUID().toString();

        PlayerTurnController playerTurnController = new PlayerTurnController();
        GameState gameState = new GameState();

        Player player1 = new Player();
        player1.setUUID(FIRST_PLAYER_UUID);

        Player player2 = new Player();
        player2.setUUID(NEXT_PLAYER_UUID);

        gameState.setPlayers(new ArrayList<>());
        gameState.getPlayers().add(player1);
        gameState.getPlayers().add(player2);

        playerTurnController.start(gameState);
        assertThat(NEXT_PLAYER_UUID, is(playerTurnController.getPlayerTurn().getNextPlayerUUID()));

        playerTurnController.nextTurn(gameState);
        assertThat(FIRST_PLAYER_UUID, is(playerTurnController.getPlayerTurn().getNextPlayerUUID()));
    }
}
