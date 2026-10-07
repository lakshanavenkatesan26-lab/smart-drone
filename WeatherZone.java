package model;

public class WeatherZone {
    private String zoneName;
    private Position center;
    private double radius;

    public WeatherZone(String zoneName, Position center, double radius) {
        this.zoneName = zoneName;
        this.center = center;
        this.radius = radius;
    }

    public String getZoneName() { return zoneName; }
    public Position getCenter() { return center; }
    public double getRadius() { return radius; }

    public boolean contains(Position point) {
        return center.distanceTo(point) < radius;
    }

    // Calculates a safe position just outside this zone, in the same
    // direction the drone was already heading away from the zone's center.
    public Position calculateReroute(Position dronePosition) {
        double dx = dronePosition.getX() - center.getX();
        double dy = dronePosition.getY() - center.getY();
        double dz = dronePosition.getZ() - center.getZ();

        double distance = center.distanceTo(dronePosition);
        if (distance == 0) {
            // Drone is exactly at the center; push it in an arbitrary direction
            return new Position(center.getX() + radius + 10, center.getY(), center.getZ());
        }

        // Normalize the direction, then place the point just outside the radius
        double scale = (radius + 10) / distance; // +10 as a safety buffer
        double newX = center.getX() + dx * scale;
        double newY = center.getY() + dy * scale;
        double newZ = center.getZ() + dz * scale;

        return new Position(newX, newY, newZ);
    }

    @Override
    public String toString() {
        return "WeatherZone[" + zoneName + "] center=" + center + " radius=" + radius;
    }
}