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
    private JTable studTable;
  //  private JButton addStudent;
  //  private JButton deleteStudent;
    private ProductModel model;


    public MainForm()
    {
        setTitle("Smart fridge");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
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

        studTable = new JTable();
        model = new ProductModel(DBWorker.getAllFridge());
        studTable.setModel(model);

        Container contentPane = this.getContentPane();
        contentPane.add(new JScrollPane(studTable),BorderLayout.CENTER);


        this.setLocationByPlatform(true);

    }
}
