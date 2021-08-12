package vending;

public class Main {

    public static void main(String[] args) {
        IVendingMachine vendingMachine;
        if (args.length > 0) {
            vendingMachine = new VendingMachineImpl(args[0]);
        } else {
            vendingMachine = new VendingMachineImpl();
        }
        UserInterface UI = new UserInterface(vendingMachine);

        UI.run();
    }
}
