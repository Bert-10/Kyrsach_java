package models.Pecipes;

public class Recipe {
    protected  int id;
    protected String name;
    protected String favorite;

    public Recipe (String name, String favorite){
        this.name=name;
        this.favorite=favorite;
    }
    public Recipe (int id,String name, String favorite){
        this.id=id;
        this.name=name;
        this.favorite=favorite;
    }
    public String getName()
    {
        return this.name;
    }
    public String getId(){return Integer.toString(this.id);}
    public String getFavorite()
    {
        return this.favorite;
    }
    public void setFavorite(String favorite)
    {
        this.favorite=favorite;
    }
}
