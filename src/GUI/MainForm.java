package GUI;

import db.DBWorker;
import models.Connects.Connect;
import models.Pecipes.Recipe;
import models.Pecipes.RecipeModel;
import models.Products.FridgeModel;
import models.Products.Product;
import models.Products.ProductModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Objects;

import static IncorrectInput.Proverka.CheckNumber;
import static IncorrectInput.Proverka.CheckStroka;

public class MainForm extends JFrame {
    private JTable Table;
  //  private JButton addStudent;
  //  private JButton deleteStudent;
    private FridgeModel modelFridge;
    private RecipeModel modelRecipe;
    private ProductModel modelProduct;
    private Container contentPane=this.getContentPane();
    private JButton openProductsToRecipe=new JButton("Показать продукты для рецепта");
    private JButton closeProductsToRecipe=new JButton("Возврат к рецептам");

    private JButton add=new JButton("Добавить");
    private JButton del=new JButton("Удалить");
    private JButton change=new JButton("Изменить");
    private JButton showUnavailableRecipes= new JButton("Показать недостоующие компоненты для рецепта");
    private Container containerOfButtons=new Container();
    private Container containerOfAdds=new Container();
    private Container containerOfChange=new Container();
    private ArrayList<Product> listOfProducts = new ArrayList<Product>();
    private ArrayList<Recipe> listOfRecipes = new ArrayList<Recipe>();
    private JComboBox comboBox;
    private JComboBox comboBoxStatic;
    private JLabel label1=new JLabel("Продукт");
    private JLabel label2=new JLabel("Количество");
    private JLabel label3=new JLabel("Ед. измерения");
    private JLabel label4=new JLabel("");
    private JLabel labelFake=new JLabel("");
    private JLabel label5=new JLabel("");
    private JLabel label6=new JLabel("");
    private JTextField PoleVvoda= new JTextField("");
    private JTextField PoleVvoda2= new JTextField("");
    private String[] units={"кг","л","шт"};
    private String[] favorites={"да","нет"};
    private JButton addElement=new JButton("Добавить элемент");
    private JButton changeElement=new JButton("Изменить элемент");
    private JButton realisationOfRecipe=new JButton("Реализовать рецепт");
    private boolean checkAdd=false;
    private int switcher=1,selectedRow=-1,selectedRowOfRecipes=-1,backRecipeCheck;
    private JMenuBar menuBar = new JMenuBar();
    private JMenuItem back = new JMenuItem("Назад");
  //  private JLabel fake=new JLabel("");


    public MainForm()
    {
        setTitle("Smart fridge");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        createFileMenu();
        init();
        openProductsToRecipe.addActionListener(e-> {
            selectedRowOfRecipes = Table.getSelectedRow();
            openProductsToRecipePress();
        });
      //  closeProductsToRecipe.addActionListener(e->showRecipes());
        del.addActionListener(e->del());
        add.addActionListener(e->add());
        change.addActionListener(e->change());

        changeElement.addActionListener(e->changeElement());
        addElement.addActionListener(e->addElement());
        realisationOfRecipe.addActionListener(e->realiseRecipe());
        setVisible(true);
        showUnavailableRecipes.addActionListener(e->getMissingProductsToRecipe());
        containerOfButtons.setLayout(new FlowLayout());
        containerOfButtons.removeAll();
        containerOfButtons.add(add);
        containerOfButtons.add(del);
        containerOfButtons.add(change);
        containerOfButtons.revalidate();
       // pack();
        setSize(550,200);
    }

    void getMissingProductsToRecipe()
    {
        if(Table.getSelectedRow()==-1)
        {
            JOptionPane.showMessageDialog(null, "Строка для показа нехватающих продуктов для рецепта не выделена", "Ошибка показа нехватающих продуктов", JOptionPane.INFORMATION_MESSAGE);
        }
        else
        {
            menuBar.add(back);
            setJMenuBar(menuBar);

            contentPane.removeAll();

            modelFridge= new FridgeModel(modelFridge.getMissingProductsToRecipe(Integer.parseInt(modelRecipe.getRecipe(Table.getSelectedRow()).getId())));
            Table = new JTable();
            Table.setModel(modelFridge);
            contentPane.add(new JScrollPane(Table),BorderLayout.CENTER);
            contentPane.revalidate();
        }
    }

    void addElement()
    {
        switch (switcher)
        {
            case 1:
                if(CheckNumber(PoleVvoda.getText()))
                {
                    modelFridge.addProductToFridge(new Product(Integer.parseInt(modelFridge.findIdOfProductByName((String)comboBox.getSelectedItem()).getId()),Double.parseDouble(PoleVvoda.getText())));
                    PoleVvoda.setText("");
                    showFridge();
                }
                break;
            case 2:
                if(CheckStroka(PoleVvoda.getText()))
                {
                    if(!modelProduct.addProduct(new Product(PoleVvoda.getText(),(String)comboBoxStatic.getSelectedItem())))
                    {
                        JOptionPane.showMessageDialog(null, "Введённое имя нарушает уникальность в базе данных.\nВведите другое имя", "Ошибка изменения элемента", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else
                    {
                        showProducts();
                    }
                }
                break;
            case 3:
                if(CheckStroka(PoleVvoda.getText()))
                {
                    if(!modelRecipe.addRecipe(new Recipe(PoleVvoda.getText(),(String)comboBoxStatic.getSelectedItem())))
                    {
                        JOptionPane.showMessageDialog(null, "Введённое имя нарушает уникальность в базе данных.\nВведите другое имя", "Ошибка изменения элемента", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else
                    {
                        showRecipes();
                    }
                }
                break;
            case 4:
                if(CheckNumber(PoleVvoda.getText()))
                {
                    modelFridge.addConnect(new Connect(Integer.parseInt(modelRecipe.getRecipe(selectedRowOfRecipes).getId()),Integer.parseInt(modelFridge.findIdOfProductByName((String)comboBox.getSelectedItem()).getId()),Double.parseDouble(PoleVvoda.getText())));
                    PoleVvoda.setText("");
                    openProductsToRecipePress();
                }
                break;
        }
    }

    void comboBoxPress()
    {
        /*
       for(int i=0;i<listOfProducts.size();i++)
       {
           if(Objects.equals((String) comboBox.getSelectedItem(), listOfProducts.get(i).getName()))
           {
               label4.setText(listOfProducts.get(i).getUnit());
               break;
           }
       }
        */
        label4.setText(modelFridge.findIdOfProductByName((String)comboBox.getSelectedItem()).getUnit());
    }

    void openProductsToRecipePress()
    {

       // if(Table.getSelectedRow()==-1)
        if(selectedRowOfRecipes==-1)
        {
            JOptionPane.showMessageDialog(null, "Строка для показа содержимого рецепта не выделена", "Ошибка показа рецепта", JOptionPane.INFORMATION_MESSAGE);
        }
        else
        {
            menuBar.add(back);
            setJMenuBar(menuBar);

            switcher=4;
            backRecipeCheck=1;
          //  selectedRow=Table.getSelectedRow();

            contentPane.removeAll();

         //   modelFridge= new FridgeModel(DBWorker.getProductsToOneRecipe(Integer.parseInt(listOfRecipes.get(Table.getSelectedRow()).getId())));
            modelFridge= new FridgeModel(modelRecipe.getProductsToOneRecipe(Integer.parseInt(modelRecipe.getRecipe(selectedRowOfRecipes).getId())));
            Table = new JTable();
            Table.setModel(modelFridge);

            contentPane.add(new JScrollPane(Table),BorderLayout.CENTER);

            containerOfButtons.remove(openProductsToRecipe);
            contentPane.add(containerOfButtons,BorderLayout.SOUTH);
       //     contentPane.add(closeProductsToRecipe,BorderLayout.SOUTH);
            contentPane.revalidate();
            containerOfButtons.revalidate();
            contentPane.setVisible(false);
            contentPane.setVisible(true);
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
        JMenu file = new JMenu("Меню");
        JMenuItem showFridge = new JMenuItem("Показать содержимое холодильника");
        JMenuItem showProducts = new JMenuItem("Показать продукты");
        JMenuItem showRecipes = new JMenuItem("Показать рецепты");
        JMenuItem showAvailableRecipes = new JMenuItem("Показ возможных рецептов");
        JMenuItem showUnavailableRecipes = new JMenuItem("Показ невозможных рецептов");

       // file.add(pomenu);
       // file.addSeparator();
        file.add(showFridge);
        file.addSeparator();
        file.add(showProducts);
        file.addSeparator();
        file.add(showRecipes);
        file.addSeparator();
        file.add(showAvailableRecipes);
        file.addSeparator();
        file.add(showUnavailableRecipes);

        showFridge.addActionListener(e-> showFridge());
        showProducts.addActionListener(e-> showProducts());
        showRecipes.addActionListener(e->showRecipes());
        showAvailableRecipes.addActionListener(e->showAvailableRecipes());
        showUnavailableRecipes.addActionListener(e->showUnavailableRecipes());
        back.addActionListener(e->back());
        menuBar.add(file);
        menuBar.add(back);
     //   menuBar.remove(back);
        setJMenuBar(menuBar);
    }

    void back()
    {
        switch(-switcher)
        {
            case -1:
                showFridge();
                break;
            case -2:
                showProducts();
                break;
            case -3:
                showRecipes();
                break;
            case -4:
                if(backRecipeCheck==1)
                {
                    showRecipes();
                }
                else
                {
                    openProductsToRecipePress();
                   // getMissingProductsToRecipe();
                }
                break;
            case -5:
                //getMissingProductsToRecipe();
                showUnavailableRecipes();
                break;
        }
    }

    void changeElement()
    {
        switch (switcher)
        {
            case 1:
                if(CheckNumber(PoleVvoda.getText()))
                {
                    modelFridge.changeProductInFridge(new Product(Integer.parseInt(modelFridge.getProduct(selectedRow).getId()),Double.parseDouble(PoleVvoda.getText())));
                    PoleVvoda.setText("");
                    showFridge();
                }
                break;
            case 2:
                if(CheckStroka(PoleVvoda.getText()))
                {
                    if(Objects.equals(PoleVvoda.getText(), labelFake.getText()))
                    {
                        modelProduct.changeProduct(new Product(Integer.parseInt(modelProduct.getProduct(selectedRow).getId()),PoleVvoda.getText(),(String)comboBoxStatic.getSelectedItem()),2);
                        showProducts();
                    }
                    else
                    {
                        if(!modelProduct.changeProduct(new Product(Integer.parseInt(modelProduct.getProduct(selectedRow).getId()),PoleVvoda.getText(),(String)comboBoxStatic.getSelectedItem()),1))
                        {
                            JOptionPane.showMessageDialog(null, "Введённое имя нарушает уникальность в базе данных.\nВведите другое имя", "Ошибка изменения элемента", JOptionPane.INFORMATION_MESSAGE);
                        }
                        else
                        {
                            showProducts();
                        }
                    }
                }
                break;
            case 3:
                if(CheckStroka(PoleVvoda.getText()))
                {
                    if(Objects.equals(PoleVvoda.getText(), labelFake.getText()))
                    {
                        modelRecipe.changeRecipe(new Recipe(Integer.parseInt(modelRecipe.getRecipe(selectedRow).getId()),PoleVvoda.getText(),(String)comboBoxStatic.getSelectedItem()),2);
                        showRecipes();
                    }
                    else
                    {
                        if(!modelRecipe.changeRecipe(new Recipe(Integer.parseInt(modelRecipe.getRecipe(selectedRow).getId()),PoleVvoda.getText(),(String)comboBoxStatic.getSelectedItem()),1))
                        {
                            JOptionPane.showMessageDialog(null, "Введённое имя нарушает уникальность в базе данных.\nВведите другое имя", "Ошибка изменения элемента", JOptionPane.INFORMATION_MESSAGE);
                        }
                        else
                        {
                            showRecipes();
                        }
                    }

                }
                break;
            case 4:
                if(CheckNumber(PoleVvoda.getText()))
                {
               //     modelFridge.changeConnect(new Connect(Integer.parseInt(modelRecipe.getRecipe(selectedRowOfRecipes).getId()),modelFridge.findIdOfProductByName((String)comboBox.getSelectedItem()),Double.parseDouble(PoleVvoda.getText())));
                    modelFridge.changeConnect(Integer.parseInt(modelRecipe.getRecipe(selectedRowOfRecipes).getId()),Integer.parseInt(modelFridge.getProduct(selectedRow).getId()),Double.parseDouble(PoleVvoda.getText()));
                    PoleVvoda.setText("");
                    openProductsToRecipePress();
                }
                break;
        }

    }

    void change()
    {

        switch (switcher)
        {
            case 1:
                if(Table.getSelectedRow()==-1)
                {
                    JOptionPane.showMessageDialog(null, "Строка для изменения не выделена", "Ошибка изменения элемента", JOptionPane.INFORMATION_MESSAGE);
                }
                else
                {
                    // modelFridge.changeProductInFridge(new Product(modelFridge.getProduct((Table.getSelectedRow()),));
                    // showFridge();
                    menuBar.add(back);
                    setJMenuBar(menuBar);
                    selectedRow=Table.getSelectedRow();

                    label1.setText("Вы изменяете продукт:");
                    labelFake.setText(modelFridge.getProduct(Table.getSelectedRow()).getName());
                    label2.setText("Введите новое количество");
                    label3.setText("Ед. измерения");
                    label4.setText(modelFridge.getProduct(Table.getSelectedRow()).getUnit());
                    PoleVvoda.setText("");
                    contentPane.removeAll();
                    containerOfAdds.removeAll();
                    containerOfAdds.setLayout(new GridLayout(4,2,5,12));
                    containerOfAdds.add(label1);
                    containerOfAdds.add(labelFake);
                    containerOfAdds.add(label2);
                    containerOfAdds.add(PoleVvoda);
                    containerOfAdds.add(label3);
                    containerOfAdds.add(label4);
                    containerOfAdds.add(changeElement);

                    contentPane.add(containerOfAdds,BorderLayout.CENTER);

                    containerOfAdds.revalidate();
                    contentPane.revalidate();

                    contentPane.setVisible(false);
                    contentPane.setVisible(true);
                }
                break;
            case 2:
                if(Table.getSelectedRow()==-1)
                {
                    JOptionPane.showMessageDialog(null, "Строка для изменения не выделена", "Ошибка изменения элемента", JOptionPane.INFORMATION_MESSAGE);
                }
                else
                {
                    menuBar.add(back);
                    setJMenuBar(menuBar);
                    selectedRow=Table.getSelectedRow();

                    comboBoxStatic=new JComboBox(units);
                    label1.setText("Вы изменяете продукт:");
                    labelFake.setText(modelProduct.getProduct(Table.getSelectedRow()).getName());
                    PoleVvoda.setText(modelProduct.getProduct(Table.getSelectedRow()).getName());
                    label2.setText("Новое имя продукта");
                    label3.setText("Ед. измерения");
                  //  label4.setText(modelProduct.getProduct(Table.getSelectedRow()).getUnit());
                    contentPane.removeAll();
                    containerOfAdds.removeAll();
                    containerOfAdds.setLayout(new GridLayout(4,2,5,12));
                    containerOfAdds.add(label1);
                    containerOfAdds.add(labelFake);
                    containerOfAdds.add(label2);
                    containerOfAdds.add(PoleVvoda);
                    containerOfAdds.add(label3);
                    containerOfAdds.add(comboBoxStatic);

                    containerOfAdds.add(changeElement);

                    contentPane.add(containerOfAdds,BorderLayout.CENTER);
                    containerOfAdds.revalidate();
                    contentPane.revalidate();

                    contentPane.setVisible(false);
                    contentPane.setVisible(true);
                }
                break;
            case 3:
                if(Table.getSelectedRow()==-1)
                {
                    JOptionPane.showMessageDialog(null, "Строка для изменения не выделена", "Ошибка изменения элемента", JOptionPane.INFORMATION_MESSAGE);
                }
                else
                {
                    menuBar.add(back);
                    setJMenuBar(menuBar);
                    selectedRow=Table.getSelectedRow();

                    comboBoxStatic=new JComboBox(favorites);
                    label1.setText("Вы изменяете рецепт:");
                    labelFake.setText(modelRecipe.getRecipe(Table.getSelectedRow()).getName());
                    PoleVvoda.setText(modelRecipe.getRecipe(Table.getSelectedRow()).getName());
                    label2.setText("Новое имя рецепта");
                    label3.setText("Предпочитаемый");
                    //  label4.setText(modelProduct.getProduct(Table.getSelectedRow()).getUnit());
                    contentPane.removeAll();
                    containerOfAdds.removeAll();
                    containerOfAdds.setLayout(new GridLayout(4,2,5,12));
                    containerOfAdds.add(label1);
                    containerOfAdds.add(labelFake);
                    containerOfAdds.add(label2);
                    containerOfAdds.add(PoleVvoda);
                    containerOfAdds.add(label3);
                    containerOfAdds.add(comboBoxStatic);

                    containerOfAdds.add(changeElement);

                    contentPane.add(containerOfAdds,BorderLayout.CENTER);
                    containerOfAdds.revalidate();
                    contentPane.revalidate();

                    contentPane.setVisible(false);
                    contentPane.setVisible(true);
                }
                break;
            case 4:
                if(Table.getSelectedRow()==-1)
                {
                    JOptionPane.showMessageDialog(null, "Строка для изменения не выделена", "Ошибка изменения элемента", JOptionPane.INFORMATION_MESSAGE);
                }
                else
                {
                    menuBar.add(back);
                    setJMenuBar(menuBar);
                    backRecipeCheck=0;
                    //listOfProducts=modelFridge.getsAvailableProductsToOneRecipe(Integer.parseInt(modelRecipe.getRecipe(selectedRowOfRecipes).getId()));
                    contentPane.removeAll();

                    PoleVvoda.setText("");
                    label1.setText("Изменяемый продукт");
                    label2.setText("Введите новое Количество");
                    label3.setText("Ед. измерения");
                    label4.setText(modelFridge.getProduct(Table.getSelectedRow()).getUnit());
                    label5.setText("Вы изменяете продукт к рецепту");
                    labelFake.setText(modelRecipe.getRecipe(selectedRowOfRecipes).getName());
                    selectedRow=Table.getSelectedRow();
                    label6.setText(modelFridge.getProduct(Table.getSelectedRow()).getName());

                    containerOfAdds.removeAll();
                    containerOfAdds.setLayout(new GridLayout(5,2,5,12));
                    containerOfAdds.add(label5);
                    containerOfAdds.add(labelFake);
                    containerOfAdds.add(label1);
                    containerOfAdds.add(label6);
                    containerOfAdds.add(label2);
                    containerOfAdds.add(PoleVvoda);
                    containerOfAdds.add(label3);
                    containerOfAdds.add(label4);

                    containerOfAdds.add(changeElement);
                    contentPane.add(containerOfAdds,BorderLayout.CENTER);
                    containerOfAdds.revalidate();
                    contentPane.revalidate();

                    contentPane.setVisible(false);
                    contentPane.setVisible(true);
                }
                break;
        }

    }

    void del()
    {
        switch (switcher)
        {
            case 1:
                if(Table.getSelectedRow()==-1)
                {
                    JOptionPane.showMessageDialog(null, "Строка для удаления элемента не выделена", "Ошибка удаления", JOptionPane.INFORMATION_MESSAGE);
                }
                else
                {

                    modelFridge.deleteProductFromFridge(Integer.parseInt(modelFridge.getProduct(Table.getSelectedRow()).getId()));
                    showFridge();
                }
                break;
            case 2:
                if(Table.getSelectedRow()==-1)
                {
                    JOptionPane.showMessageDialog(null, "Строка для удаления элемента не выделена", "Ошибка удаления", JOptionPane.INFORMATION_MESSAGE);
                }
                else
                {
                    modelProduct.deleteProduct(Integer.parseInt(modelProduct.getProduct(Table.getSelectedRow()).getId()));
                    showProducts();
                }
                break;
            case 3:
                if(Table.getSelectedRow()==-1)
                {
                    JOptionPane.showMessageDialog(null, "Строка для удаления элемента не выделена", "Ошибка удаления", JOptionPane.INFORMATION_MESSAGE);
                }
                else
                {
                    modelRecipe.deleteRecipe(Integer.parseInt(modelRecipe.getRecipe(Table.getSelectedRow()).getId()));
                    showRecipes();
                }
                break;
            case 4:
                if(Table.getSelectedRow()==-1)
                {
                    JOptionPane.showMessageDialog(null, "Строка для удаления элемента не выделена", "Ошибка удаления", JOptionPane.INFORMATION_MESSAGE);
                }
                else
                {
                    modelFridge.deleteConnect(Integer.parseInt(modelRecipe.getRecipe(selectedRowOfRecipes).getId()),Integer.parseInt(modelFridge.getProduct(Table.getSelectedRow()).getId()),3);
                    openProductsToRecipePress();
                }
                break;
        }

    }

    void add()
    {

        switch (switcher)
        {
            case 1:
                String str="";
                String [] massStr;
                listOfProducts=DBWorker.productsNotInTheFridge();
                if(listOfProducts.size()==0)
                {
                    JOptionPane.showMessageDialog(null, "Нет продуктов, которых бы не было в холодильнике. Добавьте желаемый продукт в таблицу 'продукты', а затем повторите операцию", "Ошибка добавления", JOptionPane.INFORMATION_MESSAGE);
                    // PoleVvoda.setText("");
                }
                else
                {
                    menuBar.add(back);
                    setJMenuBar(menuBar);
                    for(int i=0;i< listOfProducts.size();i++)
                    {
                        str=str+listOfProducts.get(i).getName()+",";
                    }
                    str=str.substring(0,str.length()-1);
                    massStr=str.split(",");
                    comboBox=new JComboBox(massStr);
                    label4.setText(listOfProducts.get(0).getUnit());
                    contentPane.removeAll();

                    PoleVvoda.setText("");
                    label1.setText("Продукт");
                    label2.setText("Количество");
                    label3.setText("Ед. измерения");
                    // label4.setText("");
                    containerOfAdds.removeAll();
                    containerOfAdds.setLayout(new GridLayout(4,2,5,12));
                    containerOfAdds.add(label1);
                    containerOfAdds.add(comboBox);
                    containerOfAdds.add(label2);
                    containerOfAdds.add(PoleVvoda);
                    containerOfAdds.add(label3);
                    containerOfAdds.add(label4);
                    containerOfAdds.add(addElement);
                //    if(checkAdd==false)
                //    {
                        comboBox.addActionListener (e->comboBoxPress());
                 //       checkAdd=true;
                   // }

                    //   containerOfAdds.revalidate();

                    contentPane.add(containerOfAdds,BorderLayout.CENTER);
                    containerOfAdds.revalidate();
                    contentPane.revalidate();

                    contentPane.setVisible(false);
                    contentPane.setVisible(true);
                }
                break;
            case 2:
                menuBar.add(back);
                setJMenuBar(menuBar);
                comboBoxStatic=new JComboBox(units);
                contentPane.removeAll();

                PoleVvoda.setText("");
                label1.setText("Название продукта");
                label3.setText("Ед. измерения");
                // label4.setText("");
                containerOfAdds.removeAll();
                containerOfAdds.setLayout(new GridLayout(4,2,5,12));
                containerOfAdds.add(label1);
                containerOfAdds.add(PoleVvoda);
                containerOfAdds.add(label3);
                containerOfAdds.add(comboBoxStatic);

                containerOfAdds.add(addElement);
                contentPane.add(containerOfAdds,BorderLayout.CENTER);
                containerOfAdds.revalidate();
                contentPane.revalidate();

                contentPane.setVisible(false);
                contentPane.setVisible(true);
                break;
            case 3:
                menuBar.add(back);
                setJMenuBar(menuBar);
                comboBoxStatic=new JComboBox(favorites);
                contentPane.removeAll();

                PoleVvoda.setText("");
                label1.setText("Название рецепта");
                label3.setText("Предпочтительный");
                // label4.setText("");
                containerOfAdds.removeAll();
                containerOfAdds.setLayout(new GridLayout(4,2,5,12));
                containerOfAdds.add(label1);
                containerOfAdds.add(PoleVvoda);
                containerOfAdds.add(label3);
                containerOfAdds.add(comboBoxStatic);

                containerOfAdds.add(addElement);
                contentPane.add(containerOfAdds,BorderLayout.CENTER);
                containerOfAdds.revalidate();
                contentPane.revalidate();

                contentPane.setVisible(false);
                contentPane.setVisible(true);
                break;
            case 4:
                 str="";
              //  String [] massStr;
              //  System.out.println(selectedRowOfRecipes);
                listOfProducts=modelFridge.getsAvailableProductsToOneRecipe(Integer.parseInt(modelRecipe.getRecipe(selectedRowOfRecipes).getId()));

                if(listOfProducts.size()==0)
                {
                    JOptionPane.showMessageDialog(null, "Нет продуктов, которых бы не было в рецепте. Добавьте желаемый продукт в таблицу 'продукты', а затем повторите операцию", "Ошибка добавления", JOptionPane.INFORMATION_MESSAGE);
                    // PoleVvoda.setText("");
                }
                else {
                    for (int i = 0; i < listOfProducts.size(); i++) {
                        str = str + listOfProducts.get(i).getName() + ",";
                    }
                    backRecipeCheck=0;
                    menuBar.add(back);
                    setJMenuBar(menuBar);
                    str = str.substring(0, str.length() - 1);
                    massStr = str.split(",");
                    comboBox = new JComboBox(massStr);

               //     if(checkAdd==false)
              //      {
                    comboBox.addActionListener (e->comboBoxPress());
                   // checkAdd=true;
                 //   }

                    contentPane.removeAll();

                    PoleVvoda.setText("");
                    label1.setText("Название продукта");
                    label2.setText("Количество");
                    label3.setText("Ед. измерения");
                    label4.setText(listOfProducts.get(0).getUnit());
                    label5.setText("Вы добавляете продукт к рецепту");
                    labelFake.setText(modelRecipe.getRecipe(selectedRowOfRecipes).getName());

                    containerOfAdds.removeAll();
                    containerOfAdds.setLayout(new GridLayout(5,2,5,12));
                    containerOfAdds.add(label5);
                    containerOfAdds.add(labelFake);
                    containerOfAdds.add(label1);

                    containerOfAdds.add(comboBox);

                    containerOfAdds.add(label2);
                    containerOfAdds.add(PoleVvoda);
                    containerOfAdds.add(label3);
                    containerOfAdds.add(label4);

                    containerOfAdds.add(addElement);
                    contentPane.add(containerOfAdds,BorderLayout.CENTER);
                    containerOfAdds.revalidate();
                    contentPane.revalidate();

                    contentPane.setVisible(false);
                    contentPane.setVisible(true);
                }

                break;
        }

    }

    void showFridge()
    {
        menuBar.remove(back);
        setJMenuBar(menuBar);
        switcher=1;
        contentPane.removeAll();
        Table = new JTable();

        listOfProducts=DBWorker.getAllFridge();

        modelFridge= new FridgeModel(listOfProducts);

        Table.setModel(modelFridge);
      //  contentPane = this.getContentPane();
        contentPane.add(new JScrollPane(Table),BorderLayout.CENTER);
        /*
        containerOfButtons.setLayout(new FlowLayout());
        containerOfButtons.removeAll();
        containerOfButtons.add(add);
        containerOfButtons.add(del);
        containerOfButtons.add(change);
        containerOfButtons.revalidate();
        */
        containerOfButtons.remove(openProductsToRecipe);
        contentPane.add(containerOfButtons,BorderLayout.SOUTH);

        revalidate();
        contentPane.revalidate();
        containerOfButtons.revalidate();

        contentPane.setVisible(false);
        contentPane.setVisible(true);
    }

    void showProducts()
    {
        menuBar.remove(back);
        setJMenuBar(menuBar);
        switcher=2;
        contentPane.removeAll();
        Table = new JTable();
        modelProduct = new ProductModel(DBWorker.getAllProducts());
        Table.setModel(modelProduct);
      //  contentPane = this.getContentPane();
        contentPane.add(new JScrollPane(Table),BorderLayout.CENTER);
        containerOfButtons.remove(openProductsToRecipe);
        contentPane.add(containerOfButtons,BorderLayout.SOUTH);
        contentPane.revalidate();

        menuBar.revalidate();
        contentPane.setVisible(false);
        contentPane.setVisible(true);
    }

    void showRecipes()
    {
        menuBar.remove(back);
        setJMenuBar(menuBar);
        switcher=3;
        contentPane.removeAll();
        Table = new JTable();
        listOfRecipes=DBWorker.getAllRecipes();
       // modelRecipe = new RecipeModel(DBWorker.getAllRecipes());
        modelRecipe = new RecipeModel(listOfRecipes);
        Table.setModel(modelRecipe);
      //  contentPane = this.getContentPane();
        contentPane.add(new JScrollPane(Table),BorderLayout.CENTER);
        containerOfButtons.add(openProductsToRecipe);
        contentPane.add(containerOfButtons,BorderLayout.SOUTH);
     //   contentPane.add(openProductsToRecipe,BorderLayout.SOUTH);
        contentPane.revalidate();

        contentPane.setVisible(false);
        contentPane.setVisible(true);
    }

    void showUnavailableRecipes()
    {
        switcher=5;
        menuBar.remove(back);
        setJMenuBar(menuBar);
        contentPane.removeAll();
        Table = new JTable();
        /*
        modelRecipe = new RecipeModel(DBWorker.availableRecipes());
        Table.setModel(modelRecipe);
         */

        modelRecipe = new RecipeModel(DBWorker.getUnavailableRecipes());
        Table.setModel(modelRecipe);

        JScrollPane pane=new JScrollPane(Table);
        contentPane.add(pane,BorderLayout.CENTER);
        contentPane.add(showUnavailableRecipes,BorderLayout.SOUTH);
        contentPane.revalidate();

        contentPane.setVisible(false);
        contentPane.setVisible(true);

    }

    void showAvailableRecipes()
    {

        menuBar.remove(back);
        setJMenuBar(menuBar);
        contentPane.removeAll();
        Table = new JTable();
        modelRecipe = new RecipeModel(DBWorker.availableRecipes());
        Table.setModel(modelRecipe);
        //  contentPane = this.getContentPane();
        JScrollPane pane=new JScrollPane(Table);
      //  pane.setBorder(BorderFactory.createLineBorder(Color.RED,1));

        //contentPane.add(new JScrollPane(Table),BorderLayout.CENTER);
        contentPane.add(pane,BorderLayout.CENTER);
        //contentPane.setSelectionBackground(Color.red);
       //contentPane.setRowHeaderView("gf");
        contentPane.add(realisationOfRecipe,BorderLayout.SOUTH);

        contentPane.revalidate();

        contentPane.setVisible(false);
        contentPane.setVisible(true);
    }

    void realiseRecipe()
    {
        if(Table.getSelectedRow()==-1)
        {
            JOptionPane.showMessageDialog(null, "Не выбран рецепт для реализации", "Ошибка реализации", JOptionPane.INFORMATION_MESSAGE);
        }
        else
        {
            modelRecipe.realiseRecipe(Integer.parseInt(modelRecipe.getRecipe(Table.getSelectedRow()).getId()));
            showAvailableRecipes();
            JOptionPane.showMessageDialog(null, "Рецепт был успешно реализован", "Рецепт реализован", JOptionPane.INFORMATION_MESSAGE);
        }
    }

}
