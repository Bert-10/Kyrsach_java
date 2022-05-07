package db;

import models.Connects.Connect;
import models.Pecipes.Recipe;
import models.Products.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.*;
import java.util.concurrent.SynchronousQueue;

public class DBWorker {
    public static String PATH_TO_DB_FILE="smartFridge.db";
    public static String URL="jdbc:sqlite:"+PATH_TO_DB_FILE;
    public static Connection connection;
    public static void initDB()
    {
        try {
          connection = DriverManager.getConnection(URL);
          if(connection!=null)
          {
              createDB();
          }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        /*
        addProduct(new Product("фарш","кг"));
        addProduct(new Product("молоко","л"));
        addProduct(new Product("яйцо","шт"));
        addProduct(new Product("сыр","кг"));
        addProduct(new Product("картофель","кг"));

        addRecipe(new Recipe("яичница","нет"));
        addRecipe(new Recipe("фарш с картошкой","нет"));

        addConnection(new Connect(1,3,4));
        addConnection(new Connect(2,1,1.3));
        addConnection(new Connect(2,5,2));

        addProductToFridge(new Product(1,"фарш",1,"кг"));
        addProductToFridge(new Product(5,"картофель",4,"кг"));
        addProductToFridge(new Product(3,"яйцо",10,"шт"));
        addProductToFridge(new Product(4,"сыр",2,"кг"));
   //        */;
        //getAllProducts();

       // addProductToFridge(new Product(4,"сыр",2,"кг"));
     //   deleteProductFromFridge(4);
      //  getAllConnection();
      //  System.out.println("");
       // availableRecipes ();
     //   changeRecipe(new Recipe(2,"фарш с картошкой","да"));
    //   getProductsToOneRecipe(2);
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
          //  statement.execute("CREATE TABLE if not exists 'fridge' ('id' INTEGER PRIMARY KEY AUTOINCREMENT,'name' VARCHAR(30), 'amount' DOUBLE NOT NULL, 'unit' VARCHAR(5));");
            statement.execute("CREATE TABLE if not exists 'recipes' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'name' VARCHAR(30),'favorite' VARCHAR(5));");
            statement.execute("CREATE TABLE if not exists 'products' ('id' INTEGER PRIMARY KEY AUTOINCREMENT,'name' VARCHAR(30) UNIQUE, 'unit' VARCHAR(5));");
            statement.execute("CREATE TABLE if not exists 'fridge' ('id' INTEGER PRIMARY KEY, 'amount' DOUBLE NOT NULL, FOREIGN KEY (id) REFERENCES products (id));");
            statement.execute("CREATE TABLE if not exists 'connect' ('connect_id' INTEGER PRIMARY KEY AUTOINCREMENT,'recipe_id' INTEGER , 'product_id' INTEGER ,'amount' DOUBLE NOT NULL, FOREIGN KEY (recipe_id) REFERENCES recipes (id), FOREIGN KEY (product_id) REFERENCES products (id));");
          //  statmt.execute("CREATE TABLE if not exists 'students' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'lastname' text,'name' text,'email' text, 'group_id' INTEGER NOT NULL, FOREIGN KEY (group_id) REFERENCES groups (id));");
            System.out.println("Таблицы созданы");
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addConnection(Connect connect)
    {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO connect('recipe_id','product_id','amount')"+"VALUES(?,?,?)");
            statement.setObject(1,connect.getRecipeId());
            statement.setObject(2,connect.getProductId());
            statement.setObject(3,connect.getAmount());
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void addProductToFridge(Product product)
    {
        try {
          //  PreparedStatement statement = connection.prepareStatement("INSERT INTO fridge('name','amount','unit')"+"VALUES(?,?,?)");
            PreparedStatement statement = connection.prepareStatement("INSERT INTO fridge('id','amount')"+"VALUES(?,?)");

            statement.setObject(1,product.getId());
            statement.setObject(2,product.getAmount());

            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addRecipe(Recipe recipe)
    {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO recipes('name','favorite')"+"VALUES(?,?)");
            statement.setObject(1,recipe.getName());
            statement.setObject(2,recipe.getFavorite());
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addProduct(Product product)
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

    public static ArrayList<Product> getAllProducts()
    {
        ArrayList<Product> list = new ArrayList<Product>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM products");
            while(resultSet.next())
            {
                //  System.out.println(resultSet.getString(1)+" "+resultSet.getString(2)+" "+resultSet.getString(3)+" "+resultSet.getString(4));
                list.add(new Product(Integer.parseInt(resultSet.getString(1)),resultSet.getString(2),0,resultSet.getString(3)));
            }
            resultSet.close();
            statement.close();

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return list;
    }

    public static ArrayList<Recipe> getAllRecipes()
    {
        ArrayList<Recipe> list = new ArrayList<Recipe>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM recipes");
            while(resultSet.next())
            {
              //    System.out.println(resultSet.getString(1)+" "+resultSet.getString(2)+" "+resultSet.getString(3));
                list.add(new Recipe(Integer.parseInt(resultSet.getString(1)),resultSet.getString(2),resultSet.getString(3)));
            }
            resultSet.close();
            statement.close();

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return list;
    }

    public static ArrayList<Product> getProductsToOneRecipe(int id)
    {
        ArrayList<Product> list = new ArrayList<Product>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT products.name, connect.amount, products.unit FROM connect JOIN products ON products.id=connect.product_id WHERE connect.recipe_id="+id);
          //  ResultSet resultSet = statement.executeQuery("SELECT students.id, students.lastname, students.name, students.group_id, groups.title FROM students JOIN groups ON groups.id = students.group_id");
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

    public static ArrayList<Product> getAllFridge()
    {
        ArrayList<Product> list = new ArrayList<Product>();
        try {
            Statement statement = connection.createStatement();
         //   ResultSet resultSet = statement.executeQuery("SELECT products.name, connect.amount, products.unit FROM connect JOIN products ON products.id=connect.product_id WHERE connect.recipe_id="+id);
            ResultSet resultSet = statement.executeQuery("SELECT products.name, fridge.amount, products.unit FROM fridge JOIN products ON products.id=fridge.id");
            while(resultSet.next())
            {
             //   System.out.println(resultSet.getString(1)+" "+resultSet.getString(2)+" "+resultSet.getString(3)+" "+resultSet.getString(4));
              //  list.add(new Product(Integer.parseInt(resultSet.getString(1)),resultSet.getString(2),Double.parseDouble(resultSet.getString(3)),resultSet.getString(4)));
                list.add(new Product(resultSet.getString(1),Double.parseDouble(resultSet.getString(2)),resultSet.getString(3)));
            }
            resultSet.close();
            statement.close();

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return list;
    }


    public static ArrayList<Connect> getAllConnection()
    {
        ArrayList<Connect> list = new ArrayList<Connect>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM connect");
            while(resultSet.next())
            {
                  System.out.println(resultSet.getString(1)+" "+resultSet.getString(2)+" "+resultSet.getString(3)+" "+resultSet.getString(4));
            //    list.add(new Connect(Integer.parseInt(resultSet.getString(2)),Integer.parseInt(resultSet.getString(3)),Double.parseDouble(resultSet.getString(4))));
            }
            resultSet.close();
            statement.close();

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return list;
    }


    public static ArrayList<Recipe> availableRecipes ()
    {
        ArrayList<Recipe> listRecipes = getAllRecipes();
        ArrayList<Recipe> itog=new ArrayList<Recipe>();
      //  List<Recipe> itog=new List<Recipe>();
        ArrayList<Connect> listConnect = new ArrayList<Connect>();
        ArrayList<Product> listProducts = new ArrayList<Product>();
        for(int i=0;i<listRecipes.size();i++)
        {
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM connect WHERE connect.recipe_id ="+listRecipes.get(i).getId());
                while(resultSet.next())
                {
                  //  System.out.println(resultSet.getString(1)+" "+resultSet.getString(2)+" "+resultSet.getString(3)+" "+resultSet.getString(4));
                  //   list.add(new Recipe(Integer.parseInt(resultSet.getString(1)),resultSet.getString(2),resultSet.getString(3)));
                    listConnect.add(new Connect(Integer.parseInt(resultSet.getString(2)),Integer.parseInt(resultSet.getString(3)),Double.parseDouble(resultSet.getString(4))));
                }
                resultSet.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            for(int j=0;j<listConnect.size();j++)
            {
                try {
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("SELECT * FROM fridge WHERE fridge.id ="+listConnect.get(j).getProductId()+" AND fridge.amount >="+listConnect.get(j).getAmount());
                    while(resultSet.next())
                    {
                     //   System.out.println(resultSet.getString(1)+" "+resultSet.getString(2)+" "+resultSet.getString(3)+" "+resultSet.getString(4));
                      //  listProducts.add(new Product(Integer.parseInt(resultSet.getString(1)),resultSet.getString(2),Double.parseDouble(resultSet.getString(3)),resultSet.getString(4)));
                        listProducts.add(new Product(Integer.parseInt(resultSet.getString(1)),Double.parseDouble(resultSet.getString(2))));
                    }
                    resultSet.close();
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if(listConnect.size()==listProducts.size())
            {
                itog.add(new Recipe(Integer.parseInt(listRecipes.get(i).getId()),listRecipes.get(i).getName(),listRecipes.get(i).getFavorite()));
            }
            listConnect.clear();
            listProducts.clear();
        }
        //сортировка элементов
        Collections.sort(
                itog,
                new Comparator<Recipe>() {
                    public int compare(final Recipe e1, final Recipe e2) {

                        return e1.getFavorite().compareTo(e2.getFavorite());
                    }
                }
        );

        return itog;
    }

    public static void deleteProductFromFridge(int id)  {
        try {
            Statement statement = connection.createStatement();
            statement.execute("DELETE FROM fridge WHERE fridge.id ="+id);
           // System.out.println("deleted!");
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void changeRecipe(Recipe recipe)  {
        try {
            Statement statement = connection.createStatement();
            statement.execute("UPDATE recipes SET name ='"+recipe.getName()+"', favorite ='"+recipe.getFavorite()+"' WHERE id ="+recipe.getId());
         //   statement.execute("UPDATE recipes SET favorite ='"+recipe.getFavorite()+"' WHERE id ="+recipe.getId());
           // "update students set ID='"+ id.getText() +"' , username='"+ username.getText() + "', password='"+ pass.getText() +"', firstname='"+ fname.getText() +"', lastname='"+ lname.getText() +"' WHERE ID='"+ id.getText() +"'  ";
           // statement.execute("CREATE TABLE if not exists 'recipes' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'name' VARCHAR(30),'favorite' VARCHAR(5));");
          //  statement.execute("CREATE TABLE if not exists 'recipes' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'name' VARCHAR(30),'favorite' VARCHAR(5));");
            // System.out.println("deleted!");
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

/*
    public static void test()
    {
        ArrayList<Recipe> itog=new ArrayList<Recipe>();
        itog.add(new Recipe("яичница","нет"));
        itog.add(new Recipe("фарш с картошкой","нет"));
        itog.add(new Recipe("борщ","да"));

        Collections.sort(
                itog,
                new Comparator<Recipe>() {
                    public int compare(final Recipe e1, final Recipe e2) {

                        return e1.getFavorite().compareTo(e2.getFavorite());
                    }
                }
        );

        for(int i=0;i<itog.size();i++ )
        {
            System.out.println(itog.get(i).getName()+" "+itog.get(i).getFavorite());
        }

    }
*/

}
