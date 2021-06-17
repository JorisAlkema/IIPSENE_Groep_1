package ControllerTest;

import Controller.DestinationTicketController;
import Model.DestinationTicket;
import Service.GameSetupService;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;


public class DestinationTicketControllerTest {

    DestinationTicketController destinationTicketController;

    @Before
    public void setup() {
        GameSetupService gameSetupService = GameSetupService.getInstance();
        ArrayList<DestinationTicket> destinationTickets = gameSetupService.getDestinationTickets();
        ArrayList<DestinationTicket> destinationTicketsClone = new ArrayList<>(destinationTickets);
        destinationTicketController = new DestinationTicketController(destinationTicketsClone);
    }

    @Test
    public void Should_DrawThree_When_DrawExtraIsFalse() {
        //arrange
        int expectedSize = 3;
        //act
        int actualSize = destinationTicketController.drawTickets(false).size();
        //assert
        assertThat(expectedSize, is(actualSize));
    }

    @Test
    public void Should_DrawFour_When_DrawExtraIsTrue() {
        //arrange
        int expectedSize = 4;
        //act
        int actualSize = destinationTicketController.drawTickets(true).size();
        //assert
        assertThat(expectedSize, is(actualSize));
    }

    @Test
    public void Should_DrawOneLongDestinationTicket_When_DrawExtraIsTrue() {
        //arrange
        String expectedType = "long";
        boolean containsExpectedType = false;
        //act
        ArrayList<DestinationTicket> drawnTickets = destinationTicketController.drawTickets(true);
        ArrayList<String> types = new ArrayList<>();
        for (DestinationTicket ticket : drawnTickets) {
            types.add(ticket.getType());
        }
        containsExpectedType = types.contains(expectedType);
        //assert
        assertTrue(containsExpectedType);
    }
}
