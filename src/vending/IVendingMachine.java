package vending;

public interface IVendingMachine {
    String showItems();
    void selectProduct(int UID);
    void acceptMoney();
    void cancelRequest();
    void returnProducts();
}
