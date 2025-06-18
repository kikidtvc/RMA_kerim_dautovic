package com.example.nutritracker.example.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "FoodLog")
public class FoodLog {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "foodUID")
    public int foodUID;

    @ColumnInfo(name = "size")
    public int size;

    @ColumnInfo(name = "logTime")
    public long logTime;

    public FoodLog(int foodUID, int size) {

        this.foodUID = foodUID;
        this.size = size;
        this.logTime = new Date().getTime();
    }

    public long getLogTime() {

        return this.logTime;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getFoodUID() {
        return foodUID;
    }

    public void setFoodUID(int foodUID) {
        this.foodUID = foodUID;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
