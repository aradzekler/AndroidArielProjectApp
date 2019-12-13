package com.example.androidarielprojectapp.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.androidarielprojectapp.R;

public class MainActivity extends AppCompatActivity {

    Button userRentButton;
    Button superRentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userRentButton = (Button)findViewById(R.id.userRentButton);
        superRentButton = (Button)findViewById(R.id.superRentButton);


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
    }


}
