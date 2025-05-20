package com.example.nutritracker.example.controllers;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.nutritracker.example.models.Food;
import com.example.nutritracker.example.models.FoodLog;

import java.util.List;

@Dao
public interface FoodLogDao {

    @Query("SELECT * FROM FoodLog")
    List<FoodLog> getAll();

    @Insert
    void insert(FoodLog FoodLog);

    @Query("SELECT * FROM FoodLog ORDER BY uid DESC LIMIT 5")
    List<FoodLog> getLastFive();
}
