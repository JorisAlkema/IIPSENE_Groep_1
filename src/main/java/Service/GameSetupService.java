package Service;

import Model.DestinationTicket;
import Model.RouteCell;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class GameSetupService {

    private ArrayList<DestinationTicket> destinationTickets;

    // This class should be used to help initialize the game, using methods to
    // read Cities, RouteCells, Routes, DestinationTickets and more from files
    // Could also be used to deal cards at the start of the game if no other class is made for this
    public GameSetupService() {
        // ...


    }

    // Read DestinationTickets from file
    public ArrayList<DestinationTicket> readDestinationTicketsFromFile(String filename) {
        // ...
        return destinationTickets;
    }


    /**
     * Read locations of individual RouteCells from a file
     * @param filename The name of the file containing the RouteCell locations
     * @return An ArrayList of the RouteCells described in the file
     */
    public ArrayList<RouteCell> readRouteCellsFromFile(String filename) {
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
