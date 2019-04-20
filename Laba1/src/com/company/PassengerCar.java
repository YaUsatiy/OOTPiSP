package com.company;

public class PassengerCar extends Vehicle{
    private float acceleration;
    private DriveWheel driveWheel;

    public PassengerCar(){};

    public PassengerCar(int price, int fuel, int mileage, float acceleration, DriveWheel driveWheel){
        super(price, fuel, mileage);
        this.acceleration = acceleration;
        this.driveWheel = driveWheel;
    }

    public String getDriveWheel() {
        if (this.driveWheel == DriveWheel.FRONT)
            return "front";
        else if (this.driveWheel == DriveWheel.REAR)
            return "rear";
        else return "full";
    }

    public void setDriveWheel(DriveWheel driveWheel) {
        this.driveWheel = driveWheel;
    }

    public float getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }
}
