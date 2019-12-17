package com.example.androidarielprojectapp.test;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidarielprojectapp.R;

public class NewRentalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_rental_activity);

        Intent intent = getIntent();

        String loc = intent.getStringExtra("RENTAL_LOCATION");
    }
}
