package com.example.androidarielprojectapp.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.androidarielprojectapp.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * our main activity screen
 */
public class MainActivity extends AppCompatActivity {
    FirebaseAuth user;
    Button userRentButton;
    Button superRentButton;
    Button logInButton;
    Button logOutButton;
    boolean regStatus = true; // if the user is registered it will be turned on. false by default. true for testing.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        userRentButton = (Button) findViewById(R.id.userRentButton);
        superRentButton = (Button) findViewById(R.id.superRentButton);
        logInButton = (Button) findViewById(R.id.logInButton);
        logOutButton = (Button) findViewById(R.id.Logout);

        userRentButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(), UserRentActivity.class);
                startActivity(i);
            }
        });


        logInButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                    Intent i = new Intent(getApplicationContext(), Login.class);
                    startActivity(i);
            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(MainActivity.this, "User logged out successfully!", Toast.LENGTH_LONG).show();
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });

        superRentButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (regStatus) {
                    Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                    startActivity(i);
                }
                else {
                    Toast.makeText(MainActivity.this, "Register in order to rent.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


}
