package com.example.nutritracker.example.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.nutritracker.R;

public class WaterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_water);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.water), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView buttonHome = findViewById(R.id.buttonHome);
        TextView textTitle = findViewById(R.id.textTitle);
        ImageView buttonSettings = findViewById(R.id.buttonSettings);

        buttonHome.setOnClickListener(v -> finish());

        textTitle.setOnClickListener(v -> finish());

        buttonSettings.setOnClickListener(v -> {


        });
    }
}