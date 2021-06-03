package Service;

public interface Observer {
    void update(Observable observable, Object o, String type);
}
