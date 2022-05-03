package db;

import models.Connects.Connect;
import models.Pecipes.Recipe;
import models.Products.Product;

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
        addRecipe(new Recipe("Фарш с картошкой","нет"));

        addConnection(new Connect(1,3,4));
        addConnection(new Connect(2,1,1.3));
        addConnection(new Connect(2,5,2));

        addProductToFridge(new Product(1,"фарш",1,"кг"));
        addProductToFridge(new Product(5,"картофель",4,"кг"));
        addProductToFridge(new Product(3,"яйцо",10,"шт"));
   //        */;
        //getAllProducts();


      //  getAllConnection();
      //  System.out.println("");
        availableRecipes ();
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
            statement.execute("CREATE TABLE if not exists 'fridge' ('id' INTEGER PRIMARY KEY,'name' VARCHAR(30) UNIQUE, 'amount' DOUBLE NOT NULL, 'unit' VARCHAR(5), FOREIGN KEY (id) REFERENCES products (id));");
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
            PreparedStatement statement = connection.prepareStatement("INSERT INTO fridge('id','name','amount','unit')"+"VALUES(?,?,?,?)");

            statement.setObject(1,product.getId());
            statement.setObject(2,product.getName());
            statement.setObject(3,product.getAmount());
            statement.setObject(4,product.getUnit());


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

    public static ArrayList<Product> getAllFridge()
    {
        ArrayList<Product> list = new ArrayList<Product>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM fridge");
            while(resultSet.next())
            {
             //   System.out.println(resultSet.getString(1)+" "+resultSet.getString(2)+" "+resultSet.getString(3)+" "+resultSet.getString(4));
                list.add(new Product(Integer.parseInt(resultSet.getString(1)),resultSet.getString(2),Double.parseDouble(resultSet.getString(3)),resultSet.getString(4)));
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
                        listProducts.add(new Product(Integer.parseInt(resultSet.getString(1)),resultSet.getString(2),Double.parseDouble(resultSet.getString(3)),resultSet.getString(4)));
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


        return itog;
    }

}
