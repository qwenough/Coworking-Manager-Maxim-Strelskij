import java.util.Scanner;

import services.CoworkingService;
import services.ReservationService;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        CoworkingService.loadSpacesFromFile();
        ReservationService.loadReservationsFromFile();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            CoworkingService.saveSpacesToFile();
            ReservationService.saveReservationsToFile();
        }));
        while (true) showMainMenu();
    }

    private static void showMainMenu() {
        while (true) {
            System.out.println("""
                    \n╔══════════════════════════════╗
                    ║  WELCOME TO COWORKING SPACE  ║
                    ║            MANAGER           ║
                    ╠══════════════════════════════╣
                    ║ 1. Login as Admin            ║
                    ║ 2. Login as Customer         ║
                    ║ 3. Exit                      ║
                    ╚══════════════════════════════╝
                    
                    Choose an option:\s""");

            String input = scanner.nextLine();

            switch (input) {
                case "1" -> showAdminMenu();
                case "2" -> showCustomerMenu();
                case "3" -> {
                    System.out.println("Goodbye!");
                    System.exit(0);
                }
                default -> showInvalidOption();
            }
        }
    }

    private static void showAdminMenu() {
        while (true) {
            System.out.println("""
                    \n╔══════════════════════════════╗
                    ║         ADMIN MENU           ║
                    ╠══════════════════════════════╣
                    ║ 1. Add a new coworking space ║
                    ║ 2. Remove a coworking space  ║
                    ║ 3. View all reservations     ║
                    ║ 4. View all coworking spaces ║
                    ║ 5. Back to main menu         ║
                    ╚══════════════════════════════╝
                    
                    Choose an option:\s""");

            String input = scanner.nextLine();

            switch (input) {
                case "1" -> CoworkingService.addNewSpace();
                case "2" -> CoworkingService.removeSpace();
                case "3" -> ReservationService.viewAllReservations();
                case "4" -> CoworkingService.viewAllSpaces();
                case "5" -> {
                    return;
                }
                default -> showInvalidOption();
            }
        }
    }

    private static void showCustomerMenu() {
        while (true) {
            System.out.println("""
                    \n╔══════════════════════════════╗
                    ║       CUSTOMER MENU          ║
                    ╠══════════════════════════════╣
                    ║ 1. Browse available spaces   ║
                    ║ 2. Make a reservation        ║
                    ║ 3. View my reservations      ║
                    ║ 4. Cancel a reservation      ║
                    ║ 5. Back to main menu         ║
                    ╚══════════════════════════════╝
                    
                    Choose an option:""");

            String input = scanner.nextLine();

            switch (input) {
                case "1" -> CoworkingService.viewAvailableSpaces();
                case "2" -> ReservationService.makeReservation();
                case "3" -> ReservationService.viewMyReservations();
                case "4" -> ReservationService.cancelReservation();
                case "5" -> {
                    return;
                }
                default -> showInvalidOption();
            }
        }
    }

    private static void showInvalidOption() {
        System.out.println("Invalid option. Please try again.\n");
    }
}

