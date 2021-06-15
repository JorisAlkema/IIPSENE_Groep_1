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
        GameSetupService gameSetupService = GameSetupService.getInstance();
        ArrayList<DestinationTicket> destinationTickets = gameSetupService.getDestinationTickets();
        ArrayList<DestinationTicket> destinationTicketsClone = new ArrayList<>(destinationTickets);

        DestinationTicketController destinationTicketController = new DestinationTicketController(destinationTicketsClone);
        int actOutputBooleanFalse = destinationTicketController.drawTickets(false).size();
        int actOutputBooleanTrue = destinationTicketController.drawTickets(true).size();
        //assert
        assertThat(expOutputBooleanFalse, is(actOutputBooleanFalse));
        assertThat(expOutputBooleanTrue, is(actOutputBooleanTrue));
    }
}
