package vending;

import java.util.Scanner;

public class UserInterface {
    IVendingMachine vendingMachine;
    boolean isRunning = false;

    public UserInterface(IVendingMachine vendingMachine) {
        this.vendingMachine = vendingMachine;
    }

    public void run() {
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

    private void parseCommand(String[] args) {
        String command = args[0];

        switch (command) {
            case "quit":
                if (args.length > 1) {
                    System.out.println("Invalid command");
                    return;
                }
                isRunning = false;
                break;
            case "show":
                if (args.length > 1) {
                    System.out.print("Invalid command");
                    return;
                }
                System.out.println(vendingMachine.showItems());
                break;

            case "select":
                if (args.length != 2) {
                    System.out.println("Invalid command");
                    return;
                }
                int UID;
                try {
                    UID = Integer.parseInt(args[1]);
                } catch (Exception e) {
                    System.out.println("Invalid command");
                    return;
                }
                vendingMachine.selectProduct(UID);
                break;
            default:
                System.out.println("Invalid command");
        }
    }
}
