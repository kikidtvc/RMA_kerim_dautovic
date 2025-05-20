package com.example.nutritracker.example.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.nutritracker.R;
import com.example.nutritracker.example.db.FoodDatabase;
import com.example.nutritracker.example.db.dbPopulator;
import com.example.nutritracker.example.models.Food;
import com.example.nutritracker.example.models.FoodLog;

import java.util.List;
import java.util.concurrent.Executors;

public class HistoryActivity extends AppCompatActivity {

    TextView historyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyTextView = findViewById(R.id.historyText);

        FoodDatabase db = Room.databaseBuilder(getApplicationContext(),
                FoodDatabase.class, "foodDB").build();

        List<Food> allFoods = new dbPopulator().insertAll();

        Executors.newSingleThreadExecutor().execute(() -> {
            List<FoodLog> logs = db.foodLogDao().getAll();
            StringBuilder sb = new StringBuilder();


            String foodName = new String();
            int limit = Math.min(logs.size(), 5);
            for (int i = 0; i < limit; i++) {
                FoodLog log = logs.get(i);
                for (Food food : allFoods) {

                    if (log.foodUID == food.getUid()) {

                        foodName = food.getName();
                        break;
                    }
                }
                sb.append("Log ID: ").append(log.getUid())
                        .append("\nFood: ").append(foodName)
                        .append("\nPortion: ").append(log.getSize()).append("g\n\n");
            }

            String result = sb.toString();
            runOnUiThread(() -> historyTextView.setText(result));
        });

        ImageView buttonHome = findViewById(R.id.buttonHome);
        TextView textTitle = findViewById(R.id.textTitle);
        ImageView buttonSettings = findViewById(R.id.buttonSettings);

        buttonHome.setOnClickListener(v -> finish());

        textTitle.setOnClickListener(v -> finish());
    }
}
