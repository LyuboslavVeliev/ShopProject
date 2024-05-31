package src.exceptions;

public class CheckoutNotOpenException extends Exception {
    public CheckoutNotOpenException(String msg) {
        super(msg);
    }
}
