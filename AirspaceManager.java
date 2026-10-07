package service;

import model.Drone;
import model.DroneType;
import model.NoFlyZone;
import model.Position;
import model.WeatherZone;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class AirspaceManager {
    private List<Drone> activeDrones;
    private List<NoFlyZone> noFlyZones;
    private List<WeatherZone> weatherZones;
    private double safetyThreshold;

    public AirspaceManager(double safetyThreshold) {
        this.activeDrones = new ArrayList<>();
        this.noFlyZones = new ArrayList<>();
        this.weatherZones = new ArrayList<>();
        this.safetyThreshold = safetyThreshold;
    }

    public void registerDrone(Drone drone) {
        activeDrones.add(drone);
        System.out.println("Registered: " + drone.getDroneId() + " (" + drone.getType() + ")");
    }

    public boolean removeDrone(String droneId) {
        return activeDrones.removeIf(d -> d.getDroneId().equals(droneId));
    }

    public void registerNoFlyZone(NoFlyZone zone) {
        noFlyZones.add(zone);
        System.out.println("No-fly zone added: " + zone);
    }

    public void registerWeatherZone(WeatherZone zone) {
        weatherZones.add(zone);
        System.out.println("Weather zone added: " + zone);
    }

    public void updateLocation(String droneId, Position newPos) {
        for (Drone d : activeDrones) {
            if (d.getDroneId().equals(droneId)) {
                d.setCurrentPosition(newPos);
                System.out.println("Updated " + droneId + " position to " + newPos);
                checkProximity(d);
                checkNoFlyZones(d);
                checkWeatherZones(d);
                return;
            }
        }
        System.out.println("No drone found with ID: " + droneId);
    }

    private void checkProximity(Drone updatedDrone) {
        List<Drone> nearbyDrones = new ArrayList<>();

        for (Drone other : activeDrones) {
            if (!other.getDroneId().equals(updatedDrone.getDroneId())) {
                double distance = updatedDrone.getCurrentPosition().distanceTo(other.getCurrentPosition());
                if (distance < safetyThreshold) {
                    System.out.println("⚠️ ALERT: Collision hazard between "
                            + updatedDrone.getDroneId() + " and " + other.getDroneId()
                            + "! Distance: " + String.format("%.2f", distance) + "m");
                    nearbyDrones.add(other);
                }
            }
        }

        if (!nearbyDrones.isEmpty()) {
            nearbyDrones.add(updatedDrone);
            resolveByPriority(nearbyDrones);
        }
    }

    private void resolveByPriority(List<Drone> conflictingDrones) {
        PriorityQueue<Drone> priorityQueue = new PriorityQueue<>(
                Comparator.comparing(d -> d.getType() == DroneType.EMERGENCY_MEDICAL ? 0 : 1)
        );
        priorityQueue.addAll(conflictingDrones);

        System.out.println("--- Priority resolution order ---");
        int rank = 1;
        while (!priorityQueue.isEmpty()) {
            Drone d = priorityQueue.poll();
            String role = (d.getType() == DroneType.EMERGENCY_MEDICAL)
                    ? "PROCEEDS (emergency priority)"
                    : "YIELD / REROUTE";
            System.out.println(rank + ". " + d.getDroneId() + " [" + d.getType() + "] -> " + role);
            rank++;
        }
    }

    private void checkNoFlyZones(Drone drone) {
        for (NoFlyZone zone : noFlyZones) {
            if (zone.contains(drone.getCurrentPosition())) {
                System.out.println("🚫 WARNING: " + drone.getDroneId()
                        + " has entered restricted zone \"" + zone.getZoneName() + "\"!");
            }
        }
    }

    private void checkWeatherZones(Drone drone) {
        for (WeatherZone zone : weatherZones) {
            if (zone.contains(drone.getCurrentPosition())) {
                Position safePosition = zone.calculateReroute(drone.getCurrentPosition());
                System.out.println("🌩️ " + drone.getDroneId() + " entered weather zone \""
                        + zone.getZoneName() + "\" — automatically rerouting...");
                drone.setCurrentPosition(safePosition);
                System.out.println("✅ " + drone.getDroneId() + " rerouted to safe position " + safePosition);
            }
        }
    }

    public void displayAirspaceStatus() {
        System.out.println("\n--- Current Airspace Traffic ---");
        for (Drone d : activeDrones) {
            System.out.println(d);
        }
        System.out.println("--------------------------------\n");
    }
}