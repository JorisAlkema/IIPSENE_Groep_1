package ControllerTest;

import Controller.EndGameController;
import Model.GameState;
import Model.Player;
import org.junit.Test;

import java.util.ArrayList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class EndGameControllerTest {

    @Test
    public void Should_SortPlayersDescending_When_SortingPlayersByPoint() {
        //arrange
        GameState gameState = new GameState();
        ArrayList<Player> players = new ArrayList<>();
        Player firstPlacePlayer = new Player();
        firstPlacePlayer.setPoints(40);
        Player secondPlacePlayer = new Player();
        secondPlacePlayer.setPoints(30);
        Player thirdPlacePlayer = new Player();
        thirdPlacePlayer.setPoints(20);
        players.add(thirdPlacePlayer);
        players.add(secondPlacePlayer);
        players.add(firstPlacePlayer);
        gameState.setPlayers(players);
        EndGameController endGameController = new EndGameController(gameState);

        ArrayList<Player> expectedSortedPlayers = new ArrayList<>();
        expectedSortedPlayers.add(firstPlacePlayer);
        expectedSortedPlayers.add(secondPlacePlayer);
        expectedSortedPlayers.add(thirdPlacePlayer);

        //act
        ArrayList<Player> actualSortedPlayers = endGameController.getSortedPlayersByPoints();
        //assert
        assertThat(expectedSortedPlayers, is(actualSortedPlayers));
    }

}
