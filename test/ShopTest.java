package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Dictionary;
import java.util.Hashtable;

import org.junit.Assert;
import org.junit.Test;

import src.Buyer;
import src.Cashier;
import src.Checkout;
import src.Goods;
import src.Shop;
import src.enums.Category;

public class ShopTest {
    private Shop shop = new Shop();
    private Dictionary<Goods, Integer> shippedGoods = new Hashtable<>();

    @Test
    public void TestAddsShippedGoods() {
        Goods apple = new Goods("apple", BigDecimal.valueOf(2.3), Category.Edible, LocalDate.now().plusDays(5));
        shippedGoods.put(apple, 1);

        shop.addShippedGoods(apple, 1);
        Assert.assertEquals(shippedGoods, shop.getShippedGoods());
    }

    @Test
    public void TestIncreasesShippedGoodsExpensiveWhenAddingGoods() {
        Goods apple = new Goods("apple", BigDecimal.valueOf(2.3), Category.Edible, LocalDate.now().plusDays(5));
        shippedGoods.put(apple, 1);

        shop.addShippedGoods(apple, 1);
        BigDecimal shippedExpenses = shop.getShippedGoodsExpenses();
        Assert.assertEquals(BigDecimal.valueOf(2.3), shippedExpenses);
    }

    @Test
    public void TestIncreasesQuantityOnAddsGoods() {
        Goods apple = new Goods("apple", BigDecimal.valueOf(2.3), Category.Edible, LocalDate.now().plusDays(5));

        shop.addShippedGoods(apple, 1);
        shop.addShippedGoods(apple, 1);

        shippedGoods = shop.getShippedGoods();

        Assert.assertEquals((int) shippedGoods.get(apple), 2);
    }

    @Test
    public void TestAddsCheckout() {
        Checkout checkout = new Checkout();

        shop.addCheckout(checkout);

        Assert.assertEquals(shop.getCheckoutsCount(), 1);
    }

    @Test
    public void TestAddsExistantCheckout() {
        Checkout checkout = new Checkout();

        shop.addCheckout(checkout);
        shop.addCheckout(checkout);

        Assert.assertEquals(shop.getCheckoutsCount(), 1);
    }

    @Test
    public void TestAddsMultipleCheckouts() {
        Checkout checkout = new Checkout();
        Checkout checkout2 = new Checkout();

        shop.addCheckout(checkout);
        shop.addCheckout(checkout2);

        Assert.assertEquals(shop.getCheckoutsCount(), 2);
    }

    @Test
    public void TestAddsCashier() {
        Cashier cashier = new Cashier("Jack", BigDecimal.valueOf(2000));

        shop.addCashier(cashier);

        Assert.assertEquals(shop.getCashiersCount(), 1);
    }

    @Test
    public void TestAddsExistantCashier() {
        Cashier cashier = new Cashier("Jack", BigDecimal.valueOf(2000));

        shop.addCashier(cashier);
        shop.addCashier(cashier);

        Assert.assertEquals(shop.getCashiersCount(), 1);
    }

    @Test
    public void TestAddsMultipleCashiers() {
        Cashier cashier = new Cashier("Jack", BigDecimal.valueOf(2000));
        Cashier cashier2 = new Cashier("Nick", BigDecimal.valueOf(1500));

        shop.addCashier(cashier);
        shop.addCashier(cashier2);

        Assert.assertEquals(shop.getCashiersCount(), 2);
    }

    @Test
    public void TestRemovesCashier() {
        Cashier cashier = new Cashier("Jack", BigDecimal.valueOf(2000));

        shop.addCashier(cashier);
        Assert.assertEquals(shop.getCashiersCount(), 1);

        shop.removeCashier(cashier);

        Assert.assertEquals(shop.getCashiersCount(), 0);
    }

    @Test
    public void TestBuyGoodsRemovesFromShippedList() {
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

        shop.addShippedGoods(apple, 3);
        shop.addShippedGoods(banana, 2);
        shop.addCashier(cashier);
        shop.addCheckout(checkout1);

        // Left shipped goods
        shippedGoods.put(apple, 2);
        shippedGoods.put(banana, 1);

        try {
            shop.buyGoods(buyer, buyersGoods, checkout1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        Assert.assertEquals(shippedGoods, shop.getShippedGoods());
    }

    @Test
    public void TestBuyGoodsAddsToBoughtList() {
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

        shop.addShippedGoods(apple, 3);
        shop.addShippedGoods(banana, 2);
        shop.addCashier(cashier);
        shop.addCheckout(checkout1);

        try {
            shop.buyGoods(buyer, buyersGoods, checkout1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        Assert.assertEquals(buyersGoods, shop.getSoldGoods());
    }

    @Test
    public void TestBuyGoodsThrowsWhenCheckoutClosed() {
        Goods apple = new Goods("apple", BigDecimal.valueOf(2.3), Category.Edible, LocalDate.now().plusDays(5));
        Goods banana = new Goods("banana", BigDecimal.valueOf(2.5), Category.Edible, LocalDate.now().plusDays(3));
        Cashier cashier = new Cashier("Miro", BigDecimal.valueOf(2000));
        Checkout checkout1 = new Checkout();
        checkout1.changeCashier(cashier);

        Buyer buyer = new Buyer(BigDecimal.valueOf(100));
        Dictionary<Goods, Integer> buyersGoods = new Hashtable<>();
        buyersGoods.put(apple, 1);
        buyersGoods.put(banana, 1);

        shop.addShippedGoods(apple, 3);
        shop.addShippedGoods(banana, 2);
        shop.addCashier(cashier);
        shop.addCheckout(checkout1);

        boolean thrown = false;
        try {
            shop.buyGoods(buyer, buyersGoods, checkout1);
        } catch (Exception e) {
            thrown = true;
            Assert.assertEquals(e.getMessage(), "This checkout is not open or is non-existent!");
        }

        Assert.assertTrue(thrown);
    }

    @Test
    public void TestBuyGoodsThrowsWhenCheckoutNotAdded() {
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

        shop.addShippedGoods(apple, 3);
        shop.addShippedGoods(banana, 2);
        shop.addCashier(cashier);

        boolean thrown = false;
        try {
            shop.buyGoods(buyer, buyersGoods, checkout1);
        } catch (Exception e) {
            thrown = true;
            Assert.assertEquals(e.getMessage(), "This checkout is not open or is non-existent!");
        }

        Assert.assertTrue(thrown);
    }

    @Test
    public void TestBuyGoodsThrowsWhenBuyerNotEnoughtBudget() {
        Goods apple = new Goods("apple", BigDecimal.valueOf(2.3), Category.Edible, LocalDate.now().plusDays(5));
        Goods banana = new Goods("banana", BigDecimal.valueOf(2.5), Category.Edible, LocalDate.now().plusDays(3));
        Cashier cashier = new Cashier("Miro", BigDecimal.valueOf(2000));
        Checkout checkout1 = new Checkout();
        checkout1.changeCashier(cashier);
        checkout1.changeOpenStatus(true);

        Buyer buyer = new Buyer(BigDecimal.valueOf(2));
        Dictionary<Goods, Integer> buyersGoods = new Hashtable<>();
        buyersGoods.put(apple, 1);
        buyersGoods.put(banana, 1);

        shop.addShippedGoods(apple, 3);
        shop.addShippedGoods(banana, 2);
        shop.addCashier(cashier);
        shop.addCheckout(checkout1);

        boolean thrown = false;
        try {
            shop.buyGoods(buyer, buyersGoods, checkout1);
        } catch (Exception e) {
            thrown = true;
            Assert.assertEquals(e.getMessage(), "Buyer doesn't have enough budget!");
        }

        Assert.assertTrue(thrown);
    }

    @Test
    public void TestBuyGoodsExcludesExpiredProducts() {
        // This apple is expired
        Goods apple = new Goods("apple", BigDecimal.valueOf(2.3), Category.Edible, LocalDate.now().minusDays(5));
        Goods banana = new Goods("banana", BigDecimal.valueOf(2.5), Category.Edible, LocalDate.now().plusDays(3));
        Cashier cashier = new Cashier("Miro", BigDecimal.valueOf(2000));
        Checkout checkout1 = new Checkout();
        checkout1.changeCashier(cashier);
        checkout1.changeOpenStatus(true);

        Buyer buyer = new Buyer(BigDecimal.valueOf(2000));
        Dictionary<Goods, Integer> buyersGoods = new Hashtable<>();
        buyersGoods.put(apple, 1);
        buyersGoods.put(banana, 1);

        shop.addShippedGoods(apple, 3);
        shop.addShippedGoods(banana, 2);
        shop.addCashier(cashier);
        shop.addCheckout(checkout1);

        // These products should be added to the buyers list
        Dictionary<Goods, Integer> boughtGoods = new Hashtable<>();
        boughtGoods.put(banana, 1);

        try {
            shop.buyGoods(buyer, buyersGoods, checkout1);
        } catch (Exception e) {
        }

        Assert.assertEquals(boughtGoods, shop.getSoldGoods());
    }

    @Test
    public void TestBuyGoodsThrowsNotEnoughGoods() {
        // This apple is expired
        Goods apple = new Goods("apple", BigDecimal.valueOf(2.3), Category.Edible, LocalDate.now().plusDays(5));
        Goods banana = new Goods("banana", BigDecimal.valueOf(2.5), Category.Edible, LocalDate.now().plusDays(3));
        Cashier cashier = new Cashier("Miro", BigDecimal.valueOf(2000));
        Checkout checkout1 = new Checkout();
        checkout1.changeCashier(cashier);
        checkout1.changeOpenStatus(true);

        Buyer buyer = new Buyer(BigDecimal.valueOf(2000));
        Dictionary<Goods, Integer> buyersGoods = new Hashtable<>();
        // Buyer wants to buy more than in storage
        buyersGoods.put(apple, 4);
        buyersGoods.put(banana, 1);

        shop.addShippedGoods(apple, 3);
        shop.addShippedGoods(banana, 2);
        shop.addCashier(cashier);
        shop.addCheckout(checkout1);

        boolean thrown = false;
        try {
            shop.buyGoods(buyer, buyersGoods, checkout1);
        } catch (Exception e) {
            thrown = true;
            Assert.assertEquals(e.getMessage(), "Not enough apple. Missing 1 items.");
        }

        Assert.assertTrue(thrown);
    }
}
