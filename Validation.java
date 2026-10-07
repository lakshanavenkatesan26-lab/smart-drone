package util;

public class Validation {
    public static boolean isValidDroneId(String id) {
        return id != null && !id.trim().isEmpty();
    }

    public static boolean isValidCoordinate(double value) {
        return value >= 0 && value <= 1000;
    }
}