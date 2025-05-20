package com.example.nutritracker.example.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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

        FoodDatabase db = Room.databaseBuilder(getApplicationContext(),
                FoodDatabase.class, "foodDB").build();

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
        TextInputEditText inputMealSize = findViewById(R.id.sizeEnter);

        TextView textCalories = findViewById(R.id.textCalories);
        TextView textFats = findViewById(R.id.textFats);
        TextView textCarbs = findViewById(R.id.textCarbs);
        TextView textSugars = findViewById(R.id.textSugars);
        TextView textProteins = findViewById(R.id.textProteins);

        fieldName.setOnItemClickListener((parent, view, position, id) -> {

            String selectedName = (String) parent.getItemAtPosition(position);
            selectedFood = null;

            for (Food f : allFoods) {
                if (f.getName().equalsIgnoreCase(selectedName)) {
                    selectedFood = f;
                    break;
                }
            }

            if (selectedFood != null) {
                String sizeText = inputMealSize.getText() != null ? inputMealSize.getText().toString() : "1";
                double multiplier;
                try {
                    multiplier = Double.parseDouble(sizeText);
                } catch (NumberFormatException e) {
                    multiplier = 1.0 / 1000;
                }

                textCalories.setText(String.format("%.1f", selectedFood.getCalories() * multiplier));
                textFats.setText(String.format("%.1f", selectedFood.getFats() * multiplier));
                textCarbs.setText(String.format("%.1f", selectedFood.getCarbs() * multiplier));
                textSugars.setText(String.format("%.1f", selectedFood.getSugars() * multiplier));
                textProteins.setText(String.format("%.1f", selectedFood.getProtein() * multiplier));
            }
        });

        buttonLog.setOnClickListener(v -> {
            String mealSizeText = inputMealSize.getText() != null ? inputMealSize.getText().toString() : "1";

            if (selectedFood == null) {

                Log.d("FOOD", "DEAD");
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
            });

            Log.d("FOOD_LOG", "Logged: " + foodLog.getUid() + " x" + foodLog.foodUID);
        });

        buttonHome.setOnClickListener(v -> finish());

        textTitle.setOnClickListener(v -> finish());

        buttonSettings.setOnClickListener(v -> {

            List<FoodLog> logs = db.foodLogDao().getAll();

            for (FoodLog log : logs) {

                Log.d("FOOD_LOG", "logs: " + log.getUid() + " - " + log.foodUID);
            }
            Toast.makeText(getApplicationContext(), "This a toast message", Toast.LENGTH_LONG).show();
        });
    }
}