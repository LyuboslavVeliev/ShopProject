package src;

public class Checkout {
    private final long id;
    private static long numberOfInstances = 0;
    private Cashier cashier;
    private boolean isOpen;

    public Checkout() {
        numberOfInstances++;
        this.id = numberOfInstances;
    }

    public void changeCashier(Cashier newCashier) {
        this.cashier = newCashier;
    }

    public Cashier getCurrentCashier() {
        return this.cashier;
    }

    public void changeOpenStatus(boolean _isOpen) {
        this.isOpen = _isOpen;
    }

    public boolean isOpen() {
        return this.isOpen;
    }

    public long getId() {
        return this.id;
    }
}
