package com.example.korbinian.multipeactivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.database.*;


public class HighscoreActivity extends AppCompatActivity {
    TextView text_hs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);
        text_hs = (TextView) findViewById(R.id.text_highscore);
        displayHighscore();
    }



    private void displayHighscore() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("score");

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                text_hs.setText(String.valueOf(dataSnapshot.getValue()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };



        myRef.addValueEventListener(postListener);
    }
}
