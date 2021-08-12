package vending;

public class VendingMachineImpl implements IVendingMachine {
    BankStorage bank;
    VendingStorage vendingStorage;

    public VendingMachineImpl() {
        bank = new BankStorage();
        vendingStorage = new VendingStorage();
    }

    public VendingMachineImpl(String fileName) {
        bank = new BankStorage();
        vendingStorage = new VendingStorage();

        vendingStorage.loadItems(fileName, ",");
    }

    public BankStorage getBank() {
        return bank;
    }

    public void setBank(BankStorage bank) {
        this.bank = bank;
    }

    public VendingStorage getVendingStorage() {
        return vendingStorage;
    }

    public void setVendingStorage(VendingStorage vendingStorage) {
        this.vendingStorage = vendingStorage;
    }

    @Override
    public String showItems() {
        return vendingStorage.showItems();
    }

    @Override
    public void selectProduct(int UID) {

    }

    @Override
    public void acceptMoney() {

    }

    @Override
    public void cancelRequest() {

    }

    @Override
    public void returnProducts() {

    }
}
