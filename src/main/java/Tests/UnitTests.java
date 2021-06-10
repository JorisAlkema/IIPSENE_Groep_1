package Tests;

import Controller.CardsController;
import Controller.DestinationTicketController;
import Model.DestinationTicket;
import Model.DestinationTicketDeck;
import Model.TrainCard;
import Service.GameSetupService;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class UnitTests {

    @Test
    public void generateClosedDeckHas110Cards(){
        //arrange
        int fullDeck =110;

        //act
        CardsController cardsController = new CardsController();
        ArrayList<TrainCard> deck = cardsController.generateClosedDeck();

        //assert
        assertThat(fullDeck,is(deck.size()));
    }

    @Test
    public void drawTicketsDraws3or4WhenDrawExtraIsTrue(){
        //arrange
        int expOutputBooleanFalse = 3;
        int expOutputBooleanTrue = 4;

        //act
        String destinationTicketsFile = "src/main/resources/text/destination_tickets.txt";
        GameSetupService gameSetupService = new GameSetupService();
        ArrayList<DestinationTicket> destinationTickets = gameSetupService.readDestinationTicketsFromFile(destinationTicketsFile);

        DestinationTicketController destinationTicketController = new DestinationTicketController(destinationTickets);
        int actOutputBooleanFalse = destinationTicketController.drawTickets(false).size();
        int actOutputBooleanTrue = destinationTicketController.drawTickets(true).size();
        //assert
        assertThat(expOutputBooleanFalse, is(actOutputBooleanFalse));
        assertThat(expOutputBooleanTrue, is(actOutputBooleanTrue));
    }



}
