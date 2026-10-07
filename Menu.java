package menu;

import model.Drone;
import model.DroneType;
import model.NoFlyZone;
import model.Position;
import model.WeatherZone;
import service.AirspaceManager;
import util.Validation;

import java.util.Scanner;

public class Menu {
    private final AirspaceManager manager;
    private final Scanner scanner;

    public Menu(AirspaceManager manager) {
        this.manager = manager;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;
        while (running) {
            printOptions();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": registerDroneFlow(); break;
                case "2": updateLocationFlow(); break;
                case "3": removeDroneFlow(); break;
                case "4": manager.displayAirspaceStatus(); break;
                case "5": createNoFlyZoneFlow(); break;
                case "6": createWeatherZoneFlow(); break;
                case "7": running = false; System.out.println("Exiting."); break;
                default: System.out.println("Invalid option, try again.");
            }
        }
    }

    private void printOptions() {
        System.out.println("\n=== Smart Drone Airspace Management System ===");
        System.out.println("1. Register new drone");
        System.out.println("2. Update drone location");
        System.out.println("3. Remove a drone");
        System.out.println("4. Display airspace status");
        System.out.println("5. Create a no-fly zone");
        System.out.println("6. Create a weather zone");
        System.out.println("7. Exit");
        System.out.print("Choose an option: ");
    }

    private void registerDroneFlow() {
        System.out.print("Enter drone ID: ");
        String id = scanner.nextLine().trim();
        if (!Validation.isValidDroneId(id)) {
            System.out.println("Invalid ID. Try again.");
            return;
        }
        double x = readCoordinate("X");
        double y = readCoordinate("Y");
        double z = readCoordinate("Z");

        System.out.print("Emergency medical drone? (yes/no): ");
        String typeInput = scanner.nextLine().trim().toLowerCase();
        DroneType type = typeInput.equals("yes") ? DroneType.EMERGENCY_MEDICAL : DroneType.STANDARD;

        manager.registerDrone(new Drone(id, new Position(x, y, z), type));
    }

    private void updateLocationFlow() {
        System.out.print("Enter drone ID to update: ");
        String id = scanner.nextLine().trim();
        double x = readCoordinate("new X");
        double y = readCoordinate("new Y");
        double z = readCoordinate("new Z");
        manager.updateLocation(id, new Position(x, y, z));
    }

    private void removeDroneFlow() {
        System.out.print("Enter drone ID to remove: ");
        String id = scanner.nextLine().trim();
        boolean removed = manager.removeDrone(id);
        System.out.println(removed ? "Removed " + id : "No drone found with that ID.");
    }

    private void createNoFlyZoneFlow() {
        System.out.print("Enter zone name: ");
        String name = scanner.nextLine().trim();
        double x = readCoordinate("zone center X");
        double y = readCoordinate("zone center Y");
        double z = readCoordinate("zone center Z");
        double radius = readCoordinate("zone radius");

        manager.registerNoFlyZone(new NoFlyZone(name, new Position(x, y, z), radius));
    }

    private void createWeatherZoneFlow() {
        System.out.print("Enter weather zone name (e.g. Storm-1): ");
        String name = scanner.nextLine().trim();
        double x = readCoordinate("zone center X");
        double y = readCoordinate("zone center Y");
        double z = readCoordinate("zone center Z");
        double radius = readCoordinate("zone radius");

        manager.registerWeatherZone(new WeatherZone(name, new Position(x, y, z), radius));
    }

    private double readCoordinate(String label) {
        while (true) {
            System.out.print("Enter " + label + " (0-1000): ");
            try {
                double value = Double.parseDouble(scanner.nextLine().trim());
                if (Validation.isValidCoordinate(value)) return value;
                System.out.println("Must be between 0 and 1000.");
            } catch (NumberFormatException e) {
                System.out.println("That's not a valid number.");
            }
        }
    }
}