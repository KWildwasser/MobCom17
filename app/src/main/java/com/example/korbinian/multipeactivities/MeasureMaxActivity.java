package com.example.korbinian.multipeactivities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MeasureMaxActivity extends AppCompatActivity {


    TextView textX, textY, textZ, textXmax, textYmax, textZmax, textScore;
    SensorManager sensorManager;
    Sensor sensor;
    float max_x = 0;
    float max_y = 0;
    float max_z = 0;
    static float x, y, z;
    static float score = 0;
    static double time;
    static int cnt = 0;
    static boolean is_measuring = false;
    DecimalFormat f = new DecimalFormat("#0.00");

    public void onButtonReset(View v) {
        max_x = 0;
        max_y = 0;
        max_z = 0;
        score = 0;
        cnt = 0;
        is_measuring = true;
        time = SystemClock.currentThreadTimeMillis();


    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure_max);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        textX = (TextView) findViewById(R.id.textX);
        textY = (TextView) findViewById(R.id.textY);
        textZ = (TextView) findViewById(R.id.textZ);

        textXmax = (TextView) findViewById(R.id.textXmax);
        textYmax = (TextView) findViewById(R.id.textYmax);
        textZmax = (TextView) findViewById(R.id.textZmax);

        textScore = (TextView) findViewById(R.id.textScore);


    }

    public void onResume() {
        super.onResume();
        sensorManager.registerListener(accelListener, sensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onStop() {
        super.onStop();
        sensorManager.unregisterListener(accelListener);
    }

    SensorEventListener accelListener = new SensorEventListener() {

        public void onAccuracyChanged(Sensor sensor, int acc) { }

        public void onSensorChanged(SensorEvent event) {
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];



            textX.setText("X : " + (int)x);
            textY.setText("Y : " + (int)y);
            textZ.setText("Z : " + (int)z);

            if(max_x < x) max_x = x;
            if(max_y < y) max_y = y;
            if(max_z < z) max_z = z;



            calcScore();

            textScore.setText("" +  f.format(score));
            textXmax.setText("X_max : " + (int)max_x);
            textYmax.setText("Y_max : " + (int)max_y);
            textZmax.setText("Z_max : " + (int)max_z);


        }
    };

    private void calcScore() {



        score = Math.abs(max_x);

    }
}
