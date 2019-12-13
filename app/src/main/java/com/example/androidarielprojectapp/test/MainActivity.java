package com.example.androidarielprojectapp.test;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.androidarielprojectapp.R;

/**
 * our main activity screen
 *
 */
public class MainActivity extends AppCompatActivity {

    Button userRentButton;
    Button superRentButton;
    Button logInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userRentButton = (Button)findViewById(R.id.userRentButton);
        superRentButton = (Button)findViewById(R.id.superRentButton);
        logInButton = (Button)findViewById(R.id.logInButton);


        userRentButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(),UserRentActivity.class);
                startActivity(i);
            }
        });


        superRentButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(),SuperRentActivity.class);
                startActivity(i);
            }
        });


        logInButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(),LogInActivity.class);
                startActivity(i);
            }
        });


    }



}
