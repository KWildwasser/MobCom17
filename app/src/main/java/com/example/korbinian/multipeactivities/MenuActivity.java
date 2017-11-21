package com.example.korbinian.multipeactivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class
MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public  void onButtonStart(View v) {
        Intent measureActivity = new Intent(this, MeasureActivity.class);
        startActivity(measureActivity);
    }

    public  void onButtonStartMax(View v) {
        Intent intent = new Intent(this, MeasureMaxActivity.class);
        startActivity(intent);
    }

    public  void onButtonSettings(View v) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
