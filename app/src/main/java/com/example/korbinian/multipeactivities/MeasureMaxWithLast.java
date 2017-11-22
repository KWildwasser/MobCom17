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
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;

public class MeasureMaxWithLast extends AppCompatActivity {
    double[] gravity = new double[3];
    double[] linear_acceleration = new double[3];
    TextView textX, textY, textZ, textXmax, textYmax, textZmax, textScore;
    SensorManager sensorManager;
    Sensor sensor;
    static float last_x = 0;
    float max_x = 0;
    float max_y = 0;
    float max_z = 0;
    static float x, y, z;
    static float score = 0;
    static float scoreDiff = 0;
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
        scoreDiff = 0;
        is_measuring = true;
        time = SystemClock.currentThreadTimeMillis();


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure_max);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

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

    public void onButtonShare(View v) {
        // Write a message to the database

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("score");
        String value = score + "";
        myRef.setValue(value);
        Toast myToast = Toast.makeText(getApplicationContext(), "score: " + value, Toast.LENGTH_LONG);
        myToast.show();
    }

    public void onStop() {
        super.onStop();
        sensorManager.unregisterListener(accelListener);
    }

    SensorEventListener accelListener = new SensorEventListener() {

        public void onAccuracyChanged(Sensor sensor, int acc) {
        }

        public void onSensorChanged(SensorEvent event) {
            // alpha is calculated as t / (t + dT)
            // with t, the low-pass filter's time-constant
            // and dT, the event delivery rate

            final double alpha = 0.8;

            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

            linear_acceleration[0] = event.values[0] - gravity[0];
            linear_acceleration[1] = event.values[1] - gravity[1];
            linear_acceleration[2] = event.values[2] - gravity[2];


            x = event.values[0];
            y = event.values[1];
            z = event.values[2];

            // max_x = (float) linear_acceleration[0];
            //  max_y = (float) linear_acceleration[1];
            // max_z = (float) linear_acceleration[2];


            textX.setText("X : " + x);
            textY.setText("Y : " + y);
            textZ.setText("Z : " + z);

            if (max_x < x) max_x = x;
            if (max_y < y) max_y = y;
            if (max_z < z) max_z = z;


            calcScore();

            textScore.setText("" + f.format(score));
            textXmax.setText("lin_x : " + max_x);
            textYmax.setText("lin_y : " + max_y);
            textZmax.setText("lin_z : " + max_z);


        }
    };

    private void calcScore() {


        scoreDiff = Math.abs(x - last_x);
        last_x = x;
        if (score < scoreDiff) score = scoreDiff;


    }
}
