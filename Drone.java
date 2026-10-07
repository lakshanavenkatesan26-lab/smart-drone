package model;

public class Drone {
    private String droneId;
    private Position currentPosition;
    private DroneType type;
    private FlightStatus status;

    public Drone(String droneId, Position startPosition, DroneType type) {
        this.droneId = droneId;
        this.currentPosition = startPosition;
        this.type = type;
        this.status = FlightStatus.ACTIVE;
    }

    public String getDroneId() { return droneId; }
    public Position getCurrentPosition() { return currentPosition; }
    public void setCurrentPosition(Position newPos) { this.currentPosition = newPos; }
    public DroneType getType() { return type; }
    public FlightStatus getStatus() { return status; }
    public void setStatus(FlightStatus status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("[%s] ID: %s | Pos: %s | Status: %s",
                type, droneId, currentPosition, status);
    }
}
