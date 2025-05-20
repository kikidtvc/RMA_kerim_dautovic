package com.example.nutritracker.example.models;
import androidx.room.*;
@Entity(tableName = "Food")
public class Food {

    @PrimaryKey
    public int uid;

    @ColumnInfo(name = "foodName")
    public String foodName;

    @ColumnInfo(name = "calories")
    public int calories;

    @ColumnInfo(name = "fats")
    public Double fats;

    @ColumnInfo(name = "carbs")
    public Double carbs;

    @ColumnInfo(name = "sugars")
    public Double sugars;

    @ColumnInfo(name = "protein")
    public Double protein;

    public Food(int uid, String foodName, int calories, Double fats, Double carbs, Double sugars, Double protein) {
        this.protein = protein;
        this.sugars = sugars;
        this.carbs = carbs;
        this.fats = fats;
        this.calories = calories;
        this.foodName = foodName;
        this.uid = uid;
    }

    public String getName() {return this.foodName;}
    public int getCalories() {return this.calories;}

    public Double getFats() {
        return fats;
    }

    public Double getCarbs() {
        return carbs;
    }

    public Double getSugars() {
        return sugars;
    }

    public Double getProtein() {
        return protein;
    }

    public int getUid() {
        return uid;
    }
}
