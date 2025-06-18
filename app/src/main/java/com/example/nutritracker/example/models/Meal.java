package com.example.nutritracker.example.models;

public class Meal {
    public int calories;
    public double fats;
    public double carbs;
    public double sugars;
    public double protein;

    public Meal() {}

    public Meal(int calories, double fats, double carbs, double sugars, double protein) {
        this.calories = calories;
        this.fats = fats;
        this.carbs = carbs;
        this.sugars = sugars;
        this.protein = protein;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public double getFats() {
        return fats;
    }

    public void setFats(double fats) {
        this.fats = fats;
    }

    public double getCarbs() {
        return carbs;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }

    public double getSugars() {
        return sugars;
    }

    public void setSugars(double sugars) {
        this.sugars = sugars;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }
}
