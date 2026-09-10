package com.coworking.utils;

import java.util.UUID;

public class IdGenerator {
    public static String generateSpaceId() {
        return "SP-" + UUID.randomUUID().toString().substring(0, 8);
    }

    public static String generateReservationId() {
        return "RS-" + UUID.randomUUID().toString().substring(0, 8);
    }
}
