package vending;

import java.util.Objects;

public class Item {
    private int UID;
    private String name;
    private double price;

    public Item(int UID, String name, double price) {
        this.UID = UID;
        this.name = name;
        this.price = price;
    }

    public int getUID() {
        return UID;
    }

    public void setUID(int UID) {
        this.UID = UID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item item)) return false;
        return UID == item.UID && Double.compare(item.price, price) == 0 && Objects.equals(name, item.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(UID, name, price);
    }

    @Override
    public String toString() {
        return "Item{" +
                "UID=" + UID +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
