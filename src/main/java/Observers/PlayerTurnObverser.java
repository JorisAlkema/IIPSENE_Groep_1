package Observers;

import Model.Player;
import Model.PlayerTurn;

public interface PlayerTurnObverser {
    void update(PlayerTurn playerTurn);
}
