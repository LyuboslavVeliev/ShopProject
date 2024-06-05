package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Dictionary;
import java.util.Hashtable;

import org.junit.Assert;
import org.junit.Test;

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
}
