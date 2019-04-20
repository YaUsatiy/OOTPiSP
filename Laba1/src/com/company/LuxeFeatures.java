package com.company;

import java.io.Serializable;

public class LuxeFeatures implements Serializable {
    private String name;
    private int priceUp;

    public LuxeFeatures(){}

    public LuxeFeatures(String name, int priceUp){
        this.name = name;
        this.priceUp = priceUp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriceUp() {
        return priceUp;
    }

    public void setPriceUp(int priceUp) {
        this.priceUp = priceUp;
    }

}