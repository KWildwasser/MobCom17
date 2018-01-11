package com.example.korbinian.strengthtester;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by korbinian on 06.01.18.
 */

public class DebugActivity extends AppCompatActivity {
    double[] gravity = new double[3];
    double[] linear_acceleration = new double[3];
    TextView textX, textY, textZ, textXmax, textYmax, textZmax, textScore, textMaxResolution;
    SensorManager sensorManager;
    Sensor sensor;
    float max_x = 0;
    float max_y = 0;
    float max_z = 0;
    static float x, y, z;
    Switch switchButton, switchButton2;
    static public boolean isDebug = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        textX = (TextView) findViewById(R.id.textX);
        textY = (TextView) findViewById(R.id.textY);
        textZ = (TextView) findViewById(R.id.textZ);

        textXmax = (TextView) findViewById(R.id.textXmax);
        textYmax = (TextView) findViewById(R.id.textYmax);
        textZmax = (TextView) findViewById(R.id.textZmax);

        textScore = (TextView) findViewById(R.id.textScore);
        textMaxResolution = (TextView) findViewById(R.id.textMaxResolution);

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

    public void onButtonWriteToFirebase(View v) {
        // Write a message to the database

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("time");
        String value = String.valueOf(System.currentTimeMillis());
        myRef.setValue(value);
        Toast myToast = Toast.makeText(getApplicationContext(), "time: " + value, Toast.LENGTH_LONG);
        myToast.show();

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
            // max_y = (float) linear_acceleration[1];
            // max_z = (float) linear_acceleration[2];



            //    if(max_x > currentMaxScore) {
            //        currentMaxScore = max_x;
            //  } else {


            if (max_x < x) max_x = x;
            if (max_y < y) max_y = y;
            if (max_z < z) max_z = z;



            textX.setText("X : " + x);
            textY.setText("Y : " + y);
            textZ.setText("Z : " + z);
            textXmax.setText("max_x : " + max_x);
            textYmax.setText("max_y : " + max_y);
            textZmax.setText("max_z : " + max_z);

            textMaxResolution.setText("max_range: " + sensor.getMaximumRange());




        }


    };



}
