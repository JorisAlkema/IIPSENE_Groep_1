package Service;

import Model.City;
import Model.DestinationTicket;
import Model.Route;
import Model.RouteCell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class GameSetupService {
    private final static String citiesFile = "text/cities.txt";
    private final static String routeFile = "text/routes.txt";
    private final static String destinationTicketsFile = "text/destination_tickets.txt";

    private final ArrayList<City> cities;
    private final ArrayList<Route> routes;
    private final ArrayList<DestinationTicket> destinationTickets;

    static GameSetupService gameSetupService;

    public GameSetupService() {
        this.cities = readCitiesFromFile(citiesFile);
        this.routes = readRoutesFromFile(routeFile);
        this.destinationTickets = readDestinationTicketsFromFile(destinationTicketsFile);
        //addNeighborCities();
    }

    public static GameSetupService getInstance() {
        if (gameSetupService == null) {
            gameSetupService = new GameSetupService();
        }

        return gameSetupService;
    }

    public void addNeighborCities() {
        for (City city : cities) {
            ArrayList<City> neighborCities = new ArrayList<>();
            for (Route route : routes) {
                if (route.getFirstCity().equals(city)) {
                    neighborCities.add(route.getSecondCity());
                } else if (route.getSecondCity().equals(city)) {
                    neighborCities.add(route.getFirstCity());
                }
            }

            city.setNeighborCities(neighborCities);
        }
    }

    public void removeNeighborCities() {
        for (City city : cities) {
            city.getNeighborCities().clear();
            city.setVisited(false);
        }
    }

    public ArrayList<DestinationTicket> readDestinationTicketsFromFile(String filename) {
        ArrayList<DestinationTicket> tickets = new ArrayList<>();

        try {
            InputStreamReader inputStreamReader = new InputStreamReader(getClass().getClassLoader().getResourceAsStream(filename));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();

            while (line != null) {
                if (line.startsWith("/")) {
                    line = bufferedReader.readLine();
                    continue;
                }

                String[] strings = line.split(" ");
                if (strings.length != 4) {
                    System.err.println("Error: " + Arrays.toString(strings) + " is not of length 4");
                    break;
                }

                City firstCity = null;
                City secondCity = null;
                for (City city : this.cities) {
                    if (city.getName().equals(strings[0])) {
                        firstCity = city;
                    } else if (city.getName().equals(strings[1])) {
                        secondCity = city;
                    }
                }

                if (firstCity == null || secondCity == null) {
                    System.err.println(line + " has incorrectly formatted or unexisting cities");
                }

                int points = -1;
                try {
                    points = Integer.parseInt(strings[2]);
                } catch (NumberFormatException numberFormatException) {
                    System.err.println(numberFormatException.getMessage());
                }

                tickets.add(new DestinationTicket(firstCity, secondCity, points, strings[3]));
                line = bufferedReader.readLine();
            }
            bufferedReader.close();

        } catch (IOException ioException) {
            System.err.println(ioException.getMessage());
        }
        return tickets;
    }

    private ArrayList<City> readCitiesFromFile(String filename) {
        ArrayList<City> cities = new ArrayList<>();

        try {
            InputStreamReader inputStreamReader = new InputStreamReader(getClass().getClassLoader().getResourceAsStream(filename));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                if (line.startsWith("/")) {
                    line = bufferedReader.readLine();
                    continue;
                }

                String[] strings = line.split(" ");
                if (strings.length != 3) {
                    System.err.println("Error: " + Arrays.toString(strings) + " is not of length 3");
                    break;
                }

                double[] doubles = new double[strings.length - 1];
                for (int i = 0; i < doubles.length; i++) {
                    try {
                        doubles[i] = Double.parseDouble(strings[i + 1]);
                    } catch (NumberFormatException numberFormatException) {
                        System.err.println(numberFormatException.getMessage());
                    }
                }

                cities.add(new City(strings[0], doubles[0], doubles[1]));
                line = bufferedReader.readLine();
            }
            bufferedReader.close();

        } catch (IOException ioException) {
            System.err.println(ioException.getMessage());
        }
        return cities;
    }


    private ArrayList<Route> readRoutesFromFile(String filename) {
        ArrayList<Route> routes = new ArrayList<>();

        try {
            InputStreamReader inputStreamReader = new InputStreamReader(getClass().getClassLoader().getResourceAsStream(filename));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();

            while (line != null) {
                // Skip over comments
                if (line.startsWith("/")) {
                    line = bufferedReader.readLine();
                    continue;
                }

                City firstCity = null;
                City secondCity = null;
                int locomotives = 0;
                ArrayList<RouteCell> routeCells = new ArrayList<>();
                String[] splitLine = line.split(" ");

                for (City city : this.cities) {
                    if (city.getName().equals(splitLine[0])) {
                        firstCity = city;
                    } else if (city.getName().equals(splitLine[1])) {
                        secondCity = city;
                    }
                }

                if (firstCity == null || secondCity == null) {
                    System.err.println(line + " has incorrectly formatted or unexisting cities");
                }

                String color = splitLine[2];
                String type = splitLine[3];
                if (type.equals("FERRY")) {
                    locomotives = Integer.parseInt(splitLine[4]);
                }

                line = bufferedReader.readLine();
                splitLine = line.split(" ");
                while (splitLine.length == 3) {
                    double[] doubles = new double[3];
                    for (int i = 0; i < 3; i++) {
                        doubles[i] = Double.parseDouble(splitLine[i]);
                    }

                    routeCells.add(new RouteCell(doubles[0], doubles[1], doubles[2]));
                    line = bufferedReader.readLine();
                    if (line == null) {
                        break;
                    }

                    splitLine = line.split(" ");
                }

                Route route = new Route(firstCity, secondCity, routeCells, color, type, locomotives);
                routes.add(route);
            }
            bufferedReader.close();

        } catch (IOException ioException) {
            System.err.println(ioException.getMessage());
        }
        return routes;
    }

    public ArrayList<DestinationTicket> getDestinationTickets() {
        return destinationTickets;
    }

    public ArrayList<Route> getRoutes() {
        return routes;
    }

    public ArrayList<City> getCities() {
        return cities;
    }
}
