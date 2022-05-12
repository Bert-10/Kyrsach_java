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
       // addProduct(new Product("творог","кг"));
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
        /*
        ArrayList<Product> list = getMissingProductsToRecipe(2);
        for(int i=0;i< list.size();i++)
        {
            System.out.println(list.get(i).getName()+" "+list.get(i).getAmount()+" "+list.get(i).getUnit());
        }
        */
        /*
        ArrayList<Product>pro=productsNotInTheFridge();
        if(pro.size()==0)
        {
            System.out.println("blya");
        }
        */
      //  productsNotInTheFridge();
        /*
        DecimalFormat decimalFormat = new DecimalFormat("#.###");
        String secondResult = decimalFormat.format(7.000132);
        System.out.println((secondResult));
        */
      //  System.out.println(findIdOfProductByName("молоко"));
        //System.out.println(checkProductName("кака"));
       // getsAvailableProductsToOneRecipe(2);
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

    public static ArrayList<Product> getAllProducts()
    {
        ArrayList<Product> list = new ArrayList<Product>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM products");
            while(resultSet.next())
            {
                //  System.out.println(resultSet.getString(1)+" "+resultSet.getString(2)+" "+resultSet.getString(3)+" "+resultSet.getString(4));
                list.add(new Product(Integer.parseInt(resultSet.getString(1)),resultSet.getString(2),resultSet.getString(3)));
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

    public  static  ArrayList<Product> getMissingProductsToRecipe(int id)
    {
        boolean check=false;
        ArrayList<Product> productsToRecipe=new ArrayList<Product>();
        ArrayList<Product> fridge=getAllFridge();
        ArrayList<Product> itog=new ArrayList<Product>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT products.name, connect.amount, products.unit FROM connect JOIN products ON products.id=connect.product_id WHERE connect.recipe_id="+id);
            while(resultSet.next())
            {
                //  System.out.println(resultSet.getString(1)+" "+resultSet.getString(2)+" "+resultSet.getString(3));
                productsToRecipe.add(new Product(resultSet.getString(1),Double.parseDouble(resultSet.getString(2)),resultSet.getString(3)));
            }
            resultSet.close();
            statement.close();

        } catch (SQLException e) {

            e.printStackTrace();
        }
        for(int i=0;i<productsToRecipe.size();i++)
        {
            check=false;
            for(int j=0;j<fridge.size();j++)
            {
                if(Objects.equals(fridge.get(j).getName(), productsToRecipe.get(i).getName()))
                {
                    if(Double.parseDouble(fridge.get(j).getAmount())<Double.parseDouble(productsToRecipe.get(i).getAmount()))
                    {
                        itog.add(new Product(fridge.get(j).getName(),Double.parseDouble(productsToRecipe.get(i).getAmount())-Double.parseDouble(fridge.get(j).getAmount()),fridge.get(j).getUnit()));
                    }
                    check=true;
                    break;
                }
            }
            if(check==false)
            {
                itog.add(new Product(productsToRecipe.get(i).getName(),Double.parseDouble(productsToRecipe.get(i).getAmount()),productsToRecipe.get(i).getUnit()));

            }

        }

        return itog;
    }

    public static ArrayList<Product> getProductsToOneRecipe(int id)
    {
        ArrayList<Product> list = new ArrayList<Product>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT products.id, products.name, connect.amount, products.unit FROM connect JOIN products ON products.id=connect.product_id WHERE connect.recipe_id="+id);
          //  ResultSet resultSet = statement.executeQuery("SELECT students.id, students.lastname, students.name, students.group_id, groups.title FROM students JOIN groups ON groups.id = students.group_id");
            while(resultSet.next())
            {
              //  System.out.println(resultSet.getString(1)+" "+resultSet.getString(2)+" "+resultSet.getString(3));
                list.add(new Product(Integer.parseInt(resultSet.getString(1)),resultSet.getString(2),Double.parseDouble(resultSet.getString(3)),resultSet.getString(4)));
            }
            resultSet.close();
            statement.close();

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return list;
    }

    public static ArrayList<Product> getsAvailableProductsToOneRecipe(int id)
    {
        ArrayList<Product> list = getProductsToOneRecipe(id);
        ArrayList<Product> listProducts = getAllProducts();
        ArrayList<Product> itog = new ArrayList<Product>();
        boolean check=false;
        for(int i=0;i<listProducts.size();i++)
        {
            check=false;
            for(int j=0;j<list.size();j++)
            {
                if(Objects.equals(listProducts.get(i).getId(), list.get(j).getId()))
                {
                    check=true;
                    break;
                }
            }
            if(check==false)
            {
               // System.out.println(listProducts.get(i).getId()+" "+listProducts.get(i).getName()+" "+listProducts.get(i).getUnit());
                itog.add(new Product(Integer.parseInt(listProducts.get(i).getId()),listProducts.get(i).getName(),listProducts.get(i).getUnit()));

            }

        }
        return  itog;
    }

    public static ArrayList<Product> getAllFridge()
    {
        ArrayList<Product> list = new ArrayList<Product>();
        try {
            Statement statement = connection.createStatement();
         //   ResultSet resultSet = statement.executeQuery("SELECT products.name, connect.amount, products.unit FROM connect JOIN products ON products.id=connect.product_id WHERE connect.recipe_id="+id);
            ResultSet resultSet = statement.executeQuery("SELECT fridge.id, products.name, fridge.amount, products.unit FROM fridge JOIN products ON products.id=fridge.id");
            while(resultSet.next())
            {
             //   System.out.println(resultSet.getString(1)+" "+resultSet.getString(2)+" "+resultSet.getString(3)+" "+resultSet.getString(4));
                list.add(new Product(Integer.parseInt(resultSet.getString(1)),resultSet.getString(2),Double.parseDouble(resultSet.getString(3)),resultSet.getString(4)));
             //   list.add(new Product(resultSet.getString(1),Double.parseDouble(resultSet.getString(2)),resultSet.getString(3)));
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
              //    System.out.println(resultSet.getString(1)+" "+resultSet.getString(2)+" "+resultSet.getString(3)+" "+resultSet.getString(4));
                list.add(new Connect(Integer.parseInt(resultSet.getString(2)),Integer.parseInt(resultSet.getString(3)),Double.parseDouble(resultSet.getString(4))));
            }
            resultSet.close();
            statement.close();

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return list;
    }

    public static ArrayList<Product> productsNotInTheFridge ()
    {
        //   ResultSet resultSet = statement.executeQuery("SELECT * FROM products JOIN fridge ON products.id=fridge.id WHERE fridge.id=products.id");
        ArrayList<Product> list = new ArrayList<Product>();
        ArrayList<Product> listFridge=getAllFridge();
        ArrayList<Product> listProducts=getAllProducts();
    //    /*
        boolean check=false;
        for(int i=0;i<listProducts.size();i++)
        {
            check=false;
            for(int j=0;j<listFridge.size();j++)
            {
                if(Objects.equals(listProducts.get(i).getId(), listFridge.get(j).getId()))
                {
                    check=true;
                    break;
                }
            }
            if(check==false)
            {
             //   System.out.println(listProducts.get(i).getId()+" "+listProducts.get(i).getName()+" "+listProducts.get(i).getUnit());
                list.add(new Product(Integer.parseInt(listProducts.get(i).getId()),listProducts.get(i).getName(),listProducts.get(i).getUnit()));

            }

        }
        return list;
    }

    public static ArrayList<Recipe> getUnavailableRecipes ()
    {
        ArrayList<Recipe> listAvailableRecipes = availableRecipes();
        ArrayList<Recipe> listAllRecipes = getAllRecipes();
        ArrayList<Recipe> itog=new ArrayList<Recipe>();
        boolean check=false;
        for(int i=0;i<listAllRecipes.size();i++)
        {
            check=false;
            for(int j=0;j<listAvailableRecipes.size();j++)
            {
                if(Objects.equals(listAllRecipes.get(i).getId(), listAvailableRecipes.get(j).getId()))
                {
                    check=true;
                    break;
                }
            }
            if(check==false)
            {
                itog.add(listAllRecipes.get(i));
            }
        }
        return itog;
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

    public static void changeProductInFridge(Product product)  {
        try {
            Statement statement = connection.createStatement();
            statement.execute("UPDATE fridge SET amount ='"+product.getAmount()+"' WHERE id ="+product.getId());
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean addRecipe(Recipe recipe)
    {
        boolean check=false;
        if(checkRecipeName(recipe.getName()))
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
            check=true;
        }
        else
        {
            check=false;
        }
        return check;
    }

    public static void deleteRecipe(int id)  {
       deleteConnect(id,1);
        try {
            Statement statement = connection.createStatement();
            statement.execute("DELETE FROM recipes WHERE recipes.id ="+id);
            // System.out.println("deleted!");
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean changeRecipe(Recipe recipe,int switcher)  {
        boolean check=false;
        switch(switcher)
        {
            case 1:
                if(checkRecipeName(recipe.getName()))
                {
                    try {
                        Statement statement = connection.createStatement();
                        statement.execute("UPDATE recipes SET name ='"+recipe.getName()+"', favorite ='"+recipe.getFavorite()+"' WHERE id ="+recipe.getId());
                        statement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    check=true;
                }
                else
                {
                    check=false;
                }
                break;
            case 2:
                try {
                    Statement statement = connection.createStatement();
                    statement.execute("UPDATE recipes SET favorite ='"+recipe.getFavorite()+"' WHERE id ="+recipe.getId());
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                check=true;
                break;
        }

        return check;
    }

    public static boolean checkRecipeName(String name)
    {
        boolean check=true;

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM recipes WHERE recipes.name='"+name+"'");
            while(resultSet.next())
            {
                check=false;
            }
            resultSet.close();
            statement.close();

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return check;
    }

    public static boolean addProduct(Product product)
    {
        boolean check=false;
        if(checkProductName(product.getName()))
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
            check=true;
        }
        else
        {
            check=false;
        }
        return check;
    }

    public static void deleteProduct(int id)
    {
        deleteProductFromFridge(id);
        deleteConnect(id,2);
        try {
            Statement statement = connection.createStatement();
            statement.execute("DELETE FROM products WHERE products.id ="+id);
            // System.out.println("deleted!");
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean changeProduct(Product product, int switcher)  {
        boolean check=false;
        switch (switcher)
        {
            case 1:
                if(checkProductName(product.getName()))
                {
                  //  System.out.println("1");
                    try {
                        Statement statement = connection.createStatement();
                        statement.execute("UPDATE products SET name ='"+product.getName()+"', unit ='"+product.getUnit()+"' WHERE id ="+product.getId());
                        statement.close();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    check=true;
                }
                else
                {
                    check=false;
                }
                break;
            case 2:
               // System.out.println("2");
                    try {
                        Statement statement = connection.createStatement();
                        statement.execute("UPDATE products SET unit ='"+product.getUnit()+"' WHERE id ="+product.getId());
                        statement.close();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    check=true;
                break;
        }
        return  check;
    }

    public static Product findIdOfProductByName(String name)
    {
       // int id=-1;
        Product product=new Product(-1,"","");
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM products WHERE products.name='"+name+"'");
            while(resultSet.next())
            {
              //  id=Integer.parseInt(resultSet.getString(1));
                product=new Product(Integer.parseInt(resultSet.getString(1)),resultSet.getString(2),resultSet.getString(3));
            }

            resultSet.close();
            statement.close();

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return product;
    }

    public static boolean checkProductName(String name)
    {
        boolean check=true;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM products WHERE products.name='"+name+"'");
            while(resultSet.next())
            {
               check=false;
            }

            resultSet.close();
            statement.close();

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return check;
    }

    public static void addConnect(Connect connect)
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

    public static void deleteConnect(int id,int switcher)
    {
        switch(switcher)
        {
            case 1:
                try {
                    Statement statement = connection.createStatement();
                    statement.execute("DELETE FROM connect WHERE connect.recipe_id ="+id);
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    Statement statement = connection.createStatement();
                    statement.execute("DELETE FROM connect WHERE connect.product_id ="+id);
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    Statement statement = connection.createStatement();
                    statement.execute("DELETE FROM connect WHERE connect.connect_id ="+id);
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public static int findIdConnect(int recipe_id,int product_id)
    {
        int id=-1;

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM connect WHERE recipe_id="+recipe_id+" AND product_id="+product_id);
            while(resultSet.next())
            {
              //  System.out.println(resultSet.getString(1)+" "+resultSet.getString(2)+" "+resultSet.getString(3)+" "+resultSet.getString(4));
                id=Integer.parseInt(resultSet.getString(1));
            }
            resultSet.close();
            statement.close();

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return id;
    }

    public static void changeConnect(Connect connect)  {
        try {
            Statement statement = connection.createStatement();
            statement.execute("UPDATE connect SET amount ='"+connect.getAmount()+"' WHERE connect_id ="+connect.getId());
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }





}
