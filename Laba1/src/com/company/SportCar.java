package com.company;

public class SportCar extends PassengerCar{
    private boolean cabriolet;

    public SportCar(){};

    public SportCar(int price, int fuel, int mileage, float acceleration, DriveWheel driveWheel, boolean cabriolet){
        super(price, fuel, mileage, acceleration, driveWheel);
        this.cabriolet = cabriolet;
    }

    public void setCabriolet(boolean cabriolet) {
        this.cabriolet = cabriolet;
    }

    public String isCabriolet() {
        if (this.cabriolet) return "cabriolet";
        else return "non-cabriolet";
    }
}
