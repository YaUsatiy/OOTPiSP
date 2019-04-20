package com.company;

import java.io.Serializable;
import java.util.ArrayList;

public class Vehicle implements Serializable {
    private int price;
    private int fuel;
    private int mileage;

    public Vehicle(){};

    public Vehicle(int price, int fuel, int mileage){
        this.price = price;
        this.fuel = fuel;
        this.mileage = mileage;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getFuel() {
        return fuel;
    }

    public void setFuel(int fuel) {
        this.fuel = fuel;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

}

