package Service;

import java.util.ArrayList;

public interface Observable {
    void registerObserver(Observer observer);
    void unregisterObserver(Observer observer);
    void notifyAllObservers(Object o);
}