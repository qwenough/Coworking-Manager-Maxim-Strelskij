package services;
import exceptions.InvalidSpaceTypeException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.lang.ClassNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import models.CoworkingSpace;

public class CoworkingService {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Map<String, CoworkingSpace> spaces = new HashMap<>();

    public static void addNewSpace() {
        String type;
        double price;

        while (true) {
            System.out.print("Enter type (open space/private/room) or 'cancel' to abort: ");
            type = scanner.nextLine().trim();

            if (type.equalsIgnoreCase("cancel")) {
                System.out.println("Operation cancelled.");
                return;
            }

            try {
                if (type.isEmpty()) {
                    throw new InvalidSpaceTypeException("Type cannot be empty!");
                }
                if (!type.matches("(?i)open space|private|room")) {
                    throw new InvalidSpaceTypeException("Invalid type! Use: open space, private or room");
                }
                break;
            } catch (InvalidSpaceTypeException e) {
                System.out.println(e.getMessage());
            }
        }

        while (true) {
            System.out.print("Enter price per hour: ");
            String priceInput = scanner.nextLine().trim();

            try {
                price = Double.parseDouble(priceInput);
                if (price > 0) {
                    break;
                }
                System.out.println("Price must be a positive number! Please try again.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format! Please enter a valid price.");
            }
        }

        CoworkingSpace newSpace = new CoworkingSpace(type, price);
        spaces.put(newSpace.getId(), newSpace);
        System.out.println("Space added successfully! ID: " + newSpace.getId());
    }

    public static void removeSpace() {
        viewAllSpaces();
        System.out.print("\nEnter space ID to remove: ");
        String id = scanner.nextLine().trim();

        ReservationService.removeReservationsForSpace(id);

        System.out.println(
                spaces.remove(id) != null
                        ? "Space and its reservations removed successfully!"
                        : "Space with ID " + id + " not found."
        );
    }

    public static void viewAllSpaces() {
        System.out.println("\nAll Coworking Spaces:");
        if (spaces.isEmpty()) {
            System.out.println("No spaces available.");
        } else {
            for (CoworkingSpace space : spaces.values()) {
                System.out.println(space);
            }
        }
    }

    public static void viewAvailableSpaces() {
        System.out.println("\nAvailable Coworking Spaces:");

        spaces.values().stream()
                .filter(CoworkingSpace::getAvailability)
                .forEach(System.out::println);

        if (spaces.values().stream().noneMatch(CoworkingSpace::getAvailability)) {
            System.out.println("No available spaces at the moment.");
        }
    }

    public static Optional<CoworkingSpace> getSpaceById(String id) {
        return Optional.ofNullable(spaces.get(id));
    }

    public static void saveSpacesToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("spaces.dat"))) {
            oos.writeObject(new ArrayList<>(spaces.values()));
            System.out.println("Spaces saved to file successfully!");
        } catch (IOException e) {
            System.out.println("Error saving spaces to file: " + e.getMessage());
        }
    }

    public static void loadSpacesFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("spaces.dat"))) {
            @SuppressWarnings("unchecked")
            List<CoworkingSpace> loadedSpaces = (List<CoworkingSpace>) ois.readObject();
            spaces.clear();
            loadedSpaces.forEach(space -> spaces.put(space.getId(), space));
            System.out.println("Spaces loaded from file successfully!");
        } catch (IOException e) {
            System.out.println("Error: No spaces file found or error reading it: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Error: Incorrect data format in spaces file: " + e.getMessage());
        } catch (ClassCastException e) {
            System.out.println("Error: Data format mismatch in spaces.dat." + e.getMessage());
            spaces.clear();
        }
    }
}