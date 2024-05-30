package src;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

import src.exceptions.NotEnoughBudgetException;
import src.exceptions.NotEnoughGoodsException;

public class Shop {
    private Dictionary<Goods, Integer> shippedGoods;
    private Dictionary<Goods, Integer> soldGoods;

    private ArrayList<Checkout> checkouts;
    private ArrayList<Cashier> cashiers;
    private ArrayList<Receipt> receipts;

    private BigDecimal shippedGoodsExpenses;
    private BigDecimal goodsProfit;
    private BigDecimal netProfit;

    public Shop() {
        this.shippedGoods = new Hashtable<>();
        this.soldGoods = new Hashtable<>();

        this.checkouts = new ArrayList<Checkout>();
        this.cashiers = new ArrayList<Cashier>();
        this.receipts = new ArrayList<Receipt>();
    }

    public void addShippedGoods(Goods goods, int quantity) {
        BigDecimal goodsPrice = goods.Price().multiply(BigDecimal.valueOf(quantity));

        if (this.shippedGoods.get(goods) != null) {
            int currentQuantity = this.shippedGoods.get(goods);

            this.shippedGoods.put(goods, currentQuantity + quantity);
        } else {
            this.shippedGoods.put(goods, quantity);
        }

        this.shippedGoodsExpenses = this.shippedGoodsExpenses.add(goodsPrice);
    }

    public void addCheckout(Checkout checkout) {
        if (!this.checkouts.contains(checkout)) {
            this.checkouts.add(checkout);
        }
    }

    // Hire a cashier
    public void addCashier(Cashier cashier) {
        if (!this.cashiers.contains(cashier)) {
            this.cashiers.add(cashier);
        }
    }

    // Dismiss a cashier
    public void removeCashier(Cashier cashier) {
        if (this.cashiers.contains(cashier)) {
            this.cashiers.remove(cashier);
        }
    }

    // A buyer buys goods
    public void buyGoods(Buyer buyer, Dictionary<Goods, Integer> purchasedGoods, Checkout checkout)
            throws NotEnoughBudgetException, NotEnoughGoodsException {
        BigDecimal purchasedGoodsWorth = BigDecimal.valueOf(0);

        Enumeration<Goods> goods = purchasedGoods.keys();
        while (goods.hasMoreElements()) {
            Goods currentGoods = goods.nextElement();

            BigDecimal currentGoodsPrice = currentGoods.Price()
                    .multiply(BigDecimal.valueOf(purchasedGoods.get(currentGoods)));

            purchasedGoodsWorth = purchasedGoodsWorth.add(currentGoodsPrice);
        }

        // Does the buyer have enough budget to buy the goods?
        int result = buyer.getBudget().compareTo(purchasedGoodsWorth);

        if (result < 0) {
            // Throw exception
            throw new NotEnoughBudgetException("Buyer doesn't have enough budget!");
        }

        while (goods.hasMoreElements()) {
            Goods currentGoods = goods.nextElement();

            if (currentGoods.Price() == BigDecimal.valueOf(0)) {
                continue;
            }

            if (this.shippedGoods.get(currentGoods) == null) {
                throw new NotEnoughGoodsException(
                        "Not enough " + currentGoods.getName() + ". Missing " + purchasedGoods.get(currentGoods)
                                + " items.");
            }

            int currentQuantity = this.shippedGoods.get(currentGoods);
            int demandedQuantity = purchasedGoods.get(currentGoods);

            if (currentQuantity < demandedQuantity) {
                int missingItems = demandedQuantity - currentQuantity;

                throw new NotEnoughGoodsException(
                        "Not enough " + currentGoods.getName() + ". Missing " + missingItems + " items.");
            }

            this.shippedGoods.put(currentGoods, currentQuantity - demandedQuantity);
            this.soldGoods.put(currentGoods, demandedQuantity);
        }

        Receipt receipt = new Receipt(checkout.getCurrentCashier(), LocalTime.now());
        this.receipts.add(receipt);
        // Should make this to save it into a file
        receipt.toString();
    }
}
