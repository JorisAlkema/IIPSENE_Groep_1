package ServiceTest;

import Model.DestinationTicket;
import Service.GameSetupService;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class GameSetupServiceTest {

    @Test
    public void areAllDestinationCardsGeneratedByTheGameSetupService() {
        // arrange
        final int expectedCards = 46;
        // act
        ArrayList<DestinationTicket> destinationTickets = GameSetupService.getInstance().getDestinationTickets();
        // assert
        assertThat(expectedCards, is(destinationTickets.size()));
    }
}
