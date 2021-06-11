package Observers;

import Model.Player;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public interface MapObserver {
    void update(boolean zoomedIn, ImageView backgroundImage);
}
