package com.example.korbinian.strengthtester;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        DatabaseReference myRef = database.getReference("users");

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String Highscores = "";
                List<UserScore> userScoreList = new ArrayList<UserScore>();
                for(DataSnapshot scoreSnap: dataSnapshot.getChildren()) {

                    UserScore userScore = scoreSnap.getValue(UserScore.class);

                    userScoreList.add(userScore);

                }

                Collections.sort(userScoreList);

                for(UserScore userScore: userScoreList) {
                    Highscores += userScore.username + " " + userScore.score + "\n";
                }
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                text_hs.setText(Highscores);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };



        myRef.addValueEventListener(postListener);
    }
}
