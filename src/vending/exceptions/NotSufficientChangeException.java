package vending.exceptions;

public class NotSufficientChangeException extends Exception {
    public NotSufficientChangeException(String message) {
        super(message);
    }
}
