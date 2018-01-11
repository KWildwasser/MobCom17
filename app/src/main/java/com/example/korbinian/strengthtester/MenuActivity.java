package com.example.korbinian.strengthtester;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class
MenuActivity extends AppCompatActivity {
    TextView text_info, textView;
    private static final int RC_SIGN_IN = 123;
    FirebaseAuth auth;
    FirebaseUserMetadata metadata;
    boolean newUser = false;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        auth = FirebaseAuth.getInstance();






        if (auth.getCurrentUser() != null) {
            // already signed in
            newUser = false;

        } else {
            // not signed in
            newUser = true;

            startActivityForResult(

                    // Get an instance of AuthUI based on the default app
                    AuthUI.getInstance().createSignInIntentBuilder().build(), RC_SIGN_IN);



        }

        if(newUser) {
           // userInit();
        }



      //  textView = (TextView) findViewById(R.id.textView);
      //  textView.setText(auth.getCurrentUser().getDisplayName());

    }
    public void onButtonStart(View v) {
        Intent measureActivity = new Intent(this, MeasureActivity.class);
        startActivity(measureActivity);
    }

    public void onButtonStartMax(View v) {
        Intent intent = new Intent(this, MeasureMaxActivity.class);
        startActivity(intent);
    }
    public void onButtonPlay(View v) {
        Intent intent = new Intent(this, PlayActivity.class);
        startActivity(intent);
    }

    public void onButtonSettings(View v) {
        Intent intent = new Intent(this, SimpleSettingsActivity.class);
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

    public void onButtonLogOff(View v) {
       // if (v.getId() == R.id.sign_out) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            // user is now signed out
                            startActivity(new Intent(MenuActivity.this, MenuActivity.class));
                            finish();
                        }
                    });
       // }
    }

    public void userInit() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!newUser) return;

                boolean inList = false;
                String thisUserId;
                thisUserId = auth.getCurrentUser().getUid();
                List<UserScore> userScoreList = new ArrayList<UserScore>();
                for(DataSnapshot scoreSnap: dataSnapshot.getChildren()) {

                    UserScore userScore = scoreSnap.getValue(UserScore.class);

                    userScoreList.add(userScore);

                }

                Collections.sort(userScoreList);

                for(UserScore userScore: userScoreList) {
                    if(userScore.userid == thisUserId) {
                        inList = true;
                    }

                 }

                 if(!inList) {


                     UserScore userScore = new UserScore(auth.getCurrentUser().getUid(), auth.getCurrentUser().getDisplayName(), "0");
                     mDatabase = FirebaseDatabase.getInstance().getReference();
                     mDatabase.child("users").child(auth.getCurrentUser().getUid()).setValue(userScore);

                 }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };



        myRef.addValueEventListener(postListener);










    }


}
