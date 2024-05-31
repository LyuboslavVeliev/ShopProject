package src;

import java.math.BigDecimal;

public class Buyer {
    private BigDecimal budget = BigDecimal.valueOf(0);

    public Buyer(BigDecimal _budget) {
        this.budget = _budget;
    }

    public BigDecimal getBudget() {
        return this.budget;
    }
}
