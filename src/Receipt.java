package src;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Dictionary;
import java.util.Hashtable;

public class Receipt {
    private final long id;
    private static long numberOfInstances = 0;

    private Cashier cashier;
    private LocalTime timeOfIssueing;
    private Dictionary<Goods, Integer> purchasedGoodsToQuantity;
    private Dictionary<Goods, BigDecimal> purchasedGoodsToPrice;
    private BigDecimal totalSum;

    public Receipt(Cashier _cashier, LocalTime _timeOfIssueing) {
        numberOfInstances++;
        this.id = numberOfInstances;
        this.cashier = _cashier;
        this.timeOfIssueing = _timeOfIssueing;

        this.purchasedGoodsToQuantity = new Hashtable<>();
        this.purchasedGoodsToPrice = new Hashtable<>();

        this.totalSum = BigDecimal.valueOf(0);
    }

    public void addGoods(Goods goods, int quantity) {
        BigDecimal goodsPrice = goods.Price().multiply(BigDecimal.valueOf(quantity));

        if (this.purchasedGoodsToQuantity.get(goods) != null) {
            int currentQuantity = this.purchasedGoodsToQuantity.get(goods);
            BigDecimal currentPrice = this.purchasedGoodsToPrice.get(goods);

            this.purchasedGoodsToQuantity.put(goods, currentQuantity + quantity);
            this.purchasedGoodsToPrice.put(goods, currentPrice.add(goodsPrice));
        } else {
            this.purchasedGoodsToQuantity.put(goods, quantity);
            this.purchasedGoodsToPrice.put(goods, goodsPrice);
        }

        updateTotalSum(goods, quantity);
    }

    private void updateTotalSum(Goods goods, int quantity) {
        this.totalSum = this.totalSum.add(goods.Price().multiply(BigDecimal.valueOf(quantity)));
    }

    public String getTimeOfIssueing() {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String formattedDate = this.timeOfIssueing.format(timeFormatter);

        return formattedDate;
    }

    public long getId() {
        return this.id;
    }

    public Cashier getCashier() {
        return this.cashier;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("Receipt #" + this.id + ":");
        builder.append("\r\n");
        builder.append("\r\n");

        for (int i = 0; i < this.purchasedGoodsToQuantity.size(); i++) {
            builder.append(this.purchasedGoodsToQuantity.keys().nextElement().getName() + " x "
                    + this.purchasedGoodsToQuantity.elements().nextElement() + "   $"
                    + this.purchasedGoodsToPrice.elements().nextElement());
            builder.append("\r\n");
        }
        builder.append("\r\n");

        builder.append("Total sum: $" + this.totalSum);

        return builder.toString();
    }
}
