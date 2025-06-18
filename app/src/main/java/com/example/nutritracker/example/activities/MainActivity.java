package com.example.nutritracker.example.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.nutritracker.R;
import com.example.nutritracker.example.controllers.NutritionCalculator;
import com.example.nutritracker.example.db.FoodDatabase;
import com.example.nutritracker.example.db.dbPopulator;
import com.example.nutritracker.example.models.Food;
import com.example.nutritracker.example.models.FoodLog;
import com.example.nutritracker.example.models.Meal;
import com.example.nutritracker.example.models.UserSettings;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private FoodDatabase db;
    private PieChart nutritionChart;
    private TextView recommendationText;

    public List<Food> allFoods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = Room.databaseBuilder(getApplicationContext(),
                FoodDatabase.class, "foodDB").build();

        this.allFoods = new dbPopulator().insertAll();

        nutritionChart = findViewById(R.id.nutritionChart);
        recommendationText = findViewById(R.id.recommendationText);

        findViewById(R.id.buttonFood).setOnClickListener(v -> {
            startActivity(new Intent(this, FoodActivity.class));
        });

        findViewById(R.id.buttonSettings).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        });

        loadAndDisplayNutritionData();
    }



    private void loadAndDisplayNutritionData() {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                List<FoodLog> lastFiveLogs = db.foodLogDao().getLastFiveLogs();
                List<Meal> lastFiveMeals = convertFoodLogsToMeals(lastFiveLogs);

                UserSettings settings = db.userSettingsDao().getSettings();
                double weightKg = settings != null ? settings.getWeight() : 70.0;

                Log.d("MainActivity", "Total logs: " + lastFiveLogs.size());
                Log.d("MainActivity", "Total meals: " + lastFiveMeals.size());

                Meal nextMeal = NutritionCalculator.calculateNextBestMeal(lastFiveMeals, weightKg);

                runOnUiThread(() -> {
                    if (lastFiveMeals.size() < 5) {
                        recommendationText.setText("Need at least 5 meals to calculate recommendations");
                        return;
                    }

                    setupPieChart(nextMeal);
                    recommendationText.setText(String.format(
                            "Recommended next meal:\n%d calories (%.1fg protein)",
                            nextMeal.calories, nextMeal.protein
                    ));
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Error loading nutrition data", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                });
            }
        });
    }

    private List<Meal> convertFoodLogsToMeals(List<FoodLog> foodLogs) {
        List<Meal> meals = new ArrayList<>();
        for (FoodLog log : foodLogs) {
            try {
                for(Food food : this.allFoods) {
                    if (food.getUid() == log.getFoodUID()) {
                        meals.add(new Meal(
                                (int) (food.getCalories() * log.getSize() / 100),
                                food.getFats() * log.getSize() / 100,
                                food.getCarbs() * log.getSize() / 100,
                                food.getSugars() * log.getSize() / 100,
                                food.getProtein() * log.getSize() / 100
                        ));
                    } else {
                        Log.w("convertFoodLogsToMeals", "No food found for UID: " + food.getUid());
                    }
                }
            } catch (Exception e) {
                Log.e("convertFoodLogsToMeals", "Exception converting FoodLog to Meal", e);
            }
        }
        return meals;
    }


    private void setupPieChart(Meal meal) {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry((float) meal.fats, "Fats"));
        entries.add(new PieEntry((float) meal.carbs, "Carbs"));
        entries.add(new PieEntry((float) meal.protein, "Protein"));

        PieDataSet dataSet = new PieDataSet(entries, "Nutrition Breakdown");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(12f);

        PieData pieData = new PieData(dataSet);
        nutritionChart.setData(pieData);

        nutritionChart.getDescription().setEnabled(false);
        nutritionChart.setCenterText(String.format("%d kcal", meal.calories));
        nutritionChart.setCenterTextSize(16f);
        nutritionChart.setEntryLabelColor(getResources().getColor(android.R.color.black));
        nutritionChart.setEntryLabelTextSize(12f);
        nutritionChart.animateY(1000);

        nutritionChart.invalidate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAndDisplayNutritionData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }
}