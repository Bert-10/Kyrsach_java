package models.Pecipes;

import db.DBWorker;
import models.Products.Product;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class RecipeModel extends AbstractTableModel  {
    private List<Recipe> data;

    public RecipeModel(List<Recipe> recipe){
        data = recipe;
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Recipe pr = data.get(rowIndex);
        switch (columnIndex){
            case 0: return pr.getName();
            case 1: return pr.getFavorite();
        }
        return "";
    }

    @Override
    public String getColumnName(int column) {
        switch (column){
            case 0: return "Название";
            case 1: return "Предпочтительный";
        }
        return "";
    }

    public Recipe getRecipe(int selectedRow) {
        return data.get(selectedRow);
    }

    public boolean addRecipe(Recipe recipe)
    {
        return DBWorker.addRecipe(recipe);
    }
    public void deleteRecipe(int n)
    {
        DBWorker.deleteRecipe(n);
        fireTableDataChanged();
    }
    public boolean changeRecipe(Recipe recipe,int switcher)
    {
       return DBWorker.changeRecipe(recipe, switcher);
    }

    public ArrayList<Product> getProductsToOneRecipe(int id){return DBWorker.getProductsToOneRecipe(id);}
    public ArrayList<Recipe> getUnavailableRecipes(){return DBWorker.getUnavailableRecipes();}

    public void realiseRecipe(int id){DBWorker.realiseRecipe(id);}
}
