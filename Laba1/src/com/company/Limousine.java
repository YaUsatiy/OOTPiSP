package com.company;

import java.util.HashSet;
import java.util.Set;

public class Limousine extends PassengerCar{
    private int places;
    private LuxeFeatures luxeFeature;

    public Limousine(){}

    public Limousine(int price, int fuel, int mileage, float acceleration, DriveWheel driveWheel, int places, String LName, int LPriceUp){
        super(price, fuel, mileage, acceleration, driveWheel);
        this.places = places;
        this.luxeFeature = new LuxeFeatures(LName,LPriceUp);

    }

    public int getPlaces() {
        return places;
    }

    public void setPlaces(int places) {
        this.places = places;
    }

    public void setLuxeFeature(LuxeFeatures luxeFeature){
        this.luxeFeature = luxeFeature;
    }

    public LuxeFeatures getLuxeFeatures(){
        return this.luxeFeature;
    }

    public int getLuxePriceUp(){
        return this.luxeFeature.getPriceUp();
    }

    public String getLuxName(){
        return this.luxeFeature.getName();
    }

}