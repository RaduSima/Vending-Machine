package vending.components;

import vending.exceptions.*;
import vending.money.Bill;
import vending.money.Coin;
import vending.money.Currency;
import vending.Item;

import java.util.Arrays;
import java.util.Scanner;

public class VendingMachineImpl implements IVendingMachine {
    BankStorage bank;
    VendingStorage vendingStorage;
    boolean isSellingItem = false;

    public VendingMachineImpl() {
        bank = new BankStorage();
        vendingStorage = new VendingStorage();

        bank.prefillBank();
    }

    public VendingMachineImpl(String fileName) {
        bank = new BankStorage();
        vendingStorage = new VendingStorage();

        vendingStorage.loadItems(fileName, ",");
        bank.prefillBank();
    }

    @Override
    public String showItems() {
        return vendingStorage.showItems();
    }

    @Override
    public String showMoney() {
        return bank.showMoney();
    }

    @Override
    public Item selectProduct(int UID) {
        // try and find the selected product
        Item item;
        try {
            item = vendingStorage.getItem(UID);
        } catch (SoldOutException | ItemInexistentException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return item;

    }

    @Override
    public void acceptPayment(Item item) {
        // prints info
        System.out.println("You have selected product: " + item +
                ". Please give money or cancel command.");
        System.out.println(
                "In order to enter money, enter one of the following bills or coins:");
        System.out.println(Arrays.toString(Bill.values()));
        System.out.println(Arrays.toString(Coin.values()));

        Scanner scanner = new Scanner(System.in);
        isSellingItem = true;

        while (isSellingItem) {
            String input = scanner.nextLine();

            // check for cancel command
            if (input.equalsIgnoreCase("cancel")) {
                cancelRequest(item);
                continue;
            }
            try {
                // adds specified money
                Currency currency = bank.getCurrencyFromString(input);
                bank.addMoney(currency);

                // check if there is enough money
                if (bank.getCurrentSold() >= item.getPrice()) {
                    // prints info
                    double change = bank.giveChange(item);
                    String changeStr = String.format("%1.2f", change);
                    System.out.println("Your change: " + changeStr);

                    // returns the wanted product to the client
                    returnProduct(item);
                    continue;
                }

                System.out.println(showRemainder(currency, item));

            } catch (InvalidCurrencyException e) {
                System.out.println(e.getMessage());
            } catch (NotSufficientChangeException e) {
                System.out.println(e.getMessage());
                cancelRequest(item);
            }
        }
    }

    @Override
    public void cancelRequest(Item item) {
        String roundedSoldStr = String.format("%1.2f", bank.getCurrentSold());
        System.out.println(
                "Item request canceled. Giving back " + roundedSoldStr +
                        " RON.");
        bank.restoreMoneyToClient();
        isSellingItem = false;
    }

    @Override
    public void returnProduct(Item item) {
        System.out.println(
                "You bought the following: " + item + ". Thank you!");

        isSellingItem = false;
        vendingStorage.removeItem(item);
        bank.resetProcessingState();

    }

    // gets information about the money introduced by the client and how much
    // more he needs to give
    private String showRemainder(Currency currency, Item item) {
        double value = currency.getValue();
        String roundedStr = String.format("%1.2f", getRemainder(item));
        return "You have introduced " + value + " RON. Please introduce " +
                roundedStr + " more.";
    }

    // calculates remainder
    private double getRemainder(Item item) {
        return item.getPrice() - bank.getCurrentSold();
    }
}
