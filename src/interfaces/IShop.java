package src.interfaces;

import java.math.BigDecimal;
import java.util.Dictionary;

import src.Buyer;
import src.Cashier;
import src.Checkout;
import src.Goods;
import src.exceptions.CheckoutNotOpenException;
import src.exceptions.NotEnoughBudgetException;
import src.exceptions.NotEnoughGoodsException;

public interface IShop {
    public void addShippedGoods(Goods goods, int quantity);

    public void addCheckout(Checkout checkout);

    public void addCashier(Cashier cashier);

    public void removeCashier(Cashier cashier);

    public void buyGoods(Buyer buyer, Dictionary<Goods, Integer> purchasedGoods, Checkout checkout)
            throws NotEnoughBudgetException, NotEnoughGoodsException, CheckoutNotOpenException;

    public void readReceiptFromFile(int receiptId);

    public int getCurrentNumberOfReceiptsIssued();

    public int getSoldGoodsCount();

    public BigDecimal getSalaryExpenses();

    public BigDecimal getShippedGoodsExpenses();

    public BigDecimal getGoodsProfit();

    public BigDecimal getNetProfit();

    public Dictionary<Goods, Integer> getShippedGoods();

    public int getCheckoutsCount();

    public int getCashiersCount();

    public Dictionary<Goods, Integer> getSoldGoods();
}
