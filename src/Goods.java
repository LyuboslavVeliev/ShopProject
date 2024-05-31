package src;

import java.math.BigDecimal;
import java.time.LocalDate;

import src.enums.Category;

public class Goods {
    private final long id;
    private static long numberOfInstances = 0;
    private String name;

    public BigDecimal shippingCost = BigDecimal.valueOf(0);
    public Category category;
    public LocalDate expirationDate;

    // Hardcoded for now
    public static final int markupEdiblePercentage = 20; // 20%
    public static final int markupInediblePercentage = 30; // 30%
    public static final int daysUntilReduction = 2; // 2 days
    public static final int reductionPercentage = 10; // 10%

    public Goods(String _name, BigDecimal _shippingCost, Category _category, LocalDate _expirationDate) {
        numberOfInstances++;
        this.id = numberOfInstances;
        this.name = _name;
        this.shippingCost = _shippingCost;
        this.category = _category;
        this.expirationDate = _expirationDate;
    }

    public BigDecimal Price() {
        if (LocalDate.now().isAfter(this.expirationDate)) {
            return BigDecimal.valueOf(0);
        }

        BigDecimal finalPrice = this.shippingCost;
        if (this.category == Category.Edible) {
            finalPrice = finalPrice.add(finalPrice.multiply(BigDecimal.valueOf(markupEdiblePercentage / 100.0)));

            if (LocalDate.now().isAfter(this.expirationDate.minusDays(daysUntilReduction))) {
                finalPrice = finalPrice.subtract(finalPrice.multiply(BigDecimal.valueOf(reductionPercentage / 100.0)));
            }
        } else {
            finalPrice = finalPrice.add(finalPrice.multiply(BigDecimal.valueOf(markupInediblePercentage / 100.0)));
        }

        return finalPrice;
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
}