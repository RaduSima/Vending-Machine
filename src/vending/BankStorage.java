package vending;

import java.util.Collections;
import java.util.EnumMap;

public class BankStorage {
    EnumMap<Bill, Integer> bills;
    EnumMap<Coin, Integer> coins;

    public BankStorage() {
        bills = new EnumMap<>(Bill.class);
        coins = new EnumMap<>(Coin.class);
        fillMaps();
    }

    public void addMoney(Currency money) {
        modifyMoney(money, '+');
    }

    public void takeMoney(Currency money) {
        modifyMoney(money, '-');
    }

    public EnumMap<Bill, Integer> getBills() {
        return (EnumMap<Bill, Integer>) Collections.unmodifiableMap(bills);
    }

    public EnumMap<Coin, Integer> getCoins() {
        return (EnumMap<Coin, Integer>) Collections.unmodifiableMap(coins);
    }

    private void modifyMoney(Currency money, char operation) {
        int count;
        if (operation == '+') {
            if (money instanceof Bill bill) {
                if (bills.containsKey(bill)) {
                    count = bills.get(bill);
                    bills.put(bill, count + 1);
                }
            } else if (money instanceof Coin coin) {
                if (coins.containsKey(coin)) {
                    count = coins.get(coin);
                    coins.put(coin, count + 1);
                }
            }
        } else if (operation == '-') {
            if (money instanceof Bill bill) {
                if (bills.containsKey(bill)) {
                    count = bills.get(bill);
                    if (count > 0) {
                        bills.put(bill, count - 1);
                    }
                }
            } else if (money instanceof Coin coin) {
                if (coins.containsKey(coin)) {
                    count = coins.get(coin);
                    if (count > 0) {
                        coins.put(coin, count - 1);
                    }
                }
            }
        }
    }

    private void fillMaps() {
        for (var value : Bill.values()) {
            bills.put(value, 0);
        }

        for (var value : Coin.values()) {
            coins.put(value, 0);
        }
    }
}
