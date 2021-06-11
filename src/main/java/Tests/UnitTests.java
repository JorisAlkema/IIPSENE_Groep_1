package Tests;

import Controller.CardsController;
import Controller.DestinationTicketController;
import Controller.PlayerTurnController;
import Model.DestinationTicket;
import Model.GameState;
import Model.Player;
import Model.TrainCard;
import Service.GameSetupService;
import org.junit.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class UnitTests {

    @Test
    public void generateClosedDeckHas110Cards() {
        //arrange
        int fullDeck = 110;

        //act
        CardsController cardsController = new CardsController();
        ArrayList<TrainCard> deck = cardsController.generateClosedDeck();

        //assert
        assertThat(fullDeck, is(deck.size()));
    }

    @Test
    public void drawTicketsDraws3or4WhenDrawExtraIsTrue() {
        //arrange
        int expOutputBooleanFalse = 3;
        int expOutputBooleanTrue = 4;

        //act
        String destinationTicketsFile = "src/main/resources/text/destination_tickets.txt";
        GameSetupService gameSetupService = GameSetupService.getInstance();
        ArrayList<DestinationTicket> destinationTickets = gameSetupService.readDestinationTicketsFromFile(destinationTicketsFile);

        DestinationTicketController destinationTicketController = new DestinationTicketController(destinationTickets);
        int actOutputBooleanFalse = destinationTicketController.drawTickets(false).size();
        int actOutputBooleanTrue = destinationTicketController.drawTickets(true).size();
        //assert
        assertThat(expOutputBooleanFalse, is(actOutputBooleanFalse));
        assertThat(expOutputBooleanTrue, is(actOutputBooleanTrue));
    }

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
