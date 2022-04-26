package db;

import model.Product;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

public class DBWorker {
    public static String PATH_TO_DB_FILE="smartFridge.db";
    public static String URL="jdbc:sqlite:"+PATH_TO_DB_FILE;
    public static Connection connection;
    public static void initDB()
    {
        try {
          connection = DriverManager.getConnection(URL);
     //    /*
          if(connection!=null)
          {
          //    DatabaseMetaData metaData = connection.getMetaData();
          //    System.out.print(metaData.getDriverName());
              createDB();
          }
       //   */

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static  void closeDB()
    {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static  void createDB()
    {
        try {
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE if not exists 'fridge' ('name' STRING PRIMARY KEY, 'amount' DOUBLE NOT NULL, 'unit' text);");
            statement.execute("CREATE TABLE if not exists 'recipes' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'name' text);");
            statement.execute("CREATE TABLE if not exists 'products' ('name' STRING PRIMARY KEY, 'unit' text);");
            statement.execute("CREATE TABLE if not exists 'connect' ('connect_id' INTEGER PRIMARY KEY AUTOINCREMENT,'recipe_id' INTEGER , 'product_name' STRING ,'amount' DOUBLE NOT NULL, FOREIGN KEY (recipe_id) REFERENCES recipes (id), FOREIGN KEY (product_name) REFERENCES products (name));");
          //  statmt.execute("CREATE TABLE if not exists 'students' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'lastname' text,'name' text,'email' text, 'group_id' INTEGER NOT NULL, FOREIGN KEY (group_id) REFERENCES groups (id));");
            System.out.println("Таблицы созданы");
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addProductToFridge(Product product)
    {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO fridge('name','amount','unit')"+"VALUES(?,?,?)");
            statement.setObject(1,product.getName());
            statement.setObject(2,product.getAmount());
            statement.setObject(3,product.getUnit());
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addToProducts(Product product)
    {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO products('name','unit')"+"VALUES(?,?)");
            statement.setObject(1,product.getName());
            statement.setObject(2,product.getUnit());
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Product> getAllFridge()
    {
        ArrayList<Product> list = new ArrayList<Product>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM fridge");
            while(resultSet.next())
            {
              //  System.out.println(resultSet.getString(1)+" "+resultSet.getString(2)+" "+resultSet.getString(3));
                list.add(new Product(resultSet.getString(1),Double.parseDouble(resultSet.getString(2)),resultSet.getString(3)));
            }
            resultSet.close();
            statement.close();

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return list;
    }

}
