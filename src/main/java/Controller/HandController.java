package Controller;

import Model.HandModel;
import Observers.HandObserver;

public class HandController {
    private final HandModel handModel;

    public HandController() {
        handModel = HandModel.getInstance();
    }

    public void registerObserver(HandObserver observer) {
        handModel.registerObserver(observer);
    }

    public void unregisterObserver(HandObserver observer) {
        handModel.unregisterObserver(observer);
    }
}
