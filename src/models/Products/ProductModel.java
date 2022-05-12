package models.Products;

import db.DBWorker;

import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

public class ProductModel extends AbstractTableModel {
    private List<Product> data;

    public ProductModel(List<Product> products){
        data = products;
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
        Product pr = data.get(rowIndex);
        switch (columnIndex){
            case 0: return pr.getName();
            case 1: return pr.getUnit();
        }
        return "";
    }

    @Override
    public String getColumnName(int column) {
        switch (column){
            case 0: return "Название";
            case 1: return "Единицы измерения";
        }
        return "";
    }

    public Product getProduct(int selectedRow) {
        //System.out.println(data.get(selectedRow).getName());
        return data.get(selectedRow);
    }

    public int findIdOfProductByName(String name)
    {
        return DBWorker.findIdOfProductByName(name);
    }

    public boolean addProduct(Product product)
    {
        return DBWorker.addProduct(product);
    }
    public void deleteProduct(int n)
    {
        DBWorker.deleteProduct(n);
        fireTableDataChanged();
    }
    public boolean changeProduct(Product product,int switcher)
    {
        return DBWorker.changeProduct(product,switcher);
    }
}