package models.Products;

import models.Products.Product;

import javax.swing.table.AbstractTableModel;
import java.util.List;

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

}
