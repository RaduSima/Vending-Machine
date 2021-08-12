package vending;

public enum Bill implements Currency {
    RON1(1), RON5(5), RON10(10), RON50(50), RON100(100), RON200(200), RON500(500);

    private final double value;

    Bill(double value) {
        this.value = value;
    }

    @Override
    public double getValue() {
        return value;
    }
}
