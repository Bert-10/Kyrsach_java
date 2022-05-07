package GUI;

import db.DBWorker;
import models.Pecipes.RecipeModel;
import models.Products.FridgeModel;
import models.Products.ProductModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainForm extends JFrame {
    private JTable Table;
  //  private JButton addStudent;
  //  private JButton deleteStudent;
    private FridgeModel modelFridge;
    private RecipeModel modelRecipe;
    private ProductModel modelProduct;
    private Container contentPane=this.getContentPane();
    private JButton openProductsToRecipe=new JButton("Показать продукты необходимые для рецепта");
    private JButton closeProductsToRecipe=new JButton("Возврат к рецептам ");
  //  private JLabel fake=new JLabel("");


    public MainForm()
    {
        setTitle("Smart fridge");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        createFileMenu();
        init();
        openProductsToRecipe.addActionListener(e->openProductsToRecipePress());
        closeProductsToRecipe.addActionListener(e->showRecipes());
      //  contentPane.add(fake);
        setVisible(true);
    }

    void openProductsToRecipePress()
    {

        if(Table.getSelectedRow()==-1)
        {
            JOptionPane.showMessageDialog(null, "Строка для показа содержимого рецепта не выделена", "Ошибка показа рецепта", JOptionPane.INFORMATION_MESSAGE);
        }
        else
        {
        //    System.out.println(Table.getSelectedRow()+1);
         //   FridgeModel model;
            contentPane.removeAll();
            modelFridge= new FridgeModel(DBWorker.getProductsToOneRecipe(Table.getSelectedRow()+1));
            Table = new JTable();
            Table.setModel(modelFridge);
            //  contentPane = this.getContentPane();
            contentPane.add(new JScrollPane(Table),BorderLayout.CENTER);
            contentPane.add(closeProductsToRecipe,BorderLayout.SOUTH);
            revalidate();
        }

    }

    private void init() {

        DBWorker.initDB();
        ///*
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                    DBWorker.closeDB();
            }
        });
      //  */


       // this.add(contentPane);
        showFridge();
        this.setLocationByPlatform(true);

    }

    void createFileMenu()
    {
        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("Меню");
        JMenuItem showFridge = new JMenuItem("Показать содержимое холодильника");
        JMenuItem showProducts = new JMenuItem("Показать продукты");
        JMenuItem showRecipes = new JMenuItem("Показать рецепты");
        JMenuItem showAvailableRecipes = new JMenuItem("Показ возможных рецептов");
        file.add(showFridge);
        file.addSeparator();
        file.add(showProducts);
        file.addSeparator();
        file.add(showRecipes);
        file.addSeparator();
        file.add(showAvailableRecipes);



        showFridge.addActionListener(e-> showFridge());
        showProducts.addActionListener(e-> showProducts());
        showRecipes.addActionListener(e->showRecipes());
        showAvailableRecipes.addActionListener(e->showAvailableRecipes());
        menuBar.add(file);
        setJMenuBar(menuBar);
    }

    void showFridge()
    {
        contentPane.removeAll();
        Table = new JTable();
        modelFridge= new FridgeModel(DBWorker.getAllFridge());
        Table.setModel(modelFridge);
      //  contentPane = this.getContentPane();
        contentPane.add(new JScrollPane(Table),BorderLayout.CENTER);
        revalidate();
    }

    void showProducts()
    {
        contentPane.removeAll();
        Table = new JTable();
        modelProduct = new ProductModel(DBWorker.getAllProducts());
        Table.setModel(modelProduct);
      //  contentPane = this.getContentPane();
        contentPane.add(new JScrollPane(Table),BorderLayout.CENTER);
        revalidate();
    }

    void showRecipes()
    {
        contentPane.removeAll();
        Table = new JTable();
        modelRecipe = new RecipeModel(DBWorker.getAllRecipes());
        Table.setModel(modelRecipe);
      //  contentPane = this.getContentPane();
        contentPane.add(new JScrollPane(Table),BorderLayout.CENTER);
        contentPane.add(openProductsToRecipe,BorderLayout.SOUTH);
        revalidate();
    }

    void showAvailableRecipes()
    {
        contentPane.removeAll();
        Table = new JTable();
        modelRecipe = new RecipeModel(DBWorker.availableRecipes());
        Table.setModel(modelRecipe);
        //  contentPane = this.getContentPane();
        contentPane.add(new JScrollPane(Table),BorderLayout.CENTER);
        revalidate();
    }

}
