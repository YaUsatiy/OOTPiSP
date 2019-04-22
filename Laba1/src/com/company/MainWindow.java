package com.company;

import com.sun.source.util.Plugin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Base64;

public class MainWindow extends JFrame {

    ArrayList<Vehicle> VehicleList = new ArrayList<Vehicle>();
    ArrayList<Object> ObjList = new ArrayList<Object>();
    Object obj;

    int UserInput = 1;

    DefaultListModel DLM = new DefaultListModel();
    DefaultComboBoxModel DCM = new DefaultComboBoxModel();
    JComboBox<String> FrameComboBox = new JComboBox<String>(DCM);
    JList<String> FrameList = new JList<String>(DLM);
    JScrollPane FLScrollPane = new JScrollPane(FrameList);
    JButton AddBtn = new JButton("Add");
    JButton RemoveBtn = new JButton("Remove");
    JButton ChangeBtn = new JButton("Change");
    JButton CloseBtn = new JButton("Exit");
    JButton SaveBtn = new JButton("Save");
    JButton OpenBtn = new JButton("Open");
    JButton ResetBtn = new JButton("Reset");
    DefaultComboBoxModel PCBDCM = new DefaultComboBoxModel();
    JComboBox PluginsComboBox = new JComboBox(PCBDCM);

    JFileChooser fileopen = new JFileChooser("C:\\Users\\Ваня\\IdeaProjects\\Laba1");

    MainWindow(){
        setSize(1120, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Автопарк с различными типами движущихся средств");
        setLayout(null);
        setLocationRelativeTo(null);
        setResizable(false);

        Container a = this.getContentPane();
        a.setBackground(Color.pink);

        CloseBtn.setBackground(Color.LIGHT_GRAY);

        FLScrollPane.setBounds(20,20,900,460);
        FrameList.setLayoutOrientation(JList.VERTICAL);

        AddBtn.setBounds(20, 500, 80, 50);
        OpenBtn.setBounds(960,400,100,50);
        SaveBtn.setBounds(960,470,100,50);
        ResetBtn.setBounds(960,540,100,50);
        RemoveBtn.setBounds(120, 500, 80, 50);
        ChangeBtn.setBounds(220, 500, 80, 50);
        CloseBtn.setBounds(480,500,80,50);
        PluginsComboBox.setBounds(740, 500, 80, 50);

        FrameComboBox.setBounds(20, 560, 80, 30);
        DCM.addElement("Vehicle");
        DCM.addElement("Lorry");
        DCM.addElement("Limousine");
        DCM.addElement("Saloon");
        DCM.addElement("SportCar");
        DCM.addElement("SUV");
        //DCM.addElement("LuxeFeature");

        DefaultComboBoxModel dcm_ser = new DefaultComboBoxModel();
        dcm_ser.addElement("binary");
        dcm_ser.addElement("XML");
        dcm_ser.addElement("text");
        JComboBox<String> FrameComboBox_ser = new JComboBox<String>(dcm_ser);
        FrameComboBox_ser.setBounds(840,500,80,50);

        add(FLScrollPane);
        add(AddBtn);
        add(OpenBtn);
        add(SaveBtn);
        add(ResetBtn);
        add(RemoveBtn);
        add(ChangeBtn);
        add(FrameComboBox);
        add(FrameComboBox_ser);
        add(CloseBtn);
        add(PluginsComboBox);

        ResetBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DLM.removeAllElements();
                while (VehicleList.size()!=0){
                    VehicleList.remove(0);
                }
                repaint();
            }
        });


        /////////////////
        //plugins loadind
        /////////////////
        File pluginsDir = new File("C:\\Users\\Ваня\\IdeaProjects\\Laba1\\out\\plugins1");
        File[] jars = pluginsDir.listFiles(new FileFilter() {
            public boolean accept(File FILE) {
                return FILE.isFile() && FILE.getName().endsWith(".jar");
            }
        });
        //create URLClassLoader for every jar in jars
        /////////////////////////////////////////////
        Class[] pluginClasses = new Class[jars.length];
        for(int i = 0; i < jars.length; i++){
            try{
                URL jarURL = jars[i].toURI().toURL();
                URLClassLoader classLoader = new URLClassLoader(new URL[]{jarURL});
                pluginClasses[i] = classLoader.loadClass(jars[i].getName().substring(0, jars[i].getName().lastIndexOf('.')).toLowerCase());
            }
            catch(MalformedURLException e){ e.printStackTrace(); }
            catch (ClassNotFoundException ex){ ex.printStackTrace(); }
            catch(Exception exp){ exp.printStackTrace(); }
        }
////////////////////////////////////////////////////////////////////////////////////////////
        for(Class clazz : pluginClasses){
            try{
                PCBDCM.addElement(clazz.getName());
                System.out.println(clazz.getName());
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        /* сериализация */
        SaveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final String filename;
                int ind = dcm_ser.getIndexOf(dcm_ser.getSelectedItem());
                switch (ind){
                    case (0)://binary
                        filename = "serialization.bin";
                        //****************************binary**********************************************
                        try {
                            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));
                            oos.writeObject(VehicleList);
                            oos.flush();
                            oos.close();

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            System.out.println("Problem with binary serialization found");
                        }
                        break;
                    case (1)://XML
                        filename = "serialization.xml";
                        //******************************XML**********************************************
                        try {
                            XMLEncoder xmlEncoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(filename)));

                            xmlEncoder.writeObject(VehicleList);

                            xmlEncoder.flush();
                            xmlEncoder.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            System.out.println("Problem with XML serialization found");
                        }
                        break;
                    case (2)://text
                        filename = "serialization.txt";
                        //****************************text************************************************
                        try {
                            PrintWriter writer = new PrintWriter(filename);
                            for (int i = 0; i < VehicleList.size(); i++){
                                writer.println("Class");
                                writer.println(VehicleList.get(i).getClass().getName());
                                Field[] fields;
                                Field[] s_fields;
                                fields = VehicleList.get(i).getClass().getDeclaredFields();
                                s_fields = VehicleList.get(i).getClass().getSuperclass().getDeclaredFields();
                                for (Field field : fields){
                                    writer.println("Field");
                                    field.setAccessible(true);
                                    if (!(field.getName().equals("luxeFeature"))){
                                        writer.println(field.getName());
                                        writer.println(field.get(VehicleList.get(i)));
                                    }
                                    else{
                                        writer.println("luxeFeature");
                                        Method method = VehicleList.get(i).getClass().getMethod("getLuxName");
                                        Method meth = VehicleList.get(i).getClass().getMethod("getLuxePriceUp");

                                        writer.println(method.invoke(VehicleList.get(i)));
                                        writer.println(meth.invoke(VehicleList.get(i)));
                                    }
                                }
                                for (Field field : s_fields){
                                    writer.println("SField");
                                    field.setAccessible(true);
                                    writer.println(field.getName());
                                    writer.println(field.get(VehicleList.get(i)));
                                }
                            }
                            writer.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            System.out.println("Problem with text serialization found");
                        }
                        break;
                }
                JOptionPane.showMessageDialog(null,"Данные успешно сохранены!","Внимание!", JOptionPane.INFORMATION_MESSAGE);

                if (UserInput != 1) {
                    String plugName = PluginsComboBox.getSelectedItem().toString();
                    for (Class clazz : pluginClasses) {
                        try {
                            if (clazz.getName().equals(plugName)) {
                                Plugin plugin = (Plugin) clazz.newInstance();
                                if (fileopen.getSelectedFile().getName().endsWith('.' + plugName)) {
                                    String newName = fileopen.getSelectedFile().getAbsolutePath();
                                    newName = newName.substring(0, newName.lastIndexOf('.'));
                                    fileopen.getSelectedFile().renameTo(new File(newName));
                                    fileopen.showDialog(null, "Save as");
                                }
                                System.out.println(fileopen.getSelectedFile().getName());

                                //plugin.processing(fileopen.getSelectedFile().getAbsoluteFile());
                                break;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });


        /* десериализация */
        OpenBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (UserInput !=1) {
                    String plugName = PluginsComboBox.getSelectedItem().toString();
                    for (Class clazz : pluginClasses) {
                        try {
                            if (clazz.getName().equals(plugName)) {
                                Plugin plugin = (Plugin) clazz.newInstance();
                                if (fileopen.getSelectedFile().getName().endsWith('.' + plugName))
                                    //plugin.processing(fileopen.getSelectedFile());
                                    break;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                final String filename;
                int ind = dcm_ser.getIndexOf(dcm_ser.getSelectedItem());
                switch (ind){
                    case (0):
                        //****************************binary**********************************************
                        filename = "serialization.bin";
                        try {
                            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));

                            DLM.removeAllElements();
                            while (VehicleList.size()!=0){
                                VehicleList.remove(0);
                            }
                            VehicleList=(ArrayList<Vehicle>)ois.readObject();
                            ois.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        break;
                    case (1):
                        //*****************************XML************************************************
                        filename = "serialization.xml";
                        try {
                            XMLDecoder xmlDecoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(filename)));
                            DLM.removeAllElements();
                            while (VehicleList.size()!=0){
                                VehicleList.remove(0);
                            }

                            VehicleList =(ArrayList<Vehicle>)xmlDecoder.readObject();

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            System.out.println("Problem with XML deserialization found");
                        }
                        break;
                    case (2):
                        //****************************text************************************************
                        filename = "serialization.txt";
                        try {
                            Deserializer deserializer = new Deserializer();
                            Method method = deserializer.getClass().getMethod("text", String.class, ArrayList.class);
                            VehicleList.clear();
                            VehicleList = (ArrayList<Vehicle>) method.invoke(deserializer, filename, VehicleList);
                            //fileopen.getSelectedFile().delete();
                            DLM.clear();
                        }
                        catch(Exception ex) {
                            System.out.println("Problem with text serialization found");
                        }
                        break;
                }

                    int index=100;
                    for (Vehicle vehicle : VehicleList){
                        if (vehicle instanceof Vehicle) index = 0;
                        if (vehicle instanceof Lorry) index = 1;
                        if (vehicle instanceof Limousine) index = 2;
                        if (vehicle instanceof Saloon) index = 3;
                        if (vehicle instanceof SportCar) index = 4;
                        if (vehicle instanceof SUV) index = 5;

                        switch (index){
                            case (0)://Vehicle
                                String addedStr = "Vehicle:      " + "Price: " + vehicle.getPrice() + ", "
                                        + "Fuel: " + vehicle.getFuel() + ", " + "Mileage: " + vehicle.getMileage();

                                DLM.addElement(addedStr);
                                repaint();
                                break;
                            case (1)://Lorry
                                addedStr = "Lorry:      " + "Price: " +vehicle.getPrice() + ", "
                                        + "Fuel: " + vehicle.getFuel() + ", " + "Mileage: " + vehicle.getMileage()
                                        + ", " + "Cargo: " + ((Lorry) vehicle).getCargo();

                                DLM.addElement(addedStr);
                                repaint();
                                break;
                            case (2)://Limousine
                                addedStr = "Limousine:      " + "Price: " + vehicle.getPrice() + ", "
                                        + "Fuel: " + vehicle.getFuel() + ", " + "Mileage: " + vehicle.getMileage()
                                        + ", " + "Acceleration: " + ((Limousine) vehicle).getAcceleration() + ", DriveWheel: " + ((Limousine) vehicle).getDriveWheel() + ", "
                                        + "Places: " + ((Limousine) vehicle).getPlaces() + ", LuxeFeature name: " + ((Limousine) vehicle).getLuxName()
                                        + ", LuxeFeature priceUp: " + ((Limousine) vehicle).getLuxePriceUp();

                                DLM.addElement(addedStr);
                                repaint();
                                break;
                            case (3)://Saloon
                                addedStr = "Saloon:      " + "Price: " + vehicle.getPrice() + ", "
                                        + "Fuel: " + vehicle.getFuel() + ", " + "Mileage: " + vehicle.getMileage()
                                        + ", " + "Acceleration: " + ((Saloon) vehicle).getAcceleration() + ", DriveWheel: " + ((Saloon) vehicle).getDriveWheel() + ", "
                                        + "Electro: " + ((Saloon) vehicle).isElectro() + ", LuxeFeature name: " + ((Saloon) vehicle).getLuxName()
                                        + ", LuxeFeature priceUp: " + ((Saloon) vehicle).getLuxePriceUp();

                                DLM.addElement(addedStr);
                                repaint();
                                break;
                            case (4)://SportCar
                                addedStr = "SportCar:      " + "Price: " + vehicle.getPrice() + ", "
                                        + "Fuel: " + vehicle.getFuel() + ", " + "Mileage: " + vehicle.getMileage()
                                        + ", " + "Acceleration: " + ((SportCar) vehicle).getAcceleration() + ", DriveWheel: " + ((SportCar) vehicle).getDriveWheel() + ", "
                                        + "Cabriolet: " + ((SportCar) vehicle).isCabriolet();

                                DLM.addElement(addedStr);
                                repaint();
                                break;
                            case (5)://SUV
                                addedStr = "SUV:      " + "Price: " + vehicle.getPrice() + ", "
                                        + "Fuel: " + vehicle.getFuel() + ", " + "Mileage: " + vehicle.getMileage()
                                        + ", " + "Acceleration: " + ((SUV) vehicle).getAcceleration() + ", DriveWheel: " + ((SUV) vehicle).getDriveWheel() + ", "
                                        + "Clearance: " + ((SUV) vehicle).getClearance();

                                DLM.addElement(addedStr);
                                repaint();
                                break;
                        }
                    }

                JOptionPane.showMessageDialog(null,"Данные успешно открыты!","Внимание!", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        AddBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddBtn.setVisible(false);
                RemoveBtn.setVisible(false);
                ChangeBtn.setVisible(false);

                int index = DCM.getIndexOf(DCM.getSelectedItem());
                switch (index){
                    case (0)://Vehicle
                        int x = 960;
                        int y = 20;
                        int width = 100;
                        int height = 30;

                        ObjList.add(new JTextField("Price"));
                        ObjList.add(new JTextField("Fuel"));
                        ObjList.add(new JTextField("Mileage"));
                        ObjList.add(new JButton("OK"));

                        for(int i = 0; i < 3; i++) {
                            obj = (JTextField)ObjList.get(i);
                            ((JTextField) obj).setBounds(x, y, width, height);

                            y += 40;
                            add((JTextField)obj);
                        }

                        obj = (JButton)ObjList.get(3);
                        ((JButton) obj).setBounds(x, y, width, height);
                        ((JButton)obj).addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                try {
                                    Vehicle vehicle = new Vehicle(Integer.parseInt(((JTextField)ObjList.get(0)).getText()),
                                            Integer.parseInt(((JTextField)ObjList.get(1)).getText()),
                                            Integer.parseInt(((JTextField)ObjList.get(2)).getText()));

                                    VehicleList.add(vehicle);

                                    String addedStr = "Vehicle:      " + "Price: " + ((JTextField)ObjList.get(0)).getText() + ", "
                                            + "Fuel: " + ((JTextField)ObjList.get(1)).getText() + ", " + "Mileage: " + ((JTextField)ObjList.get(2)).getText();

                                    DLM.addElement(addedStr);

                                    for(int i = 0; i < 3; i++){
                                        obj = ObjList.get(i);
                                        remove((JTextField)obj);
                                    }

                                    obj = (JButton)ObjList.get(3);
                                    remove((JButton)obj);

                                    AddBtn.setVisible(true);
                                    RemoveBtn.setVisible(true);
                                    ChangeBtn.setVisible(true);
                                    ObjList.clear();
                                    repaint();
                                }
                                catch (Exception ex){
                                    System.out.println("Exception 0");
                                    JOptionPane.showMessageDialog(null,"Проверьте введенные данные!","Внимание!", JOptionPane.INFORMATION_MESSAGE);
                                }
                            }
                        });
                        add((JButton)obj);

                        repaint();
                        break;
                    case (1)://Lorry
                        x = 960;
                        y = 20;
                        width = 100;
                        height = 30;

                        ObjList.add(new JTextField("Price"));
                        ObjList.add(new JTextField("Fuel"));
                        ObjList.add(new JTextField("Mileage"));
                        ObjList.add(new JTextField("Cargo"));
                        ObjList.add(new JButton("OK"));

                        for(int i = 0; i < 4; i++) {
                            obj = (JTextField)ObjList.get(i);
                            ((JTextField) obj).setBounds(x, y, width, height);

                            y += 40;
                            add((JTextField)obj);
                        }

                        obj = (JButton)ObjList.get(4);
                        ((JButton) obj).setBounds(x, y, width, height);
                        ((JButton)obj).addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                try {
                                    Lorry lorry = new Lorry(Integer.parseInt(((JTextField)ObjList.get(0)).getText()),
                                            Integer.parseInt(((JTextField)ObjList.get(1)).getText()),
                                            Integer.parseInt(((JTextField)ObjList.get(2)).getText()),
                                            Integer.parseInt(((JTextField)ObjList.get(3)).getText()));

                                    VehicleList.add(lorry);

                                    String addedStr = "Lorry:      " + "Price: " + ((JTextField)ObjList.get(0)).getText() + ", "
                                            + "Fuel: " + ((JTextField)ObjList.get(1)).getText() + ", " + "Mileage: " + ((JTextField)ObjList.get(2)).getText()
                                            + ", " + "Cargo: " + ((JTextField)ObjList.get(3)).getText();

                                    DLM.addElement(addedStr);

                                    for(int i = 0; i < 4; i++){
                                        obj = ObjList.get(i);
                                        remove((JTextField)obj);
                                    }

                                    obj = (JButton)ObjList.get(4);
                                    remove((JButton)obj);

                                    AddBtn.setVisible(true);
                                    RemoveBtn.setVisible(true);
                                    ChangeBtn.setVisible(true);
                                    ObjList.clear();
                                    repaint();
                                }
                                catch (Exception ex){
                                    System.out.println("Exception 0");
                                    JOptionPane.showMessageDialog(null,"Проверьте введенные данные!","Внимание!", JOptionPane.INFORMATION_MESSAGE);
                                }
                            }
                        });
                        add((JButton)obj);

                        repaint();
                        break;
                    case (2)://Limousine
                        x = 960;
                        y = 20;
                        width = 100;
                        height = 30;

                        DefaultComboBoxModel dcms = new DefaultComboBoxModel();
                        ObjList.add(new JTextField("Price"));
                        ObjList.add(new JTextField("Fuel"));
                        ObjList.add(new JTextField("Mileage"));
                        ObjList.add(new JTextField("Acceleration"));
                        ObjList.add(new JTextField("LuxeFeature name"));
                        ObjList.add(new JTextField("LuxeFeature priceUp"));
                        ObjList.add(new JTextField("places"));//6 если с 0
                        ObjList.add(new JComboBox<String>(dcms));//7
                        ObjList.add(new JButton("OK"));//8

                        for(int i = 0; i < 7; i++) {
                            obj = (JTextField)ObjList.get(i);
                            ((JTextField) obj).setBounds(x, y, width, height);

                            y += 40;
                            add((JTextField)obj);
                        }

                        obj = (JComboBox)ObjList.get(7);
                        ((JComboBox) obj).setBounds(x, y, width, height);
                        y += 40;
                        dcms.addElement("front");
                        dcms.addElement("rear");
                        dcms.addElement("full");
                        add((JComboBox)obj);

                        obj = (JButton)ObjList.get(8);
                        ((JButton) obj).setBounds(x, y, width, height);
                        ((JButton)obj).addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                try {
                                    DriveWheel DRIVEWHEEL;
                                    String driveWheel;
                                    int ind = dcms.getIndexOf(dcms.getSelectedItem());
                                    if (ind==0){
                                        driveWheel = "front";
                                        DRIVEWHEEL = DriveWheel.FRONT;
                                    }
                                    else if (ind==1){
                                        driveWheel = "rear";
                                        DRIVEWHEEL = DriveWheel.REAR;
                                    }
                                    else {
                                        driveWheel = "full";
                                        DRIVEWHEEL = DriveWheel.FULL;
                                    }

                                    Limousine limousine = new Limousine(Integer.parseInt(((JTextField)ObjList.get(0)).getText()),
                                            Integer.parseInt(((JTextField)ObjList.get(1)).getText()),
                                            Integer.parseInt(((JTextField)ObjList.get(2)).getText()),
                                            Float.parseFloat(((JTextField)ObjList.get(3)).getText()),
                                            DRIVEWHEEL, Integer.parseInt(((JTextField)ObjList.get(6)).getText()),
                                            ((JTextField)ObjList.get(4)).getText(),
                                            Integer.parseInt(((JTextField)ObjList.get(5)).getText())
                                            );

                                    VehicleList.add(limousine);

                                    String addedStr = "Limousine:      " + "Price: " + ((JTextField)ObjList.get(0)).getText() + ", "
                                            + "Fuel: " + ((JTextField)ObjList.get(1)).getText() + ", " + "Mileage: " + ((JTextField)ObjList.get(2)).getText()
                                            + ", " + "Acceleration: " + ((JTextField)ObjList.get(3)).getText() + ", DriveWheel: " + driveWheel + ", "
                                            + "Places: " + ((JTextField)ObjList.get(6)).getText() + ", LuxeFeature name: " + ((JTextField)ObjList.get(4)).getText()
                                    + ", LuxeFeature priceUp: " + ((JTextField)ObjList.get(5)).getText();

                                    DLM.addElement(addedStr);

                                    for(int i = 0; i < 7; i++){
                                        obj = ObjList.get(i);
                                        remove((JTextField)obj);
                                    }

                                    obj = (JComboBox)ObjList.get(7);
                                    remove((JComboBox)obj);

                                    obj = (JButton)ObjList.get(8);
                                    remove((JButton)obj);

                                    AddBtn.setVisible(true);
                                    RemoveBtn.setVisible(true);
                                    ChangeBtn.setVisible(true);
                                    ObjList.clear();
                                    repaint();
                                }
                                catch (Exception ex){
                                    System.out.println("Exception 0");
                                    JOptionPane.showMessageDialog(null,"Проверьте введенные данные!","Внимание!", JOptionPane.INFORMATION_MESSAGE);
                                }
                            }
                        });
                        add((JButton)obj);

                        repaint();
                        break;
                    case (3)://Saloon
                        x = 960;
                        y = 20;
                        width = 100;
                        height = 30;

                        dcms = new DefaultComboBoxModel();
                        DefaultComboBoxModel dcms1 = new DefaultComboBoxModel();
                        ObjList.add(new JTextField("Price"));
                        ObjList.add(new JTextField("Fuel"));
                        ObjList.add(new JTextField("Mileage"));
                        ObjList.add(new JTextField("Acceleration"));
                        ObjList.add(new JTextField("LuxeFeature name"));
                        ObjList.add(new JTextField("LuxeFeature priceUp"));
                        ObjList.add(new JComboBox<String>(dcms));//6 если с 0
                        ObjList.add(new JComboBox<String>(dcms1));//7
                        ObjList.add(new JButton("OK"));//8

                        for(int i = 0; i < 6; i++) {
                            obj = (JTextField)ObjList.get(i);
                            ((JTextField) obj).setBounds(x, y, width, height);

                            y += 40;
                            add((JTextField)obj);
                        }

                        obj = (JComboBox)ObjList.get(7);
                        ((JComboBox) obj).setBounds(x, y, width, height);
                        y += 40;
                        dcms1.addElement("electro");
                        dcms1.addElement("non-electro");
                        add((JComboBox)obj);

                        obj = (JComboBox)ObjList.get(6);
                        ((JComboBox) obj).setBounds(x, y, width, height);
                        y += 40;
                        dcms.addElement("front");
                        dcms.addElement("rear");
                        dcms.addElement("full");
                        add((JComboBox)obj);

                        obj = (JButton)ObjList.get(8);
                        ((JButton) obj).setBounds(x, y, width, height);
                        ((JButton)obj).addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                try {
                                    DriveWheel DRIVEWHEEL;
                                    String driveWheel;
                                    int ind = dcms.getIndexOf(dcms.getSelectedItem());
                                    if (ind==0){
                                        driveWheel = "front";
                                        DRIVEWHEEL = DriveWheel.FRONT;
                                    }
                                    else if (ind==1){
                                        driveWheel = "rear";
                                        DRIVEWHEEL = DriveWheel.REAR;
                                    }
                                    else {
                                        driveWheel = "full";
                                        DRIVEWHEEL = DriveWheel.FULL;
                                    }

                                    boolean el;
                                    String ELECTRO;
                                    ind = dcms1.getIndexOf(dcms1.getSelectedItem());
                                    if (ind==0) {
                                        el = true;
                                        ELECTRO = "electro";
                                    }
                                    else  {
                                        el = false;
                                        ELECTRO = "non-electro";
                                    }

                                    Saloon saloon = new Saloon(Integer.parseInt(((JTextField)ObjList.get(0)).getText()),
                                            Integer.parseInt(((JTextField)ObjList.get(1)).getText()),
                                            Integer.parseInt(((JTextField)ObjList.get(2)).getText()),
                                            Float.parseFloat(((JTextField)ObjList.get(3)).getText()),
                                            DRIVEWHEEL, el,
                                            ((JTextField)ObjList.get(4)).getText(),
                                            Integer.parseInt(((JTextField)ObjList.get(5)).getText())
                                    );

                                    VehicleList.add(saloon);

                                    String addedStr = "Saloon:      " + "Price: " + ((JTextField)ObjList.get(0)).getText() + ", "
                                            + "Fuel: " + ((JTextField)ObjList.get(1)).getText() + ", " + "Mileage: " + ((JTextField)ObjList.get(2)).getText()
                                            + ", " + "Acceleration: " + ((JTextField)ObjList.get(3)).getText() + ", DriveWheel: " + driveWheel + ", "
                                            + "Electro: " + ELECTRO + ", LuxeFeature name: " + ((JTextField)ObjList.get(4)).getText()
                                            + ", LuxeFeature priceUp: " + ((JTextField)ObjList.get(5)).getText();

                                    DLM.addElement(addedStr);

                                    for(int i = 0; i < 6; i++){
                                        obj = ObjList.get(i);
                                        remove((JTextField)obj);
                                    }

                                    obj = (JComboBox)ObjList.get(6);
                                    remove((JComboBox)obj);

                                    obj = (JComboBox)ObjList.get(7);
                                    remove((JComboBox)obj);

                                    obj = (JButton)ObjList.get(8);
                                    remove((JButton)obj);

                                    AddBtn.setVisible(true);
                                    RemoveBtn.setVisible(true);
                                    ChangeBtn.setVisible(true);
                                    ObjList.clear();
                                    repaint();
                                }
                                catch (Exception ex){
                                    System.out.println("Exception 0");
                                    JOptionPane.showMessageDialog(null,"Проверьте введенные данные!","Внимание!", JOptionPane.INFORMATION_MESSAGE);
                                }
                            }
                        });
                        add((JButton)obj);

                        repaint();
                        break;
                    case (4)://SportCar
                        x = 960;
                        y = 20;
                        width = 100;
                        height = 30;

                        dcms = new DefaultComboBoxModel();
                        dcms1 = new DefaultComboBoxModel();
                        ObjList.add(new JTextField("Price"));
                        ObjList.add(new JTextField("Fuel"));
                        ObjList.add(new JTextField("Mileage"));
                        ObjList.add(new JTextField("Acceleration"));
                        ObjList.add(new JComboBox<String>(dcms));
                        ObjList.add(new JComboBox<String>(dcms1));
                        ObjList.add(new JButton("OK"));//8

                        for(int i = 0; i < 4; i++) {
                            obj = (JTextField)ObjList.get(i);
                            ((JTextField) obj).setBounds(x, y, width, height);

                            y += 40;
                            add((JTextField)obj);
                        }

                        obj = (JComboBox)ObjList.get(5);
                        ((JComboBox) obj).setBounds(x, y, width, height);
                        y += 40;
                        dcms1.addElement("cabriolet");
                        dcms1.addElement("non-cabriolet");
                        add((JComboBox)obj);

                        obj = (JComboBox)ObjList.get(4);
                        ((JComboBox) obj).setBounds(x, y, width, height);
                        y += 40;
                        dcms.addElement("front");
                        dcms.addElement("rear");
                        dcms.addElement("full");
                        add((JComboBox)obj);

                        obj = (JButton)ObjList.get(6);
                        ((JButton) obj).setBounds(x, y, width, height);
                        ((JButton)obj).addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                try {
                                    DriveWheel DRIVEWHEEL;
                                    String driveWheel;
                                    int ind = dcms.getIndexOf(dcms.getSelectedItem());
                                    if (ind==0){
                                        driveWheel = "front";
                                        DRIVEWHEEL = DriveWheel.FRONT;
                                    }
                                    else if (ind==1){
                                        driveWheel = "rear";
                                        DRIVEWHEEL = DriveWheel.REAR;
                                    }
                                    else {
                                        driveWheel = "full";
                                        DRIVEWHEEL = DriveWheel.FULL;
                                    }

                                    boolean ca;
                                    String CABRIOLET;
                                    ind = dcms1.getIndexOf(dcms1.getSelectedItem());
                                    if (ind==0) {
                                        ca = true;
                                        CABRIOLET = "cabriolet";
                                    }
                                    else  {
                                        ca = false;
                                        CABRIOLET = "non-cabriolet";
                                    }

                                    SportCar sportCar = new SportCar(Integer.parseInt(((JTextField)ObjList.get(0)).getText()),
                                            Integer.parseInt(((JTextField)ObjList.get(1)).getText()),
                                            Integer.parseInt(((JTextField)ObjList.get(2)).getText()),
                                            Float.parseFloat(((JTextField)ObjList.get(3)).getText()),
                                            DRIVEWHEEL, ca);

                                    VehicleList.add(sportCar);

                                    String addedStr = "SportCar:      " + "Price: " + ((JTextField)ObjList.get(0)).getText() + ", "
                                            + "Fuel: " + ((JTextField)ObjList.get(1)).getText() + ", " + "Mileage: " + ((JTextField)ObjList.get(2)).getText()
                                            + ", " + "Acceleration: " + ((JTextField)ObjList.get(3)).getText() + ", DriveWheel: " + driveWheel + ", "
                                            + "Cabriolet: " + CABRIOLET;

                                    DLM.addElement(addedStr);

                                    for(int i = 0; i < 4; i++){
                                        obj = ObjList.get(i);
                                        remove((JTextField)obj);
                                    }

                                    obj = (JComboBox)ObjList.get(4);
                                    remove((JComboBox)obj);

                                    obj = (JComboBox)ObjList.get(5);
                                    remove((JComboBox)obj);

                                    obj = (JButton)ObjList.get(6);
                                    remove((JButton)obj);

                                    AddBtn.setVisible(true);
                                    RemoveBtn.setVisible(true);
                                    ChangeBtn.setVisible(true);
                                    ObjList.clear();
                                    repaint();
                                }
                                catch (Exception ex){
                                    System.out.println("Exception 0");
                                    JOptionPane.showMessageDialog(null,"Проверьте введенные данные!","Внимание!", JOptionPane.INFORMATION_MESSAGE);
                                }
                            }
                        });
                        add((JButton)obj);

                        repaint();
                        break;
                    case (5)://SUV
                        x = 960;
                        y = 20;
                        width = 100;
                        height = 30;

                        dcms = new DefaultComboBoxModel();
                        ObjList.add(new JTextField("Price"));
                        ObjList.add(new JTextField("Fuel"));
                        ObjList.add(new JTextField("Mileage"));
                        ObjList.add(new JTextField("Acceleration"));
                        ObjList.add(new JTextField("Clearance"));
                        ObjList.add(new JComboBox<String>(dcms));
                        ObjList.add(new JButton("OK"));//8

                        for(int i = 0; i < 5; i++) {
                            obj = (JTextField)ObjList.get(i);
                            ((JTextField) obj).setBounds(x, y, width, height);

                            y += 40;
                            add((JTextField)obj);
                        }

                        obj = (JComboBox)ObjList.get(5);
                        ((JComboBox) obj).setBounds(x, y, width, height);
                        y += 40;
                        dcms.addElement("front");
                        dcms.addElement("rear");
                        dcms.addElement("full");
                        add((JComboBox)obj);

                        obj = (JButton)ObjList.get(6);
                        ((JButton) obj).setBounds(x, y, width, height);
                        ((JButton)obj).addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                try {
                                    DriveWheel DRIVEWHEEL;
                                    String driveWheel;
                                    int ind = dcms.getIndexOf(dcms.getSelectedItem());
                                    if (ind==0){
                                        driveWheel = "front";
                                        DRIVEWHEEL = DriveWheel.FRONT;
                                    }
                                    else if (ind==1){
                                        driveWheel = "rear";
                                        DRIVEWHEEL = DriveWheel.REAR;
                                    }
                                    else {
                                        driveWheel = "full";
                                        DRIVEWHEEL = DriveWheel.FULL;
                                    }

                                    SUV suv = new SUV(Integer.parseInt(((JTextField)ObjList.get(0)).getText()),
                                            Integer.parseInt(((JTextField)ObjList.get(1)).getText()),
                                            Integer.parseInt(((JTextField)ObjList.get(2)).getText()),
                                            Float.parseFloat(((JTextField)ObjList.get(3)).getText()),
                                            DRIVEWHEEL, Integer.parseInt(((JTextField)ObjList.get(4)).getText()));

                                    VehicleList.add(suv);

                                    String addedStr = "SUV:      " + "Price: " + ((JTextField)ObjList.get(0)).getText() + ", "
                                            + "Fuel: " + ((JTextField)ObjList.get(1)).getText() + ", " + "Mileage: " + ((JTextField)ObjList.get(2)).getText()
                                            + ", " + "Acceleration: " + ((JTextField)ObjList.get(3)).getText() + ", DriveWheel: " + driveWheel + ", "
                                            + "Clearance: " + ((JTextField)ObjList.get(4)).getText();

                                    DLM.addElement(addedStr);

                                    for(int i = 0; i < 5; i++){
                                        obj = ObjList.get(i);
                                        remove((JTextField)obj);
                                    }

                                    obj = (JComboBox)ObjList.get(5);
                                    remove((JComboBox)obj);

                                    obj = (JButton)ObjList.get(6);
                                    remove((JButton)obj);

                                    AddBtn.setVisible(true);
                                    RemoveBtn.setVisible(true);
                                    ChangeBtn.setVisible(true);
                                    ObjList.clear();
                                    repaint();
                                }
                                catch (Exception ex){
                                    System.out.println("Exception 0");
                                    JOptionPane.showMessageDialog(null,"Проверьте введенные данные!","Внимание!", JOptionPane.INFORMATION_MESSAGE);
                                }
                            }
                        });
                        add((JButton)obj);

                        repaint();
                        break;
                    /*case (6)://LuxeFeature
                        x = 960;
                        y = 20;
                        width = 100;
                        height = 30;

                        ObjList.add(new JTextField("Name"));
                        ObjList.add(new JTextField("Price-up"));
                        ObjList.add(new JButton("OK"));

                        for(int i = 0; i < 2; i++) {
                            obj = (JTextField)ObjList.get(i);
                            ((JTextField) obj).setBounds(x, y, width, height);

                            y += 40;
                            add((JTextField)obj);
                        }

                        obj = (JButton)ObjList.get(2);
                        ((JButton) obj).setBounds(x, y, width, height);
                        ((JButton)obj).addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                try {
                                    LuxeFeatures luxeFeature = new LuxeFeatures(((JTextField)ObjList.get(0)).getText(), Integer.parseInt(((JTextField)ObjList.get(1)).getText()));
                                    VehicleList.add(luxeFeature);

                                    String addedStr = "LuxeFeature:      " + "Name: " + ((JTextField)ObjList.get(0)).getText() + ", "
                                            + "Price-up: " + ((JTextField)ObjList.get(1)).getText();

                                    DLM.addElement(addedStr);

                                    for(int i = 0; i < 2; i++){
                                        obj = ObjList.get(i);
                                        remove((JTextField)obj);
                                    }

                                    obj = (JButton)ObjList.get(2);
                                    remove((JButton)obj);

                                    AddBtn.setVisible(true);
                                    RemoveBtn.setVisible(true);
                                    ChangeBtn.setVisible(true);
                                    ObjList.clear();
                                    repaint();
                                }
                                catch (Exception ex){
                                    System.out.println("Exception 0");
                                    JOptionPane.showMessageDialog(null,"Проверьте введенные данные!");
                                }
                            }
                        });
                        add((JButton)obj);

                        repaint();
                        break;*/
                }

            }
        });


            ChangeBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ChangeBtn.setVisible(false);
                    RemoveBtn.setVisible(false);
                    AddBtn.setVisible(false);

                    int IND = FrameList.getSelectedIndex();
                    //try {
                    int index = 100;
                    try {
                    obj = VehicleList.get(IND);
                    }
                    catch (Exception ex){
                        System.out.println("Change exception");
                        JOptionPane.showMessageDialog(null,"Вы не выбрали объект для редактирования","Внимание!", JOptionPane.INFORMATION_MESSAGE);
                        ChangeBtn.setVisible(true);
                        RemoveBtn.setVisible(true);
                        AddBtn.setVisible(true);
                    }

                    if (obj instanceof Vehicle) index = 0;
                    if (obj instanceof Lorry) index = 1;
                    if (obj instanceof Limousine) index = 2;
                    if (obj instanceof Saloon) index = 3;
                    if (obj instanceof SportCar) index = 4;
                    if (obj instanceof SUV) index = 5;
                    //if (obj instanceof LuxeFeatures) index = 6;

                    switch (index) {
                        case (0)://Vehicle
                            int x = 960;
                            int y = 20;
                            int width = 100;
                            int height = 30;

                            DefaultComboBoxModel dcm = new DefaultComboBoxModel();
                            ObjList.add(new JTextField(String.valueOf(((Vehicle) obj).getPrice())));
                            ObjList.add(new JTextField(String.valueOf(((Vehicle) obj).getFuel())));
                            ObjList.add(new JTextField(String.valueOf(((Vehicle) obj).getMileage())));
                            ObjList.add(new JButton("OK"));

                            obj = (JTextField) ObjList.get(0);
                            ((JTextField) obj).setToolTipText("Price");
                            obj = (JTextField) ObjList.get(1);
                            ((JTextField) obj).setToolTipText("Fuel");
                            obj = (JTextField) ObjList.get(2);
                            ((JTextField) obj).setToolTipText("Mileage");

                            for (int i = 0; i < 3; i++) {
                                obj = (JTextField) ObjList.get(i);
                                ((JTextField) obj).setBounds(x, y, width, height);

                                y += 40;
                                add((JTextField) obj);
                            }
                            obj = (JButton) ObjList.get(3);
                            ((JButton) obj).setBounds(x, y, width, height);
                            ((JButton) obj).addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    try {
                                        Vehicle vehicle = new Vehicle(Integer.parseInt(((JTextField) ObjList.get(0)).getText()),
                                                Integer.parseInt(((JTextField) ObjList.get(1)).getText()),
                                                Integer.parseInt(((JTextField) ObjList.get(2)).getText()));

                                        VehicleList.remove(IND);
                                        VehicleList.add(vehicle);

                                        String addedStr = "Vehicle: " + ((JTextField) ObjList.get(0)).getText() + ", "
                                                + ((JTextField) ObjList.get(1)).getText() + ", " + ((JTextField) ObjList.get(2)).getText();

                                        DLM.remove(IND);
                                        DLM.addElement(addedStr);

                                        for (int i = 0; i < 3; i++) {
                                            obj = ObjList.get(i);
                                            remove((JTextField) obj);
                                        }

                                        obj = (JButton) ObjList.get(3);
                                        remove((JButton) obj);

                                        AddBtn.setVisible(true);
                                        RemoveBtn.setVisible(true);
                                        ChangeBtn.setVisible(true);
                                        ObjList.clear();
                                        repaint();
                                    } catch (Exception ex) {
                                        System.out.println("Exception 0");
                                        JOptionPane.showMessageDialog(null,"Проверьте введенные данные!","Внимание!", JOptionPane.INFORMATION_MESSAGE);
                                    }
                                }
                            });
                            add((JButton) obj);

                            repaint();
                            break;
                        case (1)://Lorry
                            x = 960;
                            y = 20;
                            width = 100;
                            height = 30;

                            ObjList.add(new JTextField(String.valueOf(((Lorry) obj).getPrice())));
                            ObjList.add(new JTextField(String.valueOf(((Lorry) obj).getFuel())));
                            ObjList.add(new JTextField(String.valueOf(((Lorry) obj).getMileage())));
                            ObjList.add(new JTextField(String.valueOf(((Lorry) obj).getCargo())));
                            ObjList.add(new JButton("OK"));

                            obj = (JTextField) ObjList.get(0);
                            ((JTextField) obj).setToolTipText("Price");
                            obj = (JTextField) ObjList.get(1);
                            ((JTextField) obj).setToolTipText("Fuel");
                            obj = (JTextField) ObjList.get(2);
                            ((JTextField) obj).setToolTipText("Mileage");
                            obj = (JTextField) ObjList.get(3);
                            ((JTextField) obj).setToolTipText("Cargo");

                            for(int i = 0; i < 4; i++) {
                                obj = (JTextField)ObjList.get(i);
                                ((JTextField) obj).setBounds(x, y, width, height);

                                y += 40;
                                add((JTextField)obj);
                            }

                            obj = (JButton)ObjList.get(4);
                            ((JButton) obj).setBounds(x, y, width, height);
                            ((JButton)obj).addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    try {
                                        Lorry lorry = new Lorry(Integer.parseInt(((JTextField)ObjList.get(0)).getText()),
                                                Integer.parseInt(((JTextField)ObjList.get(1)).getText()),
                                                Integer.parseInt(((JTextField)ObjList.get(2)).getText()),
                                                Integer.parseInt(((JTextField)ObjList.get(3)).getText()));

                                        VehicleList.remove(IND);
                                        VehicleList.add(lorry);

                                        String addedStr = "Lorry:      " + "Price: " + ((JTextField)ObjList.get(0)).getText() + ", "
                                                + "Fuel: " + ((JTextField)ObjList.get(1)).getText() + ", " + "Mileage: " + ((JTextField)ObjList.get(2)).getText()
                                                + ", " + "Cargo: " + ((JTextField)ObjList.get(3)).getText();

                                        DLM.remove(IND);
                                        DLM.addElement(addedStr);

                                        for(int i = 0; i < 4; i++){
                                            obj = ObjList.get(i);
                                            remove((JTextField)obj);
                                        }

                                        obj = (JButton)ObjList.get(4);
                                        remove((JButton)obj);

                                        AddBtn.setVisible(true);
                                        RemoveBtn.setVisible(true);
                                        ChangeBtn.setVisible(true);
                                        ObjList.clear();
                                        repaint();
                                    }
                                    catch (Exception ex){
                                        System.out.println("Exception 0");
                                        JOptionPane.showMessageDialog(null,"Проверьте введенные данные!","Внимание!", JOptionPane.INFORMATION_MESSAGE);
                                    }
                                }
                            });
                            add((JButton)obj);

                            repaint();
                            break;
                        case (2)://Limousine
                            x = 960;
                            y = 20;
                            width = 100;
                            height = 30;

                            DefaultComboBoxModel dcms = new DefaultComboBoxModel();
                            ObjList.add(new JTextField(String.valueOf(((Limousine) obj).getPrice())));
                            ObjList.add(new JTextField(String.valueOf(((Limousine) obj).getFuel())));
                            ObjList.add(new JTextField(String.valueOf(((Limousine) obj).getMileage())));
                            ObjList.add(new JTextField(String.valueOf(((Limousine) obj).getAcceleration())));
                            ObjList.add(new JTextField(String.valueOf(((Limousine) obj).getLuxName())));
                            ObjList.add(new JTextField(String.valueOf(((Limousine) obj).getLuxePriceUp())));
                            ObjList.add(new JTextField(String.valueOf(((Limousine) obj).getPlaces())));
                            ObjList.add(new JComboBox<String>(dcms));//7
                            ObjList.add(new JButton("OK"));//8

                            obj = (JTextField)ObjList.get(0);
                            ((JTextField) obj).setToolTipText("Price");
                            obj = (JTextField)ObjList.get(1);
                            ((JTextField) obj).setToolTipText("Fuel");
                            obj = (JTextField)ObjList.get(2);
                            ((JTextField) obj).setToolTipText("Mileage");
                            obj = (JTextField)ObjList.get(3);
                            ((JTextField) obj).setToolTipText("Acceleration");
                            obj = (JTextField)ObjList.get(4);
                            ((JTextField) obj).setToolTipText("LuxeFeature name");
                            obj = (JTextField)ObjList.get(5);
                            ((JTextField) obj).setToolTipText("LuxeFeature price-up");
                            obj = (JTextField)ObjList.get(6);
                            ((JTextField) obj).setToolTipText("Places");
                            obj = (JComboBox)ObjList.get(7);
                            ((JComboBox) obj).setToolTipText("DriveWheel");


                            for(int i = 0; i < 7; i++) {
                                obj = (JTextField)ObjList.get(i);
                                ((JTextField) obj).setBounds(x, y, width, height);

                                y += 40;
                                add((JTextField)obj);
                            }

                            obj = (JComboBox)ObjList.get(7);
                            ((JComboBox) obj).setBounds(x, y, width, height);
                            y += 40;
                            dcms.addElement("front");
                            dcms.addElement("rear");
                            dcms.addElement("full");
                            add((JComboBox)obj);

                            obj = (JButton)ObjList.get(8);
                            ((JButton) obj).setBounds(x, y, width, height);
                            ((JButton)obj).addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    try {
                                        DriveWheel DRIVEWHEEL;
                                        String driveWheel;
                                        int ind = dcms.getIndexOf(dcms.getSelectedItem());
                                        if (ind==0){
                                            driveWheel = "front";
                                            DRIVEWHEEL = DriveWheel.FRONT;
                                        }
                                        else if (ind==1){
                                            driveWheel = "rear";
                                            DRIVEWHEEL = DriveWheel.REAR;
                                        }
                                        else {
                                            driveWheel = "full";
                                            DRIVEWHEEL = DriveWheel.FULL;
                                        }

                                        Limousine limousine = new Limousine(Integer.parseInt(((JTextField)ObjList.get(0)).getText()),
                                                Integer.parseInt(((JTextField)ObjList.get(1)).getText()),
                                                Integer.parseInt(((JTextField)ObjList.get(2)).getText()),
                                                Float.parseFloat(((JTextField)ObjList.get(3)).getText()),
                                                DRIVEWHEEL, Integer.parseInt(((JTextField)ObjList.get(6)).getText()),
                                                ((JTextField)ObjList.get(4)).getText(),
                                                Integer.parseInt(((JTextField)ObjList.get(5)).getText())
                                        );

                                        VehicleList.remove(IND);
                                        VehicleList.add(limousine);

                                        String addedStr = "Limousine:      " + "Price: " + ((JTextField)ObjList.get(0)).getText() + ", "
                                                + "Fuel: " + ((JTextField)ObjList.get(1)).getText() + ", " + "Mileage: " + ((JTextField)ObjList.get(2)).getText()
                                                + ", " + "Acceleration: " + ((JTextField)ObjList.get(3)).getText() + ", DriveWheel: " + driveWheel + ", "
                                                + "Places: " + ((JTextField)ObjList.get(6)).getText() + ", LuxeFeature name: " + ((JTextField)ObjList.get(4)).getText()
                                                + ", LuxeFeature priceUp: " + ((JTextField)ObjList.get(5)).getText();

                                        DLM.remove(IND);
                                        DLM.addElement(addedStr);

                                        for(int i = 0; i < 7; i++){
                                            obj = ObjList.get(i);
                                            remove((JTextField)obj);
                                        }

                                        obj = (JComboBox)ObjList.get(7);
                                        remove((JComboBox)obj);

                                        obj = (JButton)ObjList.get(8);
                                        remove((JButton)obj);

                                        AddBtn.setVisible(true);
                                        RemoveBtn.setVisible(true);
                                        ChangeBtn.setVisible(true);
                                        ObjList.clear();
                                        repaint();
                                    }
                                    //JOptionPane p = new JOptionPane();
                                    catch (Exception ex){
                                        System.out.println("Exception 0");
                                        JOptionPane.showMessageDialog(null,"Проверьте введенные данные!","Внимание!", JOptionPane.INFORMATION_MESSAGE);
                                    }
                                }
                            });
                            add((JButton)obj);

                            repaint();
                            break;
                        case (3)://Saloon
                            x = 960;
                            y = 20;
                            width = 100;
                            height = 30;

                            dcms = new DefaultComboBoxModel();
                            DefaultComboBoxModel dcms1 = new DefaultComboBoxModel();
                            ObjList.add(new JTextField(String.valueOf(((Saloon) obj).getPrice())));
                            ObjList.add(new JTextField(String.valueOf(((Saloon) obj).getFuel())));
                            ObjList.add(new JTextField(String.valueOf(((Saloon) obj).getMileage())));
                            ObjList.add(new JTextField(String.valueOf(((Saloon) obj).getAcceleration())));
                            ObjList.add(new JTextField(String.valueOf(((Saloon) obj).getLuxName())));
                            ObjList.add(new JTextField(String.valueOf(((Saloon) obj).getLuxePriceUp())));
                            ObjList.add(new JComboBox<String>(dcms));//6 если с 0
                            ObjList.add(new JComboBox<String>(dcms1));//7
                            ObjList.add(new JButton("OK"));//8

                            obj = (JTextField)ObjList.get(0);
                            ((JTextField) obj).setToolTipText("Price");
                            obj = (JTextField)ObjList.get(1);
                            ((JTextField) obj).setToolTipText("Fuel");
                            obj = (JTextField)ObjList.get(2);
                            ((JTextField) obj).setToolTipText("Mileage");
                            obj = (JTextField)ObjList.get(3);
                            ((JTextField) obj).setToolTipText("Acceleration");
                            obj = (JTextField)ObjList.get(4);
                            ((JTextField) obj).setToolTipText("LuxeFeature name");
                            obj = (JTextField)ObjList.get(5);
                            ((JTextField) obj).setToolTipText("LuxeFeature price-up");
                            obj = (JComboBox)ObjList.get(7);
                            ((JComboBox) obj).setToolTipText("electro");
                            obj = (JComboBox)ObjList.get(6);
                            ((JComboBox) obj).setToolTipText("DriveWheel");


                            for(int i = 0; i < 6; i++) {
                                obj = (JTextField)ObjList.get(i);
                                ((JTextField) obj).setBounds(x, y, width, height);

                                y += 40;
                                add((JTextField)obj);
                            }

                            obj = (JComboBox)ObjList.get(7);
                            ((JComboBox) obj).setBounds(x, y, width, height);
                            y += 40;
                            dcms1.addElement("electro");
                            dcms1.addElement("non-electro");
                            add((JComboBox)obj);

                            obj = (JComboBox)ObjList.get(6);
                            ((JComboBox) obj).setBounds(x, y, width, height);
                            y += 40;
                            dcms.addElement("front");
                            dcms.addElement("rear");
                            dcms.addElement("full");
                            add((JComboBox)obj);

                            obj = (JButton)ObjList.get(8);
                            ((JButton) obj).setBounds(x, y, width, height);
                            ((JButton)obj).addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    try {
                                        DriveWheel DRIVEWHEEL;
                                        String driveWheel;
                                        int ind = dcms.getIndexOf(dcms.getSelectedItem());
                                        if (ind==0){
                                            driveWheel = "front";
                                            DRIVEWHEEL = DriveWheel.FRONT;
                                        }
                                        else if (ind==1){
                                            driveWheel = "rear";
                                            DRIVEWHEEL = DriveWheel.REAR;
                                        }
                                        else {
                                            driveWheel = "full";
                                            DRIVEWHEEL = DriveWheel.FULL;
                                        }

                                        boolean el;
                                        String ELECTRO;
                                        ind = dcms1.getIndexOf(dcms1.getSelectedItem());
                                        if (ind==0) {
                                            el = true;
                                            ELECTRO = "electro";
                                        }
                                        else  {
                                            el = false;
                                            ELECTRO = "non-electro";
                                        }

                                        Saloon saloon = new Saloon(Integer.parseInt(((JTextField)ObjList.get(0)).getText()),
                                                Integer.parseInt(((JTextField)ObjList.get(1)).getText()),
                                                Integer.parseInt(((JTextField)ObjList.get(2)).getText()),
                                                Float.parseFloat(((JTextField)ObjList.get(3)).getText()),
                                                DRIVEWHEEL, el,
                                                ((JTextField)ObjList.get(4)).getText(),
                                                Integer.parseInt(((JTextField)ObjList.get(5)).getText())
                                        );

                                        VehicleList.remove(IND);
                                        VehicleList.add(saloon);

                                        String addedStr = "Saloon:      " + "Price: " + ((JTextField)ObjList.get(0)).getText() + ", "
                                                + "Fuel: " + ((JTextField)ObjList.get(1)).getText() + ", " + "Mileage: " + ((JTextField)ObjList.get(2)).getText()
                                                + ", " + "Acceleration: " + ((JTextField)ObjList.get(3)).getText() + ", DriveWheel: " + driveWheel + ", "
                                                + "Electro: " + ELECTRO + ", LuxeFeature name: " + ((JTextField)ObjList.get(4)).getText()
                                                + ", LuxeFeature priceUp: " + ((JTextField)ObjList.get(5)).getText();

                                        DLM.remove(IND);
                                        DLM.addElement(addedStr);

                                        for(int i = 0; i < 6; i++){
                                            obj = ObjList.get(i);
                                            remove((JTextField)obj);
                                        }

                                        obj = (JComboBox)ObjList.get(6);
                                        remove((JComboBox)obj);

                                        obj = (JComboBox)ObjList.get(7);
                                        remove((JComboBox)obj);

                                        obj = (JButton)ObjList.get(8);
                                        remove((JButton)obj);

                                        AddBtn.setVisible(true);
                                        RemoveBtn.setVisible(true);
                                        ChangeBtn.setVisible(true);
                                        ObjList.clear();
                                        repaint();
                                    }
                                    catch (Exception ex){
                                        System.out.println("Exception 0");
                                        JOptionPane.showMessageDialog(null,"Проверьте введенные данные!","Внимание!", JOptionPane.INFORMATION_MESSAGE);
                                    }
                                }
                            });
                            add((JButton)obj);

                            repaint();
                            break;
                        case (4)://SportCar
                            x = 960;
                            y = 20;
                            width = 100;
                            height = 30;

                            dcms = new DefaultComboBoxModel();
                            dcms1 = new DefaultComboBoxModel();
                            ObjList.add(new JTextField(String.valueOf(((SportCar) obj).getPrice())));
                            ObjList.add(new JTextField(String.valueOf(((SportCar) obj).getFuel())));
                            ObjList.add(new JTextField(String.valueOf(((SportCar) obj).getMileage())));
                            ObjList.add(new JTextField(String.valueOf(((SportCar) obj).getAcceleration())));
                            ObjList.add(new JComboBox<String>(dcms));
                            ObjList.add(new JComboBox<String>(dcms1));
                            ObjList.add(new JButton("OK"));

                            obj = (JTextField)ObjList.get(0);
                            ((JTextField) obj).setToolTipText("Price");
                            obj = (JTextField)ObjList.get(1);
                            ((JTextField) obj).setToolTipText("Fuel");
                            obj = (JTextField)ObjList.get(2);
                            ((JTextField) obj).setToolTipText("Mileage");
                            obj = (JTextField)ObjList.get(3);
                            ((JTextField) obj).setToolTipText("Acceleration");
                            obj = (JComboBox)ObjList.get(5);
                            ((JComboBox) obj).setToolTipText("Cabriolet");
                            obj = (JComboBox)ObjList.get(4);
                            ((JComboBox) obj).setToolTipText("DriveWheel");


                            for(int i = 0; i < 4; i++) {
                                obj = (JTextField)ObjList.get(i);
                                ((JTextField) obj).setBounds(x, y, width, height);

                                y += 40;
                                add((JTextField)obj);
                            }

                            obj = (JComboBox)ObjList.get(5);
                            ((JComboBox) obj).setBounds(x, y, width, height);
                            y += 40;
                            dcms1.addElement("cabriolet");
                            dcms1.addElement("non-cabriolet");
                            add((JComboBox)obj);

                            obj = (JComboBox)ObjList.get(4);
                            ((JComboBox) obj).setBounds(x, y, width, height);
                            y += 40;
                            dcms.addElement("front");
                            dcms.addElement("rear");
                            dcms.addElement("full");
                            add((JComboBox)obj);

                            obj = (JButton)ObjList.get(6);
                            ((JButton) obj).setBounds(x, y, width, height);
                            ((JButton)obj).addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    try {
                                        DriveWheel DRIVEWHEEL;
                                        String driveWheel;
                                        int ind = dcms.getIndexOf(dcms.getSelectedItem());
                                        if (ind==0){
                                            driveWheel = "front";
                                            DRIVEWHEEL = DriveWheel.FRONT;
                                        }
                                        else if (ind==1){
                                            driveWheel = "rear";
                                            DRIVEWHEEL = DriveWheel.REAR;
                                        }
                                        else {
                                            driveWheel = "full";
                                            DRIVEWHEEL = DriveWheel.FULL;
                                        }

                                        boolean ca;
                                        String CABRIOLET;
                                        ind = dcms1.getIndexOf(dcms1.getSelectedItem());
                                        if (ind==0) {
                                            ca = true;
                                            CABRIOLET = "cabriolet";
                                        }
                                        else  {
                                            ca = false;
                                            CABRIOLET = "non-cabriolet";
                                        }

                                        SportCar sportCar = new SportCar(Integer.parseInt(((JTextField)ObjList.get(0)).getText()),
                                                Integer.parseInt(((JTextField)ObjList.get(1)).getText()),
                                                Integer.parseInt(((JTextField)ObjList.get(2)).getText()),
                                                Float.parseFloat(((JTextField)ObjList.get(3)).getText()),
                                                DRIVEWHEEL, ca);

                                        VehicleList.remove(IND);
                                        VehicleList.add(sportCar);

                                        String addedStr = "SportCar:      " + "Price: " + ((JTextField)ObjList.get(0)).getText() + ", "
                                                + "Fuel: " + ((JTextField)ObjList.get(1)).getText() + ", " + "Mileage: " + ((JTextField)ObjList.get(2)).getText()
                                                + ", " + "Acceleration: " + ((JTextField)ObjList.get(3)).getText() + ", DriveWheel: " + driveWheel + ", "
                                                + "Cabriolet: " + CABRIOLET;

                                        DLM.remove(IND);
                                        DLM.addElement(addedStr);

                                        for(int i = 0; i < 4; i++){
                                            obj = ObjList.get(i);
                                            remove((JTextField)obj);
                                        }

                                        obj = (JComboBox)ObjList.get(4);
                                        remove((JComboBox)obj);

                                        obj = (JComboBox)ObjList.get(5);
                                        remove((JComboBox)obj);

                                        obj = (JButton)ObjList.get(6);
                                        remove((JButton)obj);

                                        AddBtn.setVisible(true);
                                        RemoveBtn.setVisible(true);
                                        ChangeBtn.setVisible(true);
                                        ObjList.clear();
                                        repaint();
                                    }
                                    catch (Exception ex){
                                        System.out.println("Exception 0");
                                        JOptionPane.showMessageDialog(null,"Проверьте введенные данные!","Внимание!", JOptionPane.INFORMATION_MESSAGE);
                                    }
                                }
                            });
                            add((JButton)obj);

                            repaint();
                            break;
                        case (5)://SUV
                            x = 960;
                            y = 20;
                            width = 100;
                            height = 30;

                            dcms = new DefaultComboBoxModel();
                            ObjList.add(new JTextField(String.valueOf(((SUV) obj).getPrice())));
                            ObjList.add(new JTextField(String.valueOf(((SUV) obj).getFuel())));
                            ObjList.add(new JTextField(String.valueOf(((SUV) obj).getMileage())));
                            ObjList.add(new JTextField(String.valueOf(((SUV) obj).getAcceleration())));
                            ObjList.add(new JTextField(String.valueOf(((SUV) obj).getClearance())));
                            ObjList.add(new JComboBox<String>(dcms));
                            ObjList.add(new JButton("OK"));

                            obj = (JTextField)ObjList.get(0);
                            ((JTextField) obj).setToolTipText("Price");
                            obj = (JTextField)ObjList.get(1);
                            ((JTextField) obj).setToolTipText("Fuel");
                            obj = (JTextField)ObjList.get(2);
                            ((JTextField) obj).setToolTipText("Mileage");
                            obj = (JTextField)ObjList.get(3);
                            ((JTextField) obj).setToolTipText("Acceleration");
                            obj = (JTextField)ObjList.get(4);
                            ((JTextField) obj).setToolTipText("Clearance");


                            for(int i = 0; i < 5; i++) {
                                obj = (JTextField)ObjList.get(i);
                                ((JTextField) obj).setBounds(x, y, width, height);

                                y += 40;
                                add((JTextField)obj);
                            }

                            obj = (JComboBox)ObjList.get(5);
                            ((JComboBox) obj).setBounds(x, y, width, height);
                            y += 40;
                            dcms.addElement("front");
                            dcms.addElement("rear");
                            dcms.addElement("full");
                            add((JComboBox)obj);

                            obj = (JButton)ObjList.get(6);
                            ((JButton) obj).setBounds(x, y, width, height);
                            ((JButton)obj).addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    try {
                                        DriveWheel DRIVEWHEEL;
                                        String driveWheel;
                                        int ind = dcms.getIndexOf(dcms.getSelectedItem());
                                        if (ind==0){
                                            driveWheel = "front";
                                            DRIVEWHEEL = DriveWheel.FRONT;
                                        }
                                        else if (ind==1){
                                            driveWheel = "rear";
                                            DRIVEWHEEL = DriveWheel.REAR;
                                        }
                                        else {
                                            driveWheel = "full";
                                            DRIVEWHEEL = DriveWheel.FULL;
                                        }

                                        SUV suv = new SUV(Integer.parseInt(((JTextField)ObjList.get(0)).getText()),
                                                Integer.parseInt(((JTextField)ObjList.get(1)).getText()),
                                                Integer.parseInt(((JTextField)ObjList.get(2)).getText()),
                                                Float.parseFloat(((JTextField)ObjList.get(3)).getText()),
                                                DRIVEWHEEL, Integer.parseInt(((JTextField)ObjList.get(4)).getText()));

                                        VehicleList.remove(IND);
                                        VehicleList.add(suv);

                                        String addedStr = "SUV:      " + "Price: " + ((JTextField)ObjList.get(0)).getText() + ", "
                                                + "Fuel: " + ((JTextField)ObjList.get(1)).getText() + ", " + "Mileage: " + ((JTextField)ObjList.get(2)).getText()
                                                + ", " + "Acceleration: " + ((JTextField)ObjList.get(3)).getText() + ", DriveWheel: " + driveWheel + ", "
                                                + "Clearance: " + ((JTextField)ObjList.get(4)).getText();

                                        DLM.remove(IND);
                                        DLM.addElement(addedStr);

                                        for(int i = 0; i < 5; i++){
                                            obj = ObjList.get(i);
                                            remove((JTextField)obj);
                                        }

                                        obj = (JComboBox)ObjList.get(5);
                                        remove((JComboBox)obj);

                                        obj = (JButton)ObjList.get(6);
                                        remove((JButton)obj);

                                        AddBtn.setVisible(true);
                                        RemoveBtn.setVisible(true);
                                        ChangeBtn.setVisible(true);
                                        ObjList.clear();
                                        repaint();
                                    }
                                    catch (Exception ex){
                                        System.out.println("Exception 0");
                                        JOptionPane.showMessageDialog(null,"Проверьте введенные данные!","Внимание!", JOptionPane.INFORMATION_MESSAGE);
                                    }
                                }
                            });
                            add((JButton)obj);

                            repaint();
                            break;
                        /*case (6)://LuxeFeature
                            x = 960;
                            y = 20;
                            width = 100;
                            height = 30;

                            ObjList.add(new JTextField(String.valueOf(((LuxeFeatures) obj).getName())));
                            ObjList.add(new JTextField(String.valueOf(((LuxeFeatures) obj).getPriceUp())));
                            ObjList.add(new JButton("OK"));

                            obj = (JTextField)ObjList.get(0);
                            ((JTextField) obj).setToolTipText("Name");
                            obj = (JTextField)ObjList.get(1);
                            ((JTextField) obj).setToolTipText("Price-up");

                            for(int i = 0; i < 2; i++) {
                                obj = (JTextField)ObjList.get(i);
                                ((JTextField) obj).setBounds(x, y, width, height);

                                y += 40;
                                add((JTextField)obj);
                            }

                            obj = (JButton)ObjList.get(2);
                            ((JButton) obj).setBounds(x, y, width, height);
                            ((JButton)obj).addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    try {
                                        LuxeFeatures luxeFeature = new LuxeFeatures(((JTextField)ObjList.get(0)).getText(), Integer.parseInt(((JTextField)ObjList.get(1)).getText()));
                                        VehicleList.remove(IND);
                                        VehicleList.add(luxeFeature);

                                        String addedStr = "LuxeFeature:      " + "Name: " + ((JTextField)ObjList.get(0)).getText() + ", "
                                                + "Price-up: " + ((JTextField)ObjList.get(1)).getText();

                                        DLM.remove(IND);
                                        DLM.addElement(addedStr);

                                        for(int i = 0; i < 2; i++){
                                            obj = ObjList.get(i);
                                            remove((JTextField)obj);
                                        }

                                        obj = (JButton)ObjList.get(2);
                                        remove((JButton)obj);

                                        AddBtn.setVisible(true);
                                        RemoveBtn.setVisible(true);
                                        ChangeBtn.setVisible(true);
                                        ObjList.clear();
                                        repaint();
                                    }
                                    catch (Exception ex){
                                        System.out.println("Exception 0");
                                        JOptionPane.showMessageDialog(null,"Проверьте введенные данные!");
                                    }
                                }
                            });
                            add((JButton)obj);

                            repaint();
                            break;*/
                    }
                }
            });


        RemoveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int index = FrameList.getSelectedIndex();
                    DLM.remove(index);
                    VehicleList.remove(index);
                }
                catch(Exception ex){
                    JOptionPane.showMessageDialog(null,"Выберите элемент для удаления!","Внимание!", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        CloseBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        setVisible(true);
    }

}
