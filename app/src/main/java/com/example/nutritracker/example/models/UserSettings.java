package com.example.nutritracker.example.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "userSettings")
public class UserSettings {
    @PrimaryKey
    private int settingsId = 0;

    private double weight;
    private String theme;
    private boolean notificationsEnabled;
    private String preferredUnits;

    public UserSettings(double weight, String theme, boolean notificationsEnabled, String preferredUnits) {
        this.weight = weight;
        this.theme = theme;
        this.notificationsEnabled = notificationsEnabled;
        this.preferredUnits = preferredUnits;
    }

    public int getSettingsId() {
        return settingsId;
    }

    public void setSettingsId(int settingsId) {
        this.settingsId = settingsId;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }

    public void setNotificationsEnabled(boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }

    public String getPreferredUnits() {
        return preferredUnits;
    }

    public void setPreferredUnits(String preferredUnits) {
        this.preferredUnits = preferredUnits;
    }
}