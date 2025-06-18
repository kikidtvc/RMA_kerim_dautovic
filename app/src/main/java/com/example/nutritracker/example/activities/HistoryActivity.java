package com.example.nutritracker.example.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.nutritracker.R;
import com.example.nutritracker.example.db.FoodDatabase;
import com.example.nutritracker.example.db.dbPopulator;
import com.example.nutritracker.example.models.Food;
import com.example.nutritracker.example.models.FoodLog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class HistoryActivity extends AppCompatActivity {

    private TextView historyTextView;
    private FoodDatabase db;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd.MM.yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyTextView = findViewById(R.id.historyText);

        db = Room.databaseBuilder(getApplicationContext(),
                FoodDatabase.class, "foodDB").build();

        loadFoodLogs();

        ImageView buttonHome = findViewById(R.id.buttonHome);
        TextView textTitle = findViewById(R.id.textTitle);
        ImageView buttonSettings = findViewById(R.id.buttonSettings);

        buttonHome.setOnClickListener(v -> finish());
        textTitle.setOnClickListener(v -> finish());
        buttonSettings.setOnClickListener(v -> {
            startActivity(new Intent(HistoryActivity.this, SettingsActivity.class));
        });
    }

    @SuppressLint("SetTextI18n")
    private void loadFoodLogs() {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                List<FoodLog> logs = db.foodLogDao().getAll();
                List<Food> allFoods = new dbPopulator().insertAll();

                Log.d("HistoryActivity", "Total foods: " + allFoods.size());
                Log.d("HistoryActivity", "Total logs: " + logs.size());

                for (Food food : allFoods) {
                    Log.d("FoodItem", "ID: " + food.getUid() + " Name: " + food.getName());
                }

                for (FoodLog log : logs) {
                    Log.d("FoodLog", "LogID: " + log.getUid() + " FoodUID: " + log.getFoodUID());
                }

                runOnUiThread(() -> {
                    if (logs.isEmpty()) {
                        historyTextView.setText("No meals logged yet");
                        Toast.makeText(HistoryActivity.this, "No meals logged", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    StringBuilder sb = new StringBuilder();
                    int displayCount = Math.min(logs.size(), 5);

                    for (int i = logs.size() - 1; i >= logs.size() - displayCount; i--) {
                        FoodLog log = logs.get(i);
                        String foodName = findFoodName(allFoods, log.getFoodUID());

                        String formattedTime = dateFormat.format(new Date(log.getLogTime()));

                        sb.append("Time: ").append(formattedTime)
                                .append("\nFood: ").append(foodName)
                                .append("\nPortion: ").append(log.getSize()).append("g\n\n");
                    }

                    historyTextView.setText(sb.toString());
                });
            } catch (Exception e) {
                Log.e("HistoryActivity", "Error loading logs", e);
                runOnUiThread(() -> {
                    historyTextView.setText("Error loading history");
                    Toast.makeText(HistoryActivity.this, "Error loading history", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private String findFoodName(List<Food> allFoods, long foodUid) {
        for (Food food : allFoods) {
            if (food.getUid() == foodUid) {
                return food.getName();
            }
        }
        Log.e("HistoryActivity", "Food not found for UID: " + foodUid);
        return "Unknown Food (ID: " + foodUid + ")";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }
}