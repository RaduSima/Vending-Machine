package vending;

import vending.exceptions.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class VendingStorage {
    HashMap<Item, Integer> items;

    public VendingStorage() {
        items = new HashMap<>();
    }

    public void addItem(Item item) throws UIDAlreadyExistsException {
        // if item already exists
        if (items.containsKey(item)) {
            int count = items.get(item);
            items.put(item, count + 1);
            return;
        }

        // if item does not already exist
        for (var currentItem : items.keySet()) {
            // check if UID already exists
            if (item.getUID() == currentItem.getUID()) {
                throw new UIDAlreadyExistsException();
            }
        }
        items.put(item, 1);
    }

    public Item getItem(int UID) throws SoldOutException, ItemInexistentException {
        for (var currentItem : items.keySet()) {
            // if UID exists
            if (UID == currentItem.getUID()) {
                int count = items.get(currentItem);

                // if the item is still in stock
                if (count > 0) {
                    items.put(currentItem, count - 1);
                    return currentItem;
                }
                throw new SoldOutException();
            }
        }

        throw new ItemInexistentException();
    }

    public String showItems() {
        StringBuilder itemsStr = new StringBuilder();
        int count = 0;

        for (var item : items.keySet()) {
            itemsStr.append(count++).append(". ").append(item).append(" - ").append(items.get(item)).append("\n");
        }

        return itemsStr.toString().equals("") ? "No items" : itemsStr.toString();
    }

    public void loadItems(String fileName, String regex) {
        try (BufferedReader reader =
                     new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] params = line.trim().split(regex);
                if (params.length != 3) {
                    continue;
                }
                int UID = Integer.parseInt(params[0]);
                String name = params[1];
                double price = Double.parseDouble(params[2]);

                addItem(new Item(UID, name, price));
            }
        } catch (IOException | UIDAlreadyExistsException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
