package GUI;

import db.DBWorker;
import model.ProductModel;

import javax.swing.*;
import javax.swing.JComboBox;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

public class MainForm extends JFrame {
    private JTable productsTable;
  //  private JButton addStudent;
  //  private JButton deleteStudent;
    private ProductModel model;


    public MainForm()
    {
        setTitle("Smart fridge");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        createFileMenu();
        init();
        setVisible(true);
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
        productsTable = new JTable();
        model = new ProductModel(DBWorker.getAllFridge());
        productsTable.setModel(model);
        Container contentPane = this.getContentPane();
        contentPane.add(new JScrollPane(productsTable),BorderLayout.CENTER);
        this.setLocationByPlatform(true);

    }

    void createFileMenu()
    {
        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("Меню");
        JMenuItem add = new JMenuItem("Добавить продукт в холодильник");
        JMenuItem del = new JMenuItem("Убрать продукт из холодильника");
        JMenuItem change = new JMenuItem("Изменить продукт в холодильнике");
        file.add(add);
        file.addSeparator();
        file.add(del);
        file.addSeparator();
        file.add(change);
     //   add.addActionListener(e-> Add());
      //  del.addActionListener(e-> Del());
      //  search.addActionListener(e->SearchFrame());
        menuBar.add(file);
        setJMenuBar(menuBar);
    }
}
