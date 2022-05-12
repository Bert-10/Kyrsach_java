package models.Products;

public class Product {
    protected int id;
    protected String name;
    protected double amount;
    protected String unit;

    public Product (String name,double amount, String unit){
        this.name=name;
        this.amount=amount;
        this.unit=unit;
    }
    public Product (int id,String name, String unit){
        this.id=id;
        this.name=name;
        this.unit=unit;
    }
    public Product (String name, String unit){
        this.name=name;
        this.unit=unit;
    }
    public Product (int id,double amount){
        this.id=id;
        this.amount=amount;
    }
    public Product (int id,String name, double amount, String unit){
        this.id=id;
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
    public String getId(){return Integer.toString(this.id);}
    public void setAmount(int amount)
    {
         this.amount=amount;
    }

}

