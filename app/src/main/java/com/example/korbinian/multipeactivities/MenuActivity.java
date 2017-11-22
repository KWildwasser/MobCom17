package com.example.korbinian.multipeactivities;

import android.content.Intent;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.*;

public class
MenuActivity extends AppCompatActivity {
    TextView text_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

    }

    public void onButtonStart(View v) {
        Intent measureActivity = new Intent(this, MeasureActivity.class);
        startActivity(measureActivity);
    }

    public void onButtonStartMax(View v) {
        Intent intent = new Intent(this, MeasureMaxActivity.class);
        startActivity(intent);
    }

    public void onButtonSettings(View v) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void onButtonLast(View v) {
        Intent intent = new Intent(this, MeasureMaxWithLast.class);
        startActivity(intent);
    }

    public void onButtonHighscore(View v) {
        Intent intent = new Intent(this, HighscoreActivity.class);
        startActivity(intent);
    }

    public void onButtonWriteToFirebase(View v) {
        // Write a message to the database

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("time");
        String value = String.valueOf(System.currentTimeMillis());
        myRef.setValue(value);
        Toast myToast = Toast.makeText(getApplicationContext(), "time: " + value, Toast.LENGTH_LONG);
        myToast.show();

    }
}
