package ControllerTest;

import Controller.DestinationTicketController;
import Model.DestinationTicket;
import Service.GameSetupService;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class DestinationTicketControllerTest {

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
}
