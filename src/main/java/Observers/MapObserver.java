package Observers;

import javafx.scene.image.ImageView;

public interface MapObserver {
    void update(boolean zoomedIn, ImageView backgroundImage);
}
