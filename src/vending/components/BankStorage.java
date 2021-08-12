package vending.components;

import vending.Item;
import vending.exceptions.InvalidCurrencyException;
import vending.exceptions.NotSufficientChangeException;
import vending.money.Bill;
import vending.money.Coin;
import vending.money.Currency;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import static java.lang.Math.abs;

public class BankStorage {
    // Contains the currently stored bills and their respective count
    EnumMap<Bill, Integer> bills;

    // Contains the currently stored coins and their respective count
    EnumMap<Coin, Integer> coins;

    // Contains the bills stored during a buy -
    // these are the ones that could be refunded in case of cancel
    //or not enough change
    EnumMap<Bill, Integer> billsInProcess;

    // Contains the coins stored during a buy
    EnumMap<Coin, Integer> coinsInProcess;

    // Contains the currently stored total of money during a buy
    double currentSold = 0;

    // No parameter constructor. Initialises maps
    public BankStorage() {
        bills = new EnumMap<>(Bill.class);
        coins = new EnumMap<>(Coin.class);
        billsInProcess = new EnumMap<>(Bill.class);
        coinsInProcess = new EnumMap<>(Coin.class);
        fillMaps();
    }

    /* Getters and setters */
    public Map<Bill, Integer> getBills() {
        return Collections.unmodifiableMap(bills);
    }

    public Map<Coin, Integer> getCoins() {
        return Collections.unmodifiableMap(coins);
    }

    public double getCurrentSold() {
        return currentSold;
    }

    public void setCurrentSold(double currentSold) {
        this.currentSold = currentSold;
    }

    // Adds 1 bill or coin to the vending machine
    public void addMoney(Currency money) {
        int count, processingCount;

        // adds bill
        if (money instanceof Bill bill) {
            if (bills.containsKey(bill)) {
                count = bills.get(bill);
                bills.put(bill, count + 1);

                processingCount = billsInProcess.get(bill);
                billsInProcess.put(bill, processingCount + 1);
            }

            // adds coin
        } else if (money instanceof Coin coin) {
            if (coins.containsKey(coin)) {
                count = coins.get(coin);
                coins.put(coin, count + 1);

                processingCount = coinsInProcess.get(coin);
                coinsInProcess.put(coin, processingCount + 1);
            }
        }
        currentSold += money.getValue();
    }

    // Adds "nr" bills or coins to the vending machine
    public void addMoneyInBulk(Currency money, int nr) {
        for (int i = 0; i < nr; i++) {
            addMoney(money);
        }
    }

    // Subtracts 1 bill or coin to the vending machine
    public void takeMoney(Currency money) {
        int count;

        // takes bill
        if (money instanceof Bill bill) {
            if (bills.containsKey(bill)) {
                count = bills.get(bill);
                if (count > 0) {
                    bills.put(bill, count - 1);
                }
            }

            // takes coin
        } else if (money instanceof Coin coin) {
            if (coins.containsKey(coin)) {
                count = coins.get(coin);
                if (count > 0) {
                    coins.put(coin, count - 1);
                }
            }
            currentSold -= money.getValue();
        }
    }

    // Gets the total amount of money stored in the vending machine at a given
    // moment in time
    public double getTotal() {
        double total = 0;

        // computes bills
        for (var entry : bills.entrySet()) {
            Bill currentBill = entry.getKey();
            int count = entry.getValue();
            total += currentBill.getValue() * count;
        }

        // computes coins
        for (var entry : coins.entrySet()) {
            Coin currentCOin = entry.getKey();
            int count = entry.getValue();
            total += currentCOin.getValue() * count;
        }

        return total;
    }

    // Transforms a string into a currency enum (Bill or Coin)
    public Currency getCurrencyFromString(String currencyString)
            throws InvalidCurrencyException {
        Currency currency;
        try {
            currency = Bill.valueOf(currencyString);
        } catch (Exception e1) {
            try {
                currency = Coin.valueOf(currencyString);
            } catch (Exception e2) {
                throw new InvalidCurrencyException("\"" + currencyString +
                        "\" is not a valid bill or coin.");
            }
        }

        return currency;
    }

    // Resets the machine after a buy phase (basically exiting the buying state)
    public void resetProcessingState() {
        resetBillsMap(billsInProcess);
        resetCoinsMap(coinsInProcess);
        currentSold = 0;
    }

    // Restores money to client (removes it from the vending machine) in case
    // of a cancel or not enough change
    public void restoreMoneyToClient() {
        // restores bills
        for (var entry : billsInProcess.entrySet()) {
            Bill key = entry.getKey();
            int count = entry.getValue();
            for (int i = 0; i < count; i++) {
                takeMoney(key);
            }
        }

        // restores coins
        for (var entry : coinsInProcess.entrySet()) {
            Coin key = entry.getKey();
            int count = entry.getValue();
            for (int i = 0; i < count; i++) {
                takeMoney(key);
            }
        }

        resetProcessingState();
    }

    // Gives the client his change, which is constructed from the minimal
    // number of total bills and coins
    // If the change can't be constructed from the currently available bills
    // and coins, the transaction is canceled and the money is refunded
    public double giveChange(Item item) throws NotSufficientChangeException {
        // initialise needed change
        double price = item.getPrice();
        double neededChange = currentSold - price;
        double currentChange = 0;
        if (neededChange < 0.01) {
            return currentChange;
        }

        //initialise temporary maps
        EnumMap<Bill, Integer> neededBills = new EnumMap<>(Bill.class);
        EnumMap<Coin, Integer> neededCoins = new EnumMap<>(Coin.class);
        resetBillsMap(neededBills);
        resetCoinsMap(neededCoins);

        // iterate through bills
        for (var entry : bills.entrySet()) {
            Bill currentBill = entry.getKey();
            double currentBillValue = currentBill.getValue();

            int billCount = entry.getValue();
            int billsNeeded =
                    (int) ((neededChange - currentChange) / currentBillValue);

            int billsConsumed = Math.min(billsNeeded, billCount);
            neededBills.put(currentBill, billsConsumed);

            currentChange += currentBillValue * billsConsumed;
            if (isEnoughChange(neededChange, currentChange)) {
                spitOutMoney(neededBills, neededCoins);
                return neededChange;
            }
        }

        //iterate through coins
        for (var entry : coins.entrySet()) {
            Coin currentCoin = entry.getKey();
            double currentCoinValue = currentCoin.getValue();

            int coinCount = entry.getValue();
            int coinsNeeded =
                    (int) ((neededChange - currentChange) / currentCoinValue);

            int coinsConsumed = Math.min(coinsNeeded, coinCount);
            neededCoins.put(currentCoin, coinsConsumed);

            currentChange += currentCoinValue * coinsConsumed;
            if (isEnoughChange(neededChange, currentChange)) {
                spitOutMoney(neededBills, neededCoins);
                return neededChange;
            }
        }

        // shows info about the insufficient change
        String neededChangeStr = String.format("%1.2f", neededChange);
        String currentChangeStr = String.format("%1.2f", currentChange);
        throw new NotSufficientChangeException(
                "No sufficient change. Change needed: " + neededChangeStr +
                        ". Maximum change acquired: " + currentChangeStr + ".");
    }

    // Checks if "currentChange" is enough for the client
    private boolean isEnoughChange(double neededChange, double currentChange) {
        return abs(neededChange - currentChange) < 0.01;
    }

    // Subtracts money (needed for change) from the bank (simulates actually
    // giving it tot the client)
    private void spitOutMoney(EnumMap<Bill, Integer> neededBills,
                              EnumMap<Coin, Integer> neededCoins) {
        // gives bills
        for (var entry : neededBills.entrySet()) {
            Bill currentBill = entry.getKey();

            int neededBillsCount = entry.getValue();
            int currentBillsCount = bills.get(currentBill);

            bills.put(currentBill, neededBillsCount > currentBillsCount ? 0 :
                    currentBillsCount - neededBillsCount);
        }

        // gives coins
        for (var entry : neededCoins.entrySet()) {
            Coin currentCoin = entry.getKey();

            int neededCoinsCount = entry.getValue();
            int currentCoinsCount = coins.get(currentCoin);

            coins.put(currentCoin, neededCoinsCount > currentCoinsCount ? 0 :
                    currentCoinsCount - neededCoinsCount);
        }
    }

    // Fills currency maps to their default 0 values
    private void fillMaps() {
        resetBillsMap(bills);
        resetBillsMap(billsInProcess);
        resetCoinsMap(coins);
        resetCoinsMap(coinsInProcess);
    }

    // Initialises a bills map to default 0 values
    private void resetBillsMap(EnumMap<Bill, Integer> billsMap) {
        for (var value : Bill.values()) {
            billsMap.put(value, 0);
        }
    }

    // Initialises a coins map to default 0 values
    private void resetCoinsMap(EnumMap<Coin, Integer> coinsMap) {
        for (var value : Coin.values()) {
            coinsMap.put(value, 0);
        }
    }

    // Returns information about the currently stored money in the vending machine
    public String showMoney() {
        String totalStr = String.format("%1.2f", getTotal());
        return "Bills: " + bills.toString() + "\nCoins: " + coins.toString() +
                "\nTotal: " + totalStr + " RON.";
    }

    // Adds some money to the bank
    public void prefillBank() {
        addMoneyInBulk(Coin.BANI1, 100);
        addMoneyInBulk(Coin.BANI5, 50);
        addMoneyInBulk(Coin.BANI10, 10);
        addMoneyInBulk(Coin.BANI50, 11);

        addMoneyInBulk(Bill.RON1, 5);
        addMoneyInBulk(Bill.RON5, 5);
        addMoneyInBulk(Bill.RON10, 1);
        addMoneyInBulk(Bill.RON50, 0);
        addMoneyInBulk(Bill.RON100, 0);
        addMoneyInBulk(Bill.RON500, 0);

        resetProcessingState();
    }
}
