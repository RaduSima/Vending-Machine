package vending.components;

import vending.Item;
import vending.exceptions.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;

public class VendingStorage {
    // hash map for the available items (linked, so it remembers the order
    // items were introduced in the system)
    LinkedHashMap<Item, Integer> items;

    public VendingStorage() {
        items = new LinkedHashMap<>();
    }

    // adds an item to the items map
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
                throw new UIDAlreadyExistsException(
                        "Item ID " + item.getUID() + " already exists.");
            }
        }
        items.put(item, 1);
    }

    // adds "count" items to the items map
    public void addItemsInBulk(Item item, int count) {
        for (int i = 0; i < count; i++) {
            try {
                addItem(item);
            } catch (UIDAlreadyExistsException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    // removes an item from the items map
    public boolean removeItem(Item item) {
        int count;
        if (items.containsKey(item) && (count = items.get(item)) > 0) {
            items.put(item, count - 1);
            return true;
        }
        return false;
    }

    // gets an item from the items map, based on its UID
    public Item getItem(int UID)
            throws SoldOutException, ItemInexistentException {
        for (var currentItem : items.keySet()) {
            // if UID exists
            if (UID == currentItem.getUID()) {
                int count = items.get(currentItem);

                // if the item is still in stock
                if (count > 0) {
                    return currentItem;
                }
                throw new SoldOutException(
                        "Item with ID " + UID + " sold out.");
            }
        }

        throw new ItemInexistentException(
                "Item with ID " + UID + " does not exist.");
    }

    // shows info about all available items
    public String showItems() {
        StringBuilder itemsStr = new StringBuilder();
        int count = 0;

        for (var item : items.keySet()) {
            itemsStr.append(count++).append(". ").append(item)
                    .append(" - count: ").append(items.get(item)).append("\n");
        }

        return itemsStr.toString().equals("") ? "No items" :
                itemsStr.toString();
    }

    // loads items into the items map from a file
    public void loadItems(String fileName, String regex) {
        try (BufferedReader reader =
                     new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] params = line.trim().split(regex);
                if (params.length != 4) {
                    continue;
                }
                int UID = Integer.parseInt(params[0]);
                String name = params[1];
                double price = Double.parseDouble(params[2]);
                int count = Integer.parseInt(params[3]);
                addItemsInBulk(new Item(UID, name, price), count);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
