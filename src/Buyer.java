package src;

import java.math.BigDecimal;

public class Buyer {
    private BigDecimal budget;

    public Buyer(BigDecimal _budget) {
        this.budget = _budget;
    }

    public BigDecimal getBudget() {
        return this.budget;
    }
}
