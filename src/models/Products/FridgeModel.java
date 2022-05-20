package models.Products;

import db.DBWorker;
import models.Connects.Connect;
import models.Products.Product;

import javax.swing.*;
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
           // case 1: return pr.getAmount();
            case 1: return checkIntOrDouble(pr.getAmount());
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

    public String checkIntOrDouble(String amount)
    {
        String str=amount;
        double d=Double.parseDouble(amount);
        int i=(int)d;
        if(d%i==0)
        {
            str=Integer.toString(i);
        }
        return str;
    }

    public Product getProduct(int selectedRow) {
        return data.get(selectedRow);
    }

    public void addProductToFridge(int id, double amount)
    {
        DBWorker.addProductToFridge(new Product(id,amount));
    }
    public void deleteProductFromFridge(int n)
    {
        DBWorker.deleteProductFromFridge(n);
     //  data.deleteProductFromFridge(n);
        fireTableDataChanged();
    }
    public void changeProductInFridge(int id,double amount)
    {
        DBWorker.changeProductInFridge(new Product(id, amount));
    }

    public Product findIdOfProductByName(String name)
    {
        return DBWorker.findIdOfProductByName(name);
    }

    public void deleteConnect(int recipe_id,int product_id,int switcher){DBWorker.deleteConnect(DBWorker.findIdConnect(recipe_id, product_id),switcher);}

    public String getsAvailableProductsToOneRecipe(int id)
    {
        String str="";
        ArrayList<Product> listOfProducts=DBWorker.getsAvailableProductsToOneRecipe(id);
        if(listOfProducts.size()==0)
        {
            return  str;
        }
        else
        {
            for(int i=0;i< listOfProducts.size();i++)
            {
                str=str+listOfProducts.get(i).getName()+",";
            }
            str=str.substring(0,str.length()-1);
        }

        return str;
    }

    public void addConnect(int recipe_id,int product_id,double amount){
        DBWorker.addConnect(new Connect(recipe_id,product_id,amount));}

    public void changeConnect(int recipe_id,int product_id, double amount){DBWorker.changeConnect(new Connect(DBWorker.findIdConnect(recipe_id, product_id),recipe_id,product_id,amount));}
    public ArrayList<Product> getMissingProductsToRecipe(int id){return DBWorker.getMissingProductsToRecipe(id);}
    public String productsNotInTheFridge()
    {
        String str="";
        ArrayList<Product> listOfProducts=DBWorker.productsNotInTheFridge();
        if(listOfProducts.size()==0)
        {
           return  str;
        }
        else
        {
            for(int i=0;i< listOfProducts.size();i++)
            {
                str=str+listOfProducts.get(i).getName()+",";
            }
            str=str.substring(0,str.length()-1);
        }
        return str;
    }

}
