package models.Products;

public class Product {
    protected String name;
    protected double amount;
    protected String unit;

    public Product (String name, double amount, String unit){
        this.name=name;
        this.amount=amount;
        this.unit=unit;
    }

    public String getName()
    {
        return this.name;
    }

    public String getUnit()
    {
        return this.unit;
    }

    public String getAmount()
    {
        return Double.toString(this.amount);
    }
    public void setAmount(int amount)
    {
         this.amount=amount;
    }

}

