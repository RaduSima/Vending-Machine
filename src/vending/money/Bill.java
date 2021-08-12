package vending.money;

public enum Bill implements Currency {
    RON500(500), RON200(200), RON100(100), RON50(50),
    RON10(10), RON5(5), RON1(1);

    private final double value;

    Bill(double value) {
        this.value = value;
    }

    @Override
    public double getValue() {
        return value;
    }
}

