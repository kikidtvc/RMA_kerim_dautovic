package com.example.nutritracker.example.controllers;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.nutritracker.example.models.Food;
import com.example.nutritracker.example.models.FoodLog;

import java.util.List;

@Dao
public interface FoodDao {

    @Query("SELECT * FROM Food")
    List<Food> getAll();

    @Query("SELECT * FROM Food WHERE uid = :id")
    Food getFoodById(int id);
}