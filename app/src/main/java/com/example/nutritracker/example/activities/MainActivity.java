package com.example.nutritracker.example.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import com.example.nutritracker.R;
import com.example.nutritracker.example.db.FoodDatabase;
import com.example.nutritracker.example.db.dbPopulator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        new dbPopulator().insertAll();

        FoodDatabase db = Room.databaseBuilder(getApplicationContext(),
                        FoodDatabase.class, "FoodDatabase")
                .allowMainThreadQueries()
                .build();

        AppCompatButton buttonFood = findViewById(R.id.buttonFood);
        AppCompatButton buttonWeight = findViewById(R.id.buttonWeight);
        AppCompatButton buttonWater = findViewById(R.id.buttonWater);

        ImageView buttonSettings = findViewById(R.id.buttonSettings);

        buttonSettings.setOnClickListener(v -> {


        });

        buttonFood.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, FoodActivity.class)));

        buttonWeight.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, WeightActivity.class)));

        buttonWater.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, WaterActivity.class)));
    }
}