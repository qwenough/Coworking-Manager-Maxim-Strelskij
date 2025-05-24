package models;

import java.io.Serial;
import java.io.Serializable;

import utils.IdGenerator;

public class CoworkingSpace implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String id;
    private final String type;
    private final double price;
    private boolean isAvailable;

    public CoworkingSpace(String type, double price) {
        this.id = IdGenerator.generateSpaceId();
        this.type = type;
        this.price = price;
        this.isAvailable = true;
    }

    public String getId() {
        return id;
    }

    public boolean getAvailability() {
        return isAvailable;
    }

    public void setAvailability(boolean available) {
        isAvailable = available;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Type: " + type + ", Price: $" + price +
                ", Status: " + (isAvailable ? "Available" : "Booked");
    }
}