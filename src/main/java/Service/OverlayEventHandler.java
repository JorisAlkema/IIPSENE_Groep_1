package Service;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * Custom EventHandler to differentiate between click and drag events on overlaid rectangles on the the MapView
 */
public class OverlayEventHandler implements EventHandler<MouseEvent> {
    private final EventHandler<MouseEvent> clickEventHandler;
    private final EventHandler<MouseEvent> dragEventHandler;
    private boolean dragging = false;

    public OverlayEventHandler(EventHandler<MouseEvent> onClickedEventHandler, EventHandler<MouseEvent> onDraggedEventHandler) {
        this.clickEventHandler = onClickedEventHandler;
        this.dragEventHandler = onDraggedEventHandler;
    }

    @Override
    public void handle(MouseEvent event) {
        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            dragging = false;
        } else if (event.getEventType() == MouseEvent.DRAG_DETECTED) {
            dragging = true;
        } else if (!dragging && event.getEventType() == MouseEvent.MOUSE_RELEASED) {
            clickEventHandler.handle(event);
        } else if (dragging && event.getEventType() == MouseEvent.MOUSE_RELEASED) {
            dragEventHandler.handle(event);
        }
    }
}