package vending;

import vending.components.IVendingMachine;
import vending.components.UserInterface;
import vending.components.VendingMachineImpl;

public class Main {

    public static void main(String[] args) {
        // creates the vending machine
        IVendingMachine vendingMachine;
        if (args.length > 0) {
            // if a file has been specified as a command line argument, it
            // loads items from there
            vendingMachine = new VendingMachineImpl(args[0]);
        } else {
            // if a file has not been specified, it simply does not load any
            // items (the machine will be empty)
            vendingMachine = new VendingMachineImpl();
        }

        // creates the UI
        UserInterface UI = new UserInterface(vendingMachine);

        // runs the UI
        UI.run();
    }
}
