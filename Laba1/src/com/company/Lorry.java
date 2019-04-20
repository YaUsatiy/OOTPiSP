package com.company;

public class Lorry extends Vehicle {
    private int cargo;

    public Lorry(){};

    public Lorry(int price, int fuel, int mileage, int cargo){
        super(price, fuel, mileage);
        this.cargo = cargo;
    }

    public void setCargo(int cargo) {
        this.cargo = cargo;
    }

    public int getCargo() {
        return cargo;
    }
}
