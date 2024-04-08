package experiments;

import java.util.Scanner;

public class test_terminal {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {

            // Clear the terminal screen
            System.out.print("\033[H\033[2J");
            System.out.flush();


            // Display menu options
            System.out.println("1. Option 1");
            System.out.println("2. Option 2");
            System.out.println("3. Option 3");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            // Read user input
            int choice = scanner.nextInt();
            int choice1 = scanner.nextInt();
            int choice2 = scanner.nextInt();
            int choice3 = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            // Process user input
            switch (choice) {
                case 1:
                    System.out.println("You chose Option 1");
                    // Perform actions for Option 1
                    break;
                case 2:
                    System.out.println("You chose Option 2");
                    // Perform actions for Option 2
                    break;
                case 3:
                    System.out.println("You chose Option 3");
                    // Perform actions for Option 3
                    break;
                case 4:
                    System.out.println("Exiting...");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        // Close scanner when done
        scanner.close();
    }
}