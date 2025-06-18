package com.example.nutritracker.example.controllers;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.nutritracker.example.models.UserSettings;

@Dao
public interface UserSettingsDao {
    // Insert or replace the single settings row
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSettings(UserSettings settings);

    @Update
    void updateSettings(UserSettings settings);

    @Query("SELECT * FROM userSettings WHERE settingsId = 0")
    UserSettings getSettings();

    @Query("UPDATE userSettings SET weight = :weight WHERE settingsId = 0")
    void updateWeight(double weight);

    @Query("UPDATE userSettings SET theme = :theme WHERE settingsId = 0")
    void updateTheme(String theme);

    @Query("UPDATE userSettings SET notificationsEnabled = :enabled WHERE settingsId = 0")
    void updateNotificationsEnabled(boolean enabled);

    @Query("UPDATE userSettings SET preferredUnits = :units WHERE settingsId = 0")
    void updatePreferredUnits(String units);
}