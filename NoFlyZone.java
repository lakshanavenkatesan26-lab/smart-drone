package model;

public class NoFlyZone {
    private String zoneName;
    private Position center;
    private double radius;

    public NoFlyZone(String zoneName, Position center, double radius) {
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

    @Override
    public String toString() {
        return "NoFlyZone[" + zoneName + "] center=" + center + " radius=" + radius;
    }
}