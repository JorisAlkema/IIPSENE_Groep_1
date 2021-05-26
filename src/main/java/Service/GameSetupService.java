package Service;

import Model.DestinationTicket;
import Model.RouteCell;
import javafx.event.Event;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class GameSetupService {

    private ArrayList<DestinationTicket> destinationTickets;
    private ArrayList<RouteCell> routeCells;
    private ArrayList<Rectangle> rectangleOverlays;
    private final static String routeCellFile = "src/main/resources/text/routes.txt";

    // This class should be used to help initialize the game, using methods to
    // read Cities, RouteCells, Routes, DestinationTickets and more from files
    // Could also be used to deal cards at the start of the game if no other class is made for this


    // Lees cities file, maak arraylist
    // Lees routecell file, maak routes met routecells
    public GameSetupService() {
        this.routeCells = readRouteCellsFromFile(routeCellFile);
        this.rectangleOverlays = new ArrayList<>();
        this.destinationTickets = new ArrayList<>();


    }

    // Read DestinationTickets from file
    public ArrayList<DestinationTicket> readDestinationTicketsFromFile(String filename) {
        // ...
        return destinationTickets;
    }

    public ArrayList<Circle> createCityOverlays() {
        ArrayList<Circle> circleList = new ArrayList<>();
        // Zagrab
        circleList.add(createCircleOverlay(72, 203));
        //
        circleList.add(createCircleOverlay(80, 205));
        return circleList;
    }

    private Circle createCircleOverlay(int x, int y) {
        Circle circle = new Circle();
        circle.setTranslateX(x);
        circle.setTranslateY(y);
        circle.setRadius(15);
        circle.addEventHandler(MouseEvent.ANY, new OverlayEventHandler(
                e -> {
                    if (circle.getFill().equals(Color.TRANSPARENT)) {
                        circle.setFill( Color.BLACK);
                    } else {
                        circle.setFill(Color.TRANSPARENT);
                    }
                },
                Event::consume)
        );
        return circle;
    }


    public ArrayList<Rectangle> createMapViewOverlays() {
        for (RouteCell routeCell : this.routeCells) {
            this.rectangleOverlays.add( createRectangleOverlay(routeCell) );
        }
        return rectangleOverlays;
    }

    /**
     * Creates a Rectangle object to overlay on the MapView background image. The rectangle
     * overlay represents the user to interact with the given RouteCell or the Route that
     * it is a part of. The rectangles height, width and EventHandler are currently added
     * in the MapView class
     * @param routeCell an individual RouteCell
     * @return a javafx Rectangle object corresponding to the given RouteCell
     */
    private Rectangle createRectangleOverlay(RouteCell routeCell) {
        Rectangle rectangle = new Rectangle();
        // Divide by two because the file contains offsets for the large map,
        // and the MapView starts by showing the small map.
        rectangle.setTranslateX(routeCell.getOffsetX() / 2);
        rectangle.setTranslateY(routeCell.getOffsetY() / 2);
        rectangle.setRotate(routeCell.getRotation());
        rectangle.setFill(Color.TRANSPARENT);
        return rectangle;
    }


    /**
     * Read locations of individual RouteCells from a file
     * @param filename The name of the file containing the RouteCell locations
     * @return An ArrayList of the RouteCells described in the file
     */
    private ArrayList<RouteCell> readRouteCellsFromFile(String filename) {
        ArrayList<RouteCell> routeCellList = new ArrayList<>();
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
                    System.err.println("Error: " + Arrays.toString(strings) + " is not of length 3");
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
                routeCellList.add(new RouteCell(doubles[0], doubles[1], doubles[2]));
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (IOException ioException) {
            System.err.println(ioException.getMessage());
        }
        return routeCellList;
    }






}
