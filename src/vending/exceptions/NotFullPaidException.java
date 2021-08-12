package vending.exceptions;

public class NotFullPaidException extends Exception {
    public NotFullPaidException(String message) {
        super(message);
    }
}
