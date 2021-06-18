package ServiceTest;

import Model.DestinationTicket;
import Service.GameSetupService;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class GameSetupServiceTest {

    @Test
    public void Should_Contain46DestinationTickets_When_DestinationTicketsAreLoadedFromFile() {
        // arrange
        final int expectedCards = 46;
        // act
        ArrayList<DestinationTicket> destinationTickets = GameSetupService.getInstance().getDestinationTickets();
        // assert
        assertThat(expectedCards, is(destinationTickets.size()));
    }
}
