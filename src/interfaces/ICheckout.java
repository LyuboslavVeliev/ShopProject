package src.interfaces;

import src.Cashier;

public interface ICheckout {
    public void changeCashier(Cashier newCashier);

    public Cashier getCurrentCashier();

    public void changeOpenStatus(boolean _isOpen);

    public boolean isOpen();

    public long getId();
}
