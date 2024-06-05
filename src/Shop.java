package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Scanner;

import src.exceptions.CheckoutNotOpenException;
import src.exceptions.NotEnoughBudgetException;
import src.exceptions.NotEnoughGoodsException;
import src.interfaces.IShop;

public class Shop implements IShop {
    private Dictionary<Goods, Integer> shippedGoods;
    private Dictionary<Goods, Integer> soldGoods;

    private ArrayList<Checkout> checkouts;
    private ArrayList<Cashier> cashiers;
    private ArrayList<Receipt> receipts;

    private BigDecimal shippedGoodsExpenses = BigDecimal.valueOf(0);
    private BigDecimal goodsProfit = BigDecimal.valueOf(0);
    private BigDecimal netProfit = BigDecimal.valueOf(0);

    public Shop() {
        this.shippedGoods = new Hashtable<>();
        this.soldGoods = new Hashtable<>();

        this.checkouts = new ArrayList<Checkout>();
        this.cashiers = new ArrayList<Cashier>();
        this.receipts = new ArrayList<Receipt>();
    }

    public void addShippedGoods(Goods goods, int quantity) {
        BigDecimal goodsPrice = goods.shippingCost.multiply(BigDecimal.valueOf(quantity));

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
            throws NotEnoughBudgetException, NotEnoughGoodsException, CheckoutNotOpenException {
        if (!this.checkouts.contains(checkout) || !checkout.isOpen()) {
            throw new CheckoutNotOpenException("This checkout is not open or is non-existent!");
        }

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

        BigDecimal currentGoodsShippedExpenses = BigDecimal.valueOf(0);

        Receipt receipt = new Receipt(checkout.getCurrentCashier(), LocalTime.now());

        Enumeration<Goods> goods2 = purchasedGoods.keys();
        while (goods2.hasMoreElements()) {
            Goods currentGoods = goods2.nextElement();

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

            currentGoodsShippedExpenses = currentGoodsShippedExpenses
                    .add(currentGoods.shippingCost.multiply(BigDecimal.valueOf(demandedQuantity)));

            this.shippedGoods.put(currentGoods, currentQuantity - demandedQuantity);
            this.soldGoods.put(currentGoods, demandedQuantity);
            receipt.addGoods(currentGoods, demandedQuantity);
        }

        this.goodsProfit = purchasedGoodsWorth.subtract(currentGoodsShippedExpenses);

        this.receipts.add(receipt);
        // Should make this to save it into a file
        receipt.toString();
    }

    public void readReceiptFromFile(int receiptId) {
        try {
            File myObj = new File(receiptId + ".txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                System.out.println(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public int getCurrentNumberOfReceiptsIssued() {
        return this.receipts.size();
    }

    public int getSoldGoodsCount() {
        return this.soldGoods.size();
    }

    public BigDecimal getSalaryExpenses() {
        BigDecimal totalSalaryExpenses = BigDecimal.valueOf(0);

        for (int i = 0; i < this.cashiers.size(); i++) {
            BigDecimal currentCashierSalary = this.cashiers.get(i).getSalary();
            totalSalaryExpenses = totalSalaryExpenses.add(currentCashierSalary);
        }

        return totalSalaryExpenses;
    }

    public BigDecimal getShippedGoodsExpenses() {
        return this.shippedGoodsExpenses;
    }

    public BigDecimal getGoodsProfit() {
        return this.goodsProfit;
    }

    public BigDecimal getNetProfit() {
        this.netProfit = this.goodsProfit.subtract(getSalaryExpenses());

        return this.netProfit;
    }

    public Dictionary<Goods, Integer> getShippedGoods() {
        return this.shippedGoods;
    }

    public int getCheckoutsCount() {
        return this.checkouts.size();
    }

    public int getCashiersCount() {
        return this.cashiers.size();
    }

    public Dictionary<Goods, Integer> getSoldGoods() {
        return this.soldGoods;
    }
}
