package com.example.capstone18.capstone;

/**
 * Created by saurabh_local on 3/7/2018.
 */

public class Ingredient {

    private String name;
    private double quantity;
    private String measurementUnit;

    public Ingredient(String name, double quantity, String measurementUnit) {
        this.name = name;
        this.quantity = quantity;
        this.measurementUnit = measurementUnit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getMeasurementUnit() {
        return measurementUnit;
    }

    public void setMeasurementUnit(String measurementUnit) {
        this.measurementUnit = measurementUnit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Ingredient)) return false;

        Ingredient that = (Ingredient) o;

        return name.equalsIgnoreCase(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return this.name+": "+this.quantity+" "+this.measurementUnit;
    }
}
