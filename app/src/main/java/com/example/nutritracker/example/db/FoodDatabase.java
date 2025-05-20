package com.example.nutritracker.example.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.nutritracker.example.controllers.FoodDao;
import com.example.nutritracker.example.controllers.FoodLogDao;
import com.example.nutritracker.example.models.Food;
import com.example.nutritracker.example.models.FoodLog;

@Database(entities = {Food.class, FoodLog.class}, version = 1)
public abstract class FoodDatabase extends RoomDatabase {
    public abstract FoodDao foodDao();
    public abstract FoodLogDao foodLogDao(); // Add this
}

