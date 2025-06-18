package com.example.nutritracker.example.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import com.example.nutritracker.R;
import com.example.nutritracker.example.db.FoodDatabase;
import com.example.nutritracker.example.db.dbPopulator;
import com.example.nutritracker.example.models.Food;
import com.example.nutritracker.example.models.FoodLog;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

public class FoodActivity extends AppCompatActivity {

    public Food selectedFood = null;
    private TextInputEditText inputMealSize;
    private TextView textCalories, textFats, textCarbs, textSugars, textProteins;
    private FoodDatabase db;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.food), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = Room.databaseBuilder(getApplicationContext(),
                FoodDatabase.class, "foodDB").fallbackToDestructiveMigration(true).build();

        List<Food> allFoods = new dbPopulator().insertAll();

        ImageView buttonHome = findViewById(R.id.buttonHome);
        TextView textTitle = findViewById(R.id.textTitle);
        ImageView buttonSettings = findViewById(R.id.buttonSettings);
        AutoCompleteTextView fieldName = findViewById(R.id.autoCompleteTextView);
        Button buttonLog = findViewById(R.id.buttonLog);
        Button hisButton = findViewById(R.id.buttonPrev);

        hisButton.setOnClickListener(v -> {
            Intent intent = new Intent(FoodActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        Set<String> nameSet = new LinkedHashSet<>();
        for (Food food : allFoods) {
            nameSet.add(food.getName());
        }
        List<String> foodNames = new ArrayList<>(nameSet);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                foodNames
        );

        fieldName.setAdapter(adapter);
        fieldName.setThreshold(1);
        inputMealSize = findViewById(R.id.sizeEnter);

        textCalories = findViewById(R.id.textCalories);
        textFats = findViewById(R.id.textFats);
        textCarbs = findViewById(R.id.textCarbs);
        textSugars = findViewById(R.id.textSugars);
        textProteins = findViewById(R.id.textProteins);

        inputMealSize.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                updateNutrientValues();
            }
        });

        fieldName.setOnItemClickListener((parent, view, position, id) -> {
            String selectedName = (String) parent.getItemAtPosition(position);
            selectedFood = null;

            for (Food f : allFoods) {
                if (f.getName().equalsIgnoreCase(selectedName)) {
                    selectedFood = f;
                    break;
                }
            }
            updateNutrientValues();
        });

        buttonLog.setOnClickListener(v -> {
            String mealSizeText = inputMealSize.getText() != null ? inputMealSize.getText().toString() : "1";

            if (selectedFood == null) {
                Toast.makeText(FoodActivity.this, "Please select a food first", Toast.LENGTH_SHORT).show();
                return;
            }

            int mealSize;
            try {
                mealSize = Integer.parseInt(mealSizeText);
            } catch (NumberFormatException e) {
                inputMealSize.setError("Invalid size");
                return;
            }

            FoodLog foodLog = new FoodLog(selectedFood.getUid(), mealSize);

            Executors.newSingleThreadExecutor().execute(() -> {
                db.foodLogDao().insert(foodLog);
                runOnUiThread(() -> Toast.makeText(FoodActivity.this,
                        "Logged " + mealSize + "g of " + selectedFood.getName(),
                        Toast.LENGTH_SHORT).show());
            });
        });

        buttonHome.setOnClickListener(v -> finish());

        textTitle.setOnClickListener(v -> finish());

        buttonSettings.setOnClickListener(v -> {
            startActivity(new Intent(FoodActivity.this, SettingsActivity.class));
        });
    }

    @SuppressLint("DefaultLocale")
    private void updateNutrientValues() {
        if (selectedFood != null) {
            String sizeText = inputMealSize.getText() != null ? inputMealSize.getText().toString() : "1";
            double multiplier;
            try {
                multiplier = Double.parseDouble(sizeText) / 100;
            } catch (NumberFormatException e) {
                multiplier = 1.0;
            }

            textCalories.setText(String.format("%.1f", selectedFood.getCalories() * multiplier));
            textFats.setText(String.format("%.1f", selectedFood.getFats() * multiplier));
            textCarbs.setText(String.format("%.1f", selectedFood.getCarbs() * multiplier));
            textSugars.setText(String.format("%.1f", selectedFood.getSugars() * multiplier));
            textProteins.setText(String.format("%.1f", selectedFood.getProtein() * multiplier));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }
}