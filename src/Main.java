package src;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import src.enums.Category;

public class Main {
    public static void main(String[] args) {
        Goods apple = new Goods("apple", BigDecimal.valueOf(2.3), Category.Edible, LocalDate.now().plusDays(5));
        Cashier cashier = new Cashier("Miro", BigDecimal.valueOf(2000));
        Receipt receipt = new Receipt(cashier, LocalTime.now());
        receipt.addGoods(apple, 2);

        System.out.println(receipt.toString());
    }
}