package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class Deserializer {
    public ArrayList<Object> text(String filename, ArrayList<Object> VehicleList) { //передаю не file, а filename
        try {
            VehicleList.clear();
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String str;
            ArrayList<String> arr = new ArrayList<>();

            int i = -1;
            while ((str = reader.readLine()) != null){
                if(str.equals("Class")){
                    arr.clear();
                    i++;
                    str = reader.readLine();
                    if ( (!(str.equals("com.company.Saloon"))) && (!(str.equals("com.company.Limousine"))) && (!(str.equals("com.company.SportCar"))) && (!(str.equals("com.company.SUV"))) ) {
                        Class clazz = Class.forName(str);
                        VehicleList.add(clazz.newInstance());
                    }
                    else{
                        if(str.equals("com.company.Saloon")) {
                            for (int j = 1; j < 14; j++) {
                                //str = reader.readLine();
                                arr.add(reader.readLine());
                            }
                            float acceleration = Float.parseFloat(arr.get(9));
                            DriveWheel driveWheel;
                            if (arr.get(12).equals("FRONT")) driveWheel = DriveWheel.FRONT;
                            else  if (arr.get(12).equals("REAR")) driveWheel = DriveWheel.REAR;
                            else driveWheel = DriveWheel.FULL;
                            boolean electro;
                            if (arr.get(2).equals("true")) electro = true;
                            else electro = false;
                            int LPriceUp = Integer.parseInt(arr.get(6));
                            String Lname = arr.get(5);
                            Saloon saloon= new Saloon(1,2,3,acceleration, driveWheel, electro,Lname, LPriceUp);
                            VehicleList.add(saloon);
                            continue;
                        }
                        float acceleration = Float.parseFloat(arr.get(9));
                        DriveWheel driveWheel;
                        if (arr.get(12).equals("FRONT")) driveWheel = DriveWheel.FRONT;
                        else  if (arr.get(12).equals("REAR")) driveWheel = DriveWheel.REAR;
                        else driveWheel = DriveWheel.FULL;
                        int places = Integer.parseInt(arr.get(2));
                        int LPriceUp = Integer.parseInt(arr.get(6));
                        String Lname = arr.get(5);
                        if(str.equals("com.company.Limousine")) {
                            for (int j = 1; j < 14; j++) {
                                //str = reader.readLine();
                                arr.add(reader.readLine());
                            }
                            Limousine limousine = new Limousine(1,2,3,acceleration,driveWheel,places,Lname,LPriceUp);
                            VehicleList.add(limousine);
                            continue;
                        }
                        if(str.equals("com.company.Sportcar")) {
                            for (int j = 1; j < 10; j++) {
                                //str = reader.readLine();
                                arr.add(reader.readLine());
                            }
                            acceleration = Float.parseFloat(arr.get(5));
                            if (arr.get(8).equals("FRONT")) driveWheel = DriveWheel.FRONT;
                            else  if (arr.get(8).equals("REAR")) driveWheel = DriveWheel.REAR;
                            else driveWheel = DriveWheel.FULL;
                            boolean cabriolet;
                            if (arr.get(2).equals("true")) cabriolet = true;
                            else cabriolet = false;

                            SportCar sportCar = new SportCar(1, 2, 3, acceleration, driveWheel, cabriolet);
                            VehicleList.add(sportCar);
                            continue;
                        }
                        if(str.equals("com.company.SUV")) {
                            for (int j = 1; j < 10; j++) {
                                arr.add(str);
                            }
                            acceleration = Float.parseFloat(arr.get(5));
                            int clearance = Integer.parseInt(arr.get(2));
                            SUV suv = new SUV(1, 2, 3, acceleration, DriveWheel.FRONT, clearance);
                            VehicleList.add(suv);
                            continue;
                        }
                    }
                }
                if((str.equals("Field"))){
                    str = reader.readLine();
                    if(!(str.equals("luxeFeature"))) {
                        Field field = VehicleList.get(i).getClass().getDeclaredField(str);
                        field.setAccessible(true);
                        str = reader.readLine();
                        if ((str.equals("true")) || (str.equals("false"))) {
                            boolean b;
                            if (str.equals(true)) b = true;
                            else b = false;
                            field.set(VehicleList.get(i), b);
                        }
                        else {
                            try{
                                field.set(VehicleList.get(i), str);
                            }
                            catch (Exception ex){
                                try {field.set((VehicleList.get(i)), Integer.parseInt(str));}
                                catch(Exception e1){}
                            }
                        }
                    }
                    else{
                        str = reader.readLine();
                        str = reader.readLine();
                    }
                }
                if(str.equals("SField")){
                    str = reader.readLine();
                    Field sfield = VehicleList.get(i).getClass().getSuperclass().getDeclaredField(str);
                    sfield.setAccessible(true);
                    str = reader.readLine();

                    try{
                        sfield.set(VehicleList.get(i), str);
                    }
                    catch (Exception ex){
                        try {sfield.set((VehicleList.get(i)), Integer.parseInt(str));}
                        catch(Exception e2){}
                    }

                }
            }
            reader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Problem with text serialization found");
        }
        return VehicleList;
    }
}
