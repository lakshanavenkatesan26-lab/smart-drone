package model;

public class Position {
    private double x;
    private double y;
    private double z;

    public Position(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double distanceTo(Position other) {
        return Math.sqrt(
                Math.pow(this.x - other.x, 2) +
                        Math.pow(this.y - other.y, 2) +
                        Math.pow(this.z - other.z, 2)
        );
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}