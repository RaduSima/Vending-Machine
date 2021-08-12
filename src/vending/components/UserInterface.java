package vending.components;

import vending.Item;

import java.util.Scanner;

public class UserInterface {
    IVendingMachine vendingMachine;
    boolean isRunning = false;

    public UserInterface(IVendingMachine vendingMachine) {
        this.vendingMachine = vendingMachine;
    }

    // Runs the UI
    public void run() {
        printMenu();

        isRunning = true;
        Scanner scanner = new Scanner(System.in);

        while (isRunning) {
            // read command and its arguments
            String input = scanner.nextLine();
            String[] args = input.split(" ");
            if (args.length <= 0) {
                continue;
            }
            parseCommand(args);
        }
    }

    // Prints an intuitive menu
    private void printMenu() {
        System.out.println(
                "\tWelcome to the vending machine! The available commands are:");
        System.out.println("help - shows this menu again");
        System.out.println("quit - exists the program");
        System.out.println(
                "show items - shows details about every currently available item");
        System.out.println(
                "show money - shows all currently stored money (mostly an admin command)");
        System.out.println(
                "select 'item_ID' - select a product with the specified ID." +
                        "\n\tThis puts the vending machine" +
                        " into a processing state, in which you have to enter" +
                        " money or use the 'cancel' command to cancel the request.");
    }

    // Parses commands from user
    private void parseCommand(String[] args) {
        String command = args[0];

        switch (command.toLowerCase()) {
            case "quit" -> handleQuitCommand(args);
            case "show" -> handleShowCommand(args);
            case "select" -> handleSelectCommand(args);
            case "help" -> handleHelpCommand(args);
            default -> System.out.println("Invalid command");
        }
    }

    /* Process different types of commands from the user */
    private void handleQuitCommand(String[] args) {
        if (args.length != 1) {
            System.out.println("Invalid command");
            return;
        }
        isRunning = false;
    }

    private void handleShowCommand(String[] args) {
        if (args.length != 2) {
            System.out.println("Invalid command");
            return;
        }
        if (args[1].equalsIgnoreCase("items")) {
            System.out.println(vendingMachine.showItems());
        } else if (args[1].equalsIgnoreCase("money")) {
            System.out.println(vendingMachine.showMoney());
        } else {
            System.out.println("Invalid command");
        }
    }

    private void handleSelectCommand(String[] args) {
        int UID;
        Item item;
        if (args.length != 2) {
            System.out.println("Invalid command");
            return;
        }
        try {
            UID = Integer.parseInt(args[1]);
        } catch (Exception e) {
            System.out.println("Invalid command");
            return;
        }

        if ((item = vendingMachine.selectProduct(UID)) == null) {
            return;
        }
        vendingMachine.acceptPayment(item);
    }

    private void handleHelpCommand(String[] args) {
        if (args.length != 1) {
            System.out.println("Invalid command");
            return;
        }
        printMenu();
    }
}
