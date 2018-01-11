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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class
SimpleSettingsActivity extends AppCompatActivity {
    TextView textView2;
    private static final int RC_SIGN_IN = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_settings);

        FirebaseAuth auth = FirebaseAuth.getInstance();
   /*     if (auth.getCurrentUser() != null) {
            // already signed in
        } else {
            // not signed in

            startActivityForResult(

                    // Get an instance of AuthUI based on the default app
                    AuthUI.getInstance().createSignInIntentBuilder().build(), RC_SIGN_IN);

        }*/

          textView2 = (TextView) findViewById(R.id.textView2);
          textView2.setText(auth.getCurrentUser().getDisplayName());

    }


    public void onButtonWriteToFirebase(View v) {
        // Write a message to the database

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("time");
        String value = String.valueOf(System.currentTimeMillis());
        myRef.setValue(value);
        Toast myToast = Toast.makeText(getApplicationContext(), getString(R.string.pref_debug_firebase_systemtime) + " " + value, Toast.LENGTH_LONG);
        myToast.show();

    }

    public void onButtonDebug(View v) {
        Intent intent = new Intent(this, DebugActivity.class);
        startActivity(intent);
    }

    public void onButtonLogOff(View v) {
        // if (v.getId() == R.id.sign_out) {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // user is now signed out
                        startActivity(new Intent(SimpleSettingsActivity.this, MenuActivity.class));
                        finish();
                    }
                });
        // }
    }

}
