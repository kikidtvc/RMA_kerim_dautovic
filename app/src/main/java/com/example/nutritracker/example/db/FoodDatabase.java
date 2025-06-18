package com.example.nutritracker.example.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.nutritracker.example.controllers.FoodDao;
import com.example.nutritracker.example.controllers.FoodLogDao;
import com.example.nutritracker.example.models.Food;
import com.example.nutritracker.example.models.FoodLog;
import com.example.nutritracker.example.models.UserSettings;
import com.example.nutritracker.example.controllers.UserSettingsDao;

@Database(entities = {Food.class, FoodLog.class, UserSettings.class}, version = 3)
public abstract class FoodDatabase extends RoomDatabase {
    public abstract FoodDao foodDao();
    public abstract FoodLogDao foodLogDao();
    public abstract UserSettingsDao userSettingsDao();
}