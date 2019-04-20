package com.company;

public class Saloon extends PassengerCar {
    private boolean electro;
    private LuxeFeatures luxeFeature;


    public void setElectro(boolean electro) {
        this.electro = electro;
    }

    public String isElectro() {
        if (this.electro) return "electro";
        else return "non-electro";
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

    public Saloon(){}

    public Saloon(int price, int fuel, int mileage, float acceleration, DriveWheel driveWheel, boolean electro, String LName, int LPriceUp){
        super(price, fuel, mileage, acceleration, driveWheel);
        this.electro = electro;
        this.luxeFeature = new LuxeFeatures(LName,LPriceUp);
    }

}

