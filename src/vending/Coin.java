package vending;

public enum Coin implements Currency {
    BANI1(0.01), BANI5(0.05), BANI10(0.1), BANI50(0.5);

    private final double value;

    Coin(double value) {
        this.value = value;
    }

    @Override
    public double getValue() {
        return value;
    }
}
