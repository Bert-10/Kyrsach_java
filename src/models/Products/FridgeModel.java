package models.Products;

import db.DBWorker;
import models.Connects.Connect;
import models.Products.Product;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FridgeModel extends AbstractTableModel {
    private List<Product> data;

    public FridgeModel(List<Product> products){
        data = products;
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Product pr = data.get(rowIndex);
        switch (columnIndex){
            case 0: return pr.getName();
            case 1: return pr.getAmount();
            case 2: return pr.getUnit();
        }
        return "";
    }

    @Override
    public String getColumnName(int column) {
        switch (column){
            case 0: return "Название";
            case 1: return "Количество";
            case 2: return "Единицы измерения";
        }
        return "";
    }

    public Product getProduct(int selectedRow) {
        return data.get(selectedRow);
    }

    public void addProductToFridge(Product product)
    {
        DBWorker.addProductToFridge(product);
    }
    public void deleteProductFromFridge(int n)
    {
        DBWorker.deleteProductFromFridge(n);
     //  data.deleteProductFromFridge(n);
        fireTableDataChanged();
    }
    public void changeProductInFridge(Product product)
    {
        DBWorker.changeProductInFridge(product);
    }

    public Product findIdOfProductByName(String name)
    {
        return DBWorker.findIdOfProductByName(name);
    }

    public void deleteConnect(int recipe_id,int product_id,int switcher){DBWorker.deleteConnect(DBWorker.findIdConnect(recipe_id, product_id),switcher);}

    public ArrayList<Product> getsAvailableProductsToOneRecipe(int id){return DBWorker.getsAvailableProductsToOneRecipe(id);}

    public void addConnect(Connect connect){
        DBWorker.addConnect(connect);}

    public void changeConnect(int recipe_id,int product_id, double amount){DBWorker.changeConnect(new Connect(DBWorker.findIdConnect(recipe_id, product_id),recipe_id,product_id,amount));}
    public ArrayList<Product> getMissingProductsToRecipe(int id){return DBWorker.getMissingProductsToRecipe(id);}
}
