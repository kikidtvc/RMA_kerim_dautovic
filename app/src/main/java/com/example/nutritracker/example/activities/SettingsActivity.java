package com.example.nutritracker.example.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.room.Room;

import com.example.nutritracker.R;
import com.example.nutritracker.example.db.FoodDatabase;
import com.example.nutritracker.example.models.UserSettings;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.Executors;

public class SettingsActivity extends AppCompatActivity {

    private TextInputEditText inputWeight;
    private MaterialButtonToggleGroup unitToggleGroup;
    private MaterialButtonToggleGroup themeToggleGroup;
    private SwitchMaterial notificationsSwitch;
    private AppCompatButton buttonSave;
    private FoodDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        db = Room.databaseBuilder(getApplicationContext(),
                FoodDatabase.class, "foodDB").build();

        initializeViews();

        loadCurrentSettings();

        setupSaveButton();
    }

    private void initializeViews() {
        inputWeight = findViewById(R.id.inputWeight);
        unitToggleGroup = findViewById(R.id.unitToggleGroup);
        themeToggleGroup = findViewById(R.id.themeToggleGroup);
        notificationsSwitch = findViewById(R.id.notificationsSwitch);
        buttonSave = findViewById(R.id.buttonSave);
    }

    private void loadCurrentSettings() {
        Executors.newSingleThreadExecutor().execute(() -> {
            UserSettings settings = db.userSettingsDao().getSettings();
            runOnUiThread(() -> {
                if (settings != null) {
                    inputWeight.setText(String.valueOf(settings.getWeight()));

                    int unitButtonId = settings.getPreferredUnits().equals("kg") ?
                            R.id.kgButton : R.id.lbsButton;
                    unitToggleGroup.check(unitButtonId);

                    int themeButtonId = settings.getTheme().equals("light") ?
                            R.id.lightThemeButton : R.id.darkThemeButton;
                    themeToggleGroup.check(themeButtonId);

                    notificationsSwitch.setChecked(settings.isNotificationsEnabled());
                }
            });
        });
    }

    private void setupSaveButton() {
        buttonSave.setOnClickListener(v -> saveSettings());
    }

    private void saveSettings() {
        try {
            double weight = Double.parseDouble(inputWeight.getText().toString());
            String units = unitToggleGroup.getCheckedButtonId() == R.id.kgButton ? "kg" : "lbs";
            String theme = themeToggleGroup.getCheckedButtonId() == R.id.lightThemeButton ? "light" : "dark";
            boolean notificationsEnabled = notificationsSwitch.isChecked();

            UserSettings newSettings = new UserSettings(
                    weight,
                    theme,
                    notificationsEnabled,
                    units
            );

            Executors.newSingleThreadExecutor().execute(() -> {
                db.userSettingsDao().insertSettings(newSettings);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Settings saved successfully", Toast.LENGTH_SHORT).show();
                    applyTheme(theme);
                });
            });
        } catch (NumberFormatException e) {
            inputWeight.setError("Please enter a valid weight");
        }
    }

    private void applyTheme(String theme) {
        if (theme.equals("dark")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        recreate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }
}