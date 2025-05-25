package com.coworking.models;

import java.io.Serial;
import java.io.Serializable;
import com.coworking.utils.IdGenerator;

public class Reservation implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String id;
    private final String spaceId;
    private final String customerName;
    private final String date;
    private final String startTime;
    private final String endTime;

    public Reservation(String spaceId, String customerName, String date, String startTime, String endTime) {
        this.id = IdGenerator.generateReservationId();
        this.spaceId = spaceId;
        this.customerName = customerName;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getId() {
        return id;
    }

    public String getSpaceId() {
        return spaceId;
    }

    public String getCustomerName() {
        return customerName;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + id + ", Space ID: " + spaceId +
                ", Customer: " + customerName + ", Date: " + date +
                ", Time: " + startTime + " - " + endTime;
    }
}