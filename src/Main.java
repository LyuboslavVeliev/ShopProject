package src;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Dictionary;
import java.util.Hashtable;

import src.enums.Category;

public class Main {
    public static void main(String[] args) {
        Goods apple = new Goods("apple", BigDecimal.valueOf(2.3), Category.Edible, LocalDate.now().plusDays(5));
        Goods banana = new Goods("banana", BigDecimal.valueOf(2.5), Category.Edible, LocalDate.now().plusDays(3));
        Cashier cashier = new Cashier("Miro", BigDecimal.valueOf(2000));
        Checkout checkout1 = new Checkout();
        checkout1.changeCashier(cashier);
        checkout1.changeOpenStatus(true);

        Buyer buyer = new Buyer(BigDecimal.valueOf(100));
        Dictionary<Goods, Integer> buyersGoods = new Hashtable<>();
        buyersGoods.put(apple, 1);
        buyersGoods.put(banana, 1);

        Shop shop = new Shop();
        shop.addShippedGoods(apple, 3);
        shop.addShippedGoods(banana, 2);
        shop.addCashier(cashier);
        shop.addCheckout(checkout1);

        try {
            shop.buyGoods(buyer, buyersGoods, checkout1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Goods profit: " + shop.getGoodsProfit());
        System.out.println("Net profit: " + shop.getNetProfit());
        System.out.println("Sold items: " + shop.getSoldGoodsCount());

    }
}