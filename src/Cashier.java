package src;

import java.math.BigDecimal;

import src.interfaces.ICashier;

public class Cashier implements ICashier {
    private final long id;
    private static long numberOfInstances = 0;
    private String name;
    private BigDecimal salary = BigDecimal.valueOf(0);

    public Cashier(String _name, BigDecimal _salary) {
        numberOfInstances++;
        this.id = numberOfInstances;
        this.name = _name;
        this.salary = _salary;
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public BigDecimal getSalary() {
        return this.salary;
    }
}
