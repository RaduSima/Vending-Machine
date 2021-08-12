package vending.money;

public enum Coin implements Currency {
    BANI50(0.5), BANI10(0.1), BANI5(0.05), BANI1(0.01);

    private final double value;

    Coin(double value) {
        this.value = value;
    }

    @Override
    public double getValue() {
        return value;
    }
}
