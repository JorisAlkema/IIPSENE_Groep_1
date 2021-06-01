package Controller;

import Model.MapModel;

public class MapController {
    private final MapModel mapModel;


    public MapController(MapModel mapModel) {
        this.mapModel = mapModel;
    }

    public MapModel getMapModel() {
        return mapModel;
    }
}
