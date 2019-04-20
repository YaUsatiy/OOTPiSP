package com.company;

public class SUV extends PassengerCar{
    private int clearance;

    public SUV(){};

    public SUV(int price, int fuel, int mileage, float acceleration, DriveWheel driveWheel, int clearance){
        super(price, fuel, mileage, acceleration, driveWheel);
        this.clearance = clearance;
    }

    public int getClearance() {
        return clearance;
    }

    public void setClearance(int clearance) {
        this.clearance = clearance;
    }
}
