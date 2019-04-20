package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainWindow();
            }
        });
    }
}
    /*public static void main(String[] args) {
        JFrame frame = new JFrame("Автопарк с различными типамии движущихся средств");

        frame.setSize(600,400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());


        JPanel panel1 = new JPanel(new FlowLayout());
        JPanel panel2 = new JPanel(new FlowLayout());
        //JPanel panel3 = new JPanel(new FlowLayout());

        JTextField textField = new JTextField(15);
        JButton buttonAdd = new JButton("Добавить");
        JButton buttonDel = new JButton("Удалить");

        //ComboBox
        String[] items = {"Vehicle","Lorry","Limousine","Saloon","SportCar","SUV","LuxeFeatures"};
        JComboBox comboBox = new JComboBox(items);

        panel1.setPreferredSize(new Dimension(600,200));

        //JList
        DefaultListModel dfm = new DefaultListModel();
        JList myList = new JList(dfm);
        JScrollPane myScrollPaneList  = new JScrollPane(myList);
        myScrollPaneList.setPreferredSize(new Dimension(300,200 ));
        myList.setLayoutOrientation(JList.VERTICAL);

        buttonAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = textField.getText();
                dfm.addElement(text);
            }
        });

        buttonDel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dfm.removeElement(myList.getSelectedValue());
            }
        });

        dfm.addElement("123456");
        dfm.addElement("111");
        dfm.addElement("222");

        panel2.add(myScrollPaneList);
        panel1.add(textField);
        panel1.add(buttonAdd);
        panel1.add(comboBox);
        panel1.add(buttonDel);

        frame.add(panel1, BorderLayout.NORTH);
        frame.add(panel2, BorderLayout.CENTER);
        frame.add(myScrollPaneList);

        frame.pack();
        frame.setVisible(true);
    }*/


