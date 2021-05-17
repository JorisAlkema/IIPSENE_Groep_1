package View;

import javafx.event.Event;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.*;
import java.util.ArrayList;

/** Constructs a scene with a pannable Map background. */
public class MapView extends ScrollPane {
    private ImageView backgroundImage;
    private ArrayList<Rectangle> rectangles;
    private StackPane layout;
    private boolean zoomedIn;
    private final ImageView bigBackgroundImage;
    private final ImageView smallBackgroundImage;
    private final ImagePattern smallImagePattern;
    private final ImagePattern bigImagePattern;
    private static final double smallCellWidth = 35; // 70x23 for large, 35x12 for small
    private static final double smallCellHeight = 12;
    private static final double bigCellWidth = 70;
    private static final double bigCellHeight = 23;

    public MapView() {
        super();
        bigBackgroundImage = new ImageView("Map_Big.jpg");
        smallBackgroundImage = new ImageView("Map_Small.jpg");
        bigImagePattern = new ImagePattern(new Image("Train.png"));
        smallImagePattern = new ImagePattern(new Image("Train-small.png"));
        this.backgroundImage = smallBackgroundImage;
        setContent(initLayout());
        // Hide scrollbars
        setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        zoomedIn = false;
    }

    public void zoomIn() {
        if (zoomedIn) {
            return;
        }
        this.backgroundImage = bigBackgroundImage;
        layout.getChildren().set(0, this.backgroundImage);
        setPannable(true);
        for (Rectangle rectangle : rectangles) {
            rectangle.setWidth(bigCellWidth);
            rectangle.setHeight(bigCellHeight);
            rectangle.setTranslateX(rectangle.getTranslateX() * 2);
            rectangle.setTranslateY(rectangle.getTranslateY() * 2);
            if ( ! rectangle.getFill().equals(Color.TRANSPARENT)) {
                rectangle.setFill(bigImagePattern);
            }
        }
        this.layout();
        setHvalue(getHmin() + (getHmax() - getHmin()) / 2);
        setVvalue(getVmin() + (getVmax() - getVmin()) / 2);
        zoomedIn = true;
    }

    public void zoomOut() {
        if (! zoomedIn) {
            return;
        }
        this.backgroundImage = smallBackgroundImage;
        layout.getChildren().set(0, this.backgroundImage);
        setPannable(false);
        for (Rectangle rectangle : rectangles) {
            rectangle.setWidth(smallCellWidth);
            rectangle.setHeight(smallCellHeight);
            rectangle.setTranslateX(rectangle.getTranslateX() / 2);
            rectangle.setTranslateY(rectangle.getTranslateY() / 2);
            if ( ! rectangle.getFill().equals(Color.TRANSPARENT)) {
                rectangle.setFill(smallImagePattern);
            }
        }
        zoomedIn = false;
    }

    private StackPane initLayout() {
        // Stack overlays on top of the background image
        layout = new StackPane();
        layout.getChildren().add(backgroundImage);
        rectangles = createRouteCells("src/main/resources/routes.txt");
        for (Rectangle rectangle : rectangles) {
            rectangle.setTranslateX(rectangle.getTranslateX() / 2);
            rectangle.setTranslateY(rectangle.getTranslateY() / 2);
        }
        layout.getChildren().addAll(rectangles);

        return layout;
    }

    private Rectangle createCellOverlay(double offsetX, double offsetY, double rotation) {
        Rectangle rectangle = new Rectangle(smallCellWidth, smallCellHeight);
        rectangle.setFill(smallImagePattern);
        rectangle.setRotate(rotation);
        rectangle.setTranslateX(offsetX);
        rectangle.setTranslateY(offsetY);
        rectangle.addEventHandler(MouseEvent.ANY, new OverlayEventHandler(
                        e -> {
                            if (rectangle.getFill().equals(Color.TRANSPARENT)) {
                                rectangle.setFill( zoomedIn ? bigImagePattern : smallImagePattern );
                            } else {
                                rectangle.setFill(Color.TRANSPARENT);
                            }
                        },
                        Event::consume)
//                e -> System.out.println("Dragged"))
        );
        return rectangle;
    }

    private Rectangle createCellOverlay(double[] doubles) {
        if (doubles.length != 3) {
            System.err.println(doubles + " is not of length 3!");
            return null;
        }
        return createCellOverlay(doubles[0], doubles[1], doubles[2]);
    }

    private ArrayList<Rectangle> createRouteCells(String filename) {
        ArrayList<Rectangle> routeCellList = new ArrayList<>();
        try {
            File file = new File(filename);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                // Skip over comments
                if (line.startsWith("/")) {
                    line = bufferedReader.readLine();
                    continue;
                }
                String[] strings = line.split(" ");
                if (strings.length != 3) {
                    System.err.println("Error: " + strings.toString() + " is not of length 3");
                    break;
                }
                double[] doubles = new double[strings.length];
                for (int i = 0; i < strings.length; i++) {
                    try {
                        doubles[i] = Double.parseDouble(strings[i]);
                    } catch (NumberFormatException numberFormatException) {
                        System.err.println(numberFormatException.getMessage());
                    }
                }
                routeCellList.add(createCellOverlay(doubles));
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (IOException ioException) {
            System.err.println(ioException.getMessage());
        }
        return routeCellList;
    }

    public boolean isZoomedIn() {
        return zoomedIn;
    }
}
