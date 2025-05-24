package services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.lang.ClassNotFoundException;

import models.CoworkingSpace;
import models.Reservation;
import exceptions.InvalidReservationTimeException;

public class ReservationService {
    private static List<Reservation> reservations = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    public static void makeReservation() {
        CoworkingService.viewAvailableSpaces();
        String name;
        String spaceId;
        String date;
        String startTime;
        String endTime;

        while (true) {
            System.out.print("\nEnter space ID to reserve (or 'cancel' to abort): ");
            spaceId = scanner.nextLine().trim();

            if (spaceId.equalsIgnoreCase("cancel")) {
                System.out.println("Reservation cancelled.");
                return;
            }

            Optional<CoworkingSpace> spaceOpt = CoworkingService.getSpaceById(spaceId);
            if (spaceOpt.isEmpty()) {
                System.out.println("Space with this ID not found. Please try again.");
                continue;
            }
            if (!spaceOpt.get().getAvailability()) {
                System.out.println("This space is already booked. Please choose another.");
                continue;
            }
            break;
        }

        while (true) {
            System.out.print("Enter your name (2-50 characters): ");
            name = scanner.nextLine().trim();
            if (name.length() >= 2 && name.length() <= 50) {
                break;
            }
            System.out.println("Name must be between 2 and 50 characters.");
        }

        while (true) {
            System.out.print("Enter date (DD/MM/YYYY): ");
            date = scanner.nextLine().trim();
            if (date.matches("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$")) {
                break;
            }
            System.out.println("Invalid date format. Please use DD/MM/YYYY.");
        }

        while (true) {
            System.out.print("Enter start time (HH:MM, 09:00-18:00): ");
            startTime = scanner.nextLine().trim();

            System.out.print("Enter end time (HH:MM, 09:00-18:00): ");
            endTime = scanner.nextLine().trim();

            try {
                if (!startTime.matches("^(09|1[0-8]):([0-5][0-9])$") ||
                        !endTime.matches("^(09|1[0-8]):([0-5][0-9])$")) {
                    System.out.println("Invalid time format or outside working hours (09:00-18:00).");
                    continue;
                }

                if (startTime.compareTo(endTime) >= 0) {
                    throw new InvalidReservationTimeException("End time must be after start time");
                }
                break;
            } catch (InvalidReservationTimeException e) {
                System.out.println(e.getMessage());
            }
        }

        Reservation reservation = new Reservation(spaceId, name, date, startTime, endTime);
        reservations.add(reservation);
        CoworkingService.getSpaceById(spaceId).ifPresent(space -> space.setAvailability(false));

        System.out.println("Reservation created successfully! Reservation ID: " + reservation.getId());
    }

    public static void viewAllReservations() {
        System.out.println("\nAll Reservations:");
        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
        } else {
            for (Reservation res : reservations) {
                System.out.println(res);
            }
        }
    }

    public static void viewMyReservations() {
        System.out.print("\nEnter your name: ");
        String name = scanner.nextLine();

        System.out.println("Your Reservations:");
        boolean found = false;

        for (Reservation res : reservations) {
            if (res.getCustomerName().equalsIgnoreCase(name)) {
                System.out.println(res);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No reservations found for " + name);
        }
    }

    public static void cancelReservation() {
        viewMyReservations();

        System.out.print("\nEnter reservation ID to cancel: ");
        String id = scanner.nextLine();

        boolean removed = false;
        for (Reservation reservation : reservations) {
            if (reservation.getId().equals(id)) {
                CoworkingService.getSpaceById(reservation.getSpaceId())
                        .ifPresent(space -> space.setAvailability(true));
                reservations.remove(reservation);
                removed = true;
                break;
            }
        }

        if (removed) {
            System.out.println("Reservation cancelled successfully!");
        } else {
            System.out.println("Reservation with ID " + id + " not found.");
        }
    }

    public static void removeReservationsForSpace(String spaceId) {
        reservations.removeIf(reservation -> reservation.getSpaceId().equals(spaceId));
    }

    public static void saveReservationsToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("reservations.dat"))) {
            oos.writeObject(reservations);
            System.out.println("Reservations saved to file successfully!");
        } catch (IOException e) {
            System.out.println("Error saving reservations to file: " + e.getMessage());
        }
    }

    public static void loadReservationsFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("reservations.dat"))) {
            @SuppressWarnings("unchecked")
            List<Reservation> loadedReservations = (List<Reservation>) ois.readObject();
            reservations = loadedReservations;
            System.out.println("Reservations loaded from file successfully!");
        } catch (IOException e) {
            System.out.println("Error: No reservations file found or error reading it: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Error: Class definition mismatch in reservations file: " + e.getMessage());
        } catch (ClassCastException e) {
            System.out.println("Error: Data format mismatch in reservations.dat. Expected List<Reservation>. " + e.getMessage());
            reservations = new ArrayList<>();
        }
    }
}