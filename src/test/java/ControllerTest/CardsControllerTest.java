package ControllerTest;

import Controller.CardsController;
import Model.TrainCard;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CardsControllerTest {

    @Test
    public void Should_Contain110Cards_When_ClosedDeckIsInitialized() {
        //arrange
        int fullDeck = 110;

        //act
        CardsController cardsController = new CardsController();
        ArrayList<TrainCard> deck = cardsController.generateClosedDeck();

        //assert
        assertThat(fullDeck, is(deck.size()));
    }
}
