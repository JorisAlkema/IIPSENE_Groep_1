package Controller;

import Model.TurnTimer;
import Observers.TurnTimerObserver;

public class TurnTimerController {
    private TurnTimer turnTimer = new TurnTimer();

    public void startTimer(GameController gameController) {
        turnTimer.newCountDownTimer(gameController);
    }

    public void stopTimer() {
        turnTimer.stopTimer();
    }

    public void resetTimer(GameController gameController) {
        stopTimer();
        startTimer(gameController);
    }

    public void registerObserver(TurnTimerObserver turnTimerObserver) {
        turnTimer.registerObserver(turnTimerObserver);
    }
}
