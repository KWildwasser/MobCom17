package com.example.korbinian.strengthtester;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

import static com.example.korbinian.strengthtester.DebugActivity.isDebug;

public class MeasureMeanActivity extends AppCompatActivity {
    double[] gravity = new double[3];
    double[] linear_acceleration = new double[3];
    TextView textX, textY, textZ, textXmax, textYmax, textZmax, textScore, textMaxResolution;
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
    private DatabaseReference mDatabase;
    FirebaseAuth auth;
    UserScore userScore;
    ProgressBar simpleProgressBar = null;
    float myMaxScore = 0;
    float currentMaxScore = 0;

    FirebaseUserMetadata metadata;
    public void onButtonReset(View v) {
        reset();


    }

    public void reset() {
        max_x = 0;
        max_y = 0;
        max_z = 0;
        score = 0;
        currentMaxScore = 0;
        cnt = 0;
        is_measuring = true;
        time = SystemClock.currentThreadTimeMillis();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure_mean);
        simpleProgressBar =  (ProgressBar) findViewById(R.id.progressBar3); // initiate the progress bar

        auth = FirebaseAuth.getInstance();




        metadata = auth.getCurrentUser().getMetadata();
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

       // textMaxResolution.setText("max_range: " + sensor.getMaximumRange() );


        FirebaseDatabase database = FirebaseDatabase.getInstance();


        final DatabaseReference myRef = database.getReference("users").child(auth.getCurrentUser().getUid());

        if(myRef != null) {

            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists()) {
                        userScore = dataSnapshot.getValue(UserScore.class);
                        myMaxScore = Float.parseFloat(userScore.score);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };


            myRef.addValueEventListener(postListener);

        }

    }

    public void onResume() {
        super.onResume();
        sensorManager.registerListener(accelListener, sensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onButtonShare(View v) {
       share();
    }

    public void share() {
        // Write a message to the database

      /*  FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("score");
        String value = score + " " + auth.getCurrentUser().getDisplayName();
        myRef.setValue(value);


        myRef = database.getReference("score");
        value = score + " " + auth.getCurrentUser().getDisplayName();
        myRef.setValue(value); */

        if(score > myMaxScore) {
            myMaxScore = score;


            writeNewUserScore(auth.getCurrentUser().getUid(), auth.getCurrentUser().getDisplayName(), score + "");
            Toast myToast = Toast.makeText(getApplicationContext(), "New Highscore: " + score, Toast.LENGTH_LONG);
            myToast.show();
        } else {
            Toast myToast = Toast.makeText(getApplicationContext(), "No new Highscore", Toast.LENGTH_LONG);
            myToast.show();
        }
    }

    private void writeNewUserScore(String userId, String name, String score) {
        UserScore userScore = new UserScore(userId, name, score);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(userId).setValue(userScore);
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
            // max_y = (float) linear_acceleration[1];
            // max_z = (float) linear_acceleration[2];



        //    if(max_x > currentMaxScore) {
        //        currentMaxScore = max_x;
          //  } else {


                if (max_x < x) max_x = x;
                if (max_y < y) max_y = y;
                if (max_z < z) max_z = z;


                calcScore();
              //  share();
               // reset();
          //  }


if(isDebug) {
    textX.setText("X : " + x);
    textY.setText("Y : " + y);
    textZ.setText("Z : " + z);
    textXmax.setText("max_x : " + max_x);
    textYmax.setText("max_y : " + max_y);
    textZmax.setText("max_z : " + max_z);

    textMaxResolution.setText("max_range: " + sensor.getMaximumRange());
}


            textScore.setText("" + f.format(score));

            simpleProgressBar.setMax(1000); // 100 maximum value for the progress bar
            simpleProgressBar.setProgress((int)score); // 50 default progress value for the progress bar
           /* if (!isDebug) {
                textX.setText("");
                textY.setText("");
                textZ.setText("");
                textXmax.setText("");
                textYmax.setText("");
                textZmax.setText("");
                textMaxResolution.setText("");
            }
            */

        }


    };

    private void calcScore() {

        boolean measurement_complete = is_measuring;
        if (time + 100 > SystemClock.currentThreadTimeMillis()) {
            cnt++;
            score += Math.abs(x);
            textScore.setText("SHAKE IT!");
        } else {
            is_measuring = false;
        }

        if (measurement_complete & !is_measuring) {
            score /= cnt;
            textScore.setText("" + f.format(score));
        }

        // score = Math.abs(max_x)-30;
        // if (score < 0) score = 0;

    }
}

