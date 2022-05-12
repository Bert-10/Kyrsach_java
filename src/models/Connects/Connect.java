package models.Connects;

public class Connect {
    protected int id;
    protected int RecipeId;
    protected int ProductId;
    protected double amount;

    public Connect (int RecipeId,int ProductId,double amount){
        this.RecipeId=RecipeId;
        this.ProductId=ProductId;
        this.amount=amount;
    }
    public Connect (int id,int RecipeId,int ProductId,double amount){
        this.id=id;
        this.RecipeId=RecipeId;
        this.ProductId=ProductId;
        this.amount=amount;
    }
    public  int getId () {return this.id;}

    public int getRecipeId()
    {
        return this.RecipeId;
    }

    public int getProductId()
    {
        return this.ProductId;
    }

    public double getAmount(){return this.amount;}
}
