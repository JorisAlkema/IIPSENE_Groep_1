package Controller;

import App.MainState;
import Model.HandModel;
import Model.Player;
import Observers.HandObserver;

public class HandController {
    private final Player localPlayer;
    private final HandModel handModel;

    public HandController() {
        localPlayer = MainState.firebaseService.getPlayerFromLobby(MainState.roomCode, MainState.player_uuid);
        handModel = HandModel.getInstance();
    }

    public void registerObserver(HandObserver observer) {
        handModel.registerObserver(observer);
    }

    public void unregisterObserver(HandObserver observer) {
        handModel.unregisterObserver(observer);
    }
}
