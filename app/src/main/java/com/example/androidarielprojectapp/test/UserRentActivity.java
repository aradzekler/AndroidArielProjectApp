package com.example.androidarielprojectapp.test;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidarielprojectapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class UserRentActivity extends AppCompatActivity {
    private final double SCOOTER = 10;
    private final double BICYCLE = 20;
    DatabaseReference mDataBase;
    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_rent); // change layout
        Button Search = (Button) findViewById(R.id.SearchBtn);
        final CheckBox scooterCheck = (CheckBox) findViewById(R.id.scooter_check_box);
        double start_Price;
        double end_Price;
        final CheckBox biCheck = (CheckBox) findViewById(R.id.bicycle_check_box);
        final EditText startPrice = (EditText) findViewById(R.id.StartPrice);
        final EditText endPrice = (EditText) findViewById(R.id.EndPrice);
        final Toast check_Box_Error = Toast.makeText(this, "you have to check al least 1 vehicle.", Toast.LENGTH_LONG);
        mDataBase= FirebaseDatabase.getInstance().getReference("rents");
        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double start_Price=Double.parseDouble(startPrice.toString());
                double end_Price=Double.parseDouble(endPrice.toString());
                if(!biCheck.isChecked()&&!scooterCheck.isChecked())
                {
                    check_Box_Error.show();
                }
                else if(biCheck.isChecked()&&!scooterCheck.isChecked())
                {
                    if (startPrice.getText().toString().isEmpty() && endPrice.getText().toString().isEmpty()) {
                        query =FirebaseDatabase.getInstance().getReference("rents")
                                .orderByChild("price").equalTo(BICYCLE);

                    }
                    else if (!startPrice.getText().toString().isEmpty() && endPrice.getText().toString().isEmpty())
                    {
                        query =FirebaseDatabase.getInstance().getReference("rents")
                                .orderByChild("price")
                                .equalTo(BICYCLE)
                                .startAt(start_Price);
                    }
                    else if(startPrice.getText().toString().isEmpty() && !endPrice.getText().toString().isEmpty())
                    {
                        query =FirebaseDatabase.getInstance().getReference("rents")
                                .orderByChild("price")
                                .equalTo(BICYCLE)
                                .endAt(end_Price);
                    }
                    else{
                        query =FirebaseDatabase.getInstance().getReference("rents")
                                .orderByChild("price")
                                .equalTo(BICYCLE)
                                .startAt(start_Price)
                                .endAt(end_Price);
                    }
                }
                else if(!biCheck.isChecked()&&scooterCheck.isChecked())
                {
                    if (startPrice.getText().toString().isEmpty() && endPrice.getText().toString().isEmpty()) {
                        query =FirebaseDatabase.getInstance().getReference("rents")
                                .orderByChild("price").equalTo(SCOOTER);

                    }
                    else if (!startPrice.getText().toString().isEmpty() && endPrice.getText().toString().isEmpty())
                    {
                        query =FirebaseDatabase.getInstance().getReference("rents")
                                .orderByChild("price")
                                .equalTo(SCOOTER)
                                .startAt(start_Price);
                    }
                    else if(startPrice.getText().toString().isEmpty() && !endPrice.getText().toString().isEmpty())
                    {
                        query =FirebaseDatabase.getInstance().getReference("rents")
                                .orderByChild("price")
                                .equalTo(SCOOTER)
                                .endAt(end_Price);
                    }
                    else{
                        query =FirebaseDatabase.getInstance().getReference("rents")
                                .orderByChild("price")
                                .startAt(start_Price)
                                .endAt(end_Price);
                    }
                }
                else{           //choose all vehicles
                    if (startPrice.getText().toString().isEmpty() && endPrice.getText().toString().isEmpty()) {
                        query =FirebaseDatabase.getInstance().getReference("rents")
                                .orderByChild("price");

                    }
                    else if (!startPrice.getText().toString().isEmpty() && endPrice.getText().toString().isEmpty())
                    {
                        query =FirebaseDatabase.getInstance().getReference("rents")
                                .orderByChild("price")
                                .startAt(start_Price);
                    }
                    else if(startPrice.getText().toString().isEmpty() && !endPrice.getText().toString().isEmpty())
                    {
                        query =FirebaseDatabase.getInstance().getReference("rents")
                                .orderByChild("price")
                                .endAt(end_Price);
                    }
                    else{
                        query =FirebaseDatabase.getInstance().getReference("rents")
                                .orderByChild("price")
                                .startAt(start_Price)
                                .endAt(end_Price);
                    }
                }

            }
        });


    }

}