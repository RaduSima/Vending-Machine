package vending.components;

import vending.Item;

public interface IVendingMachine {
    // Shows available items
    String showItems();

    // Shows currently stored bills, coins and total money
    String showMoney();

    // Selects a product
    Item selectProduct(int UID);

    // Enter payment processing mode
    void acceptPayment(Item item);

    // Cancel an item request and exists payment processing mode
    void cancelRequest(Item item);

    // Returns an item to the client
    void returnProduct(Item item);
}
