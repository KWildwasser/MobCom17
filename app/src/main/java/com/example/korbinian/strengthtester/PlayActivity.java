package com.example.korbinian.strengthtester;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.auth.*;

/**
 * Created by korbinian on 06.01.18.
 */

public class PlayActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;

    FirebaseAuth auth;
    FirebaseUserMetadata metadata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
    }

    public void onButtonStartMean(View v) {
        Intent measureMeanActivity = new Intent(this, MeasureActivity.class);
        startActivity(measureMeanActivity);
    }

    public void onButtonStartMax(View v) {
        Intent intent = new Intent(this, MeasureMaxActivity.class);
        startActivity(intent);
    }
}
