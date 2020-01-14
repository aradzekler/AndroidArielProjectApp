package com.example.androidarielprojectapp.test;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidarielprojectapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserRentActivity extends AppCompatActivity {
    private final double SCOOTER = 10;
    private final double BICYCLE = 20;
    private final double MINIMUM_PRICE = 0;
    private final double MAXIMUM_PRICE = 999;
    private final String TAG = "UserRentActivity";
    DatabaseReference mDataBase;
    Query query;
    RegisterNewRentDataObject userObject;
    ArrayList<RegisterNewRentDataObject> userObjectQueryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_rent); // change layout
        Button Search = (Button) findViewById(R.id.SearchBtn);
        final CheckBox scooterCheck = (CheckBox) findViewById(R.id.scooter_check_box);
        final CheckBox biCheck = (CheckBox) findViewById(R.id.bicycle_check_box);
        final EditText startPrice = (EditText) findViewById(R.id.StartPrice);
        final EditText endPrice = (EditText) findViewById(R.id.EndPrice);
        final Toast check_Box_Error = Toast.makeText(this, "you have to check al least 1 vehicle.", Toast.LENGTH_LONG);
        mDataBase= FirebaseDatabase.getInstance().getReference("rents");

        //TODO: all those if statements are a mess
        //queries are fixed + cleaned up unneeded if statements
        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userObjectQueryList.clear();
                if(!biCheck.isChecked()&&!scooterCheck.isChecked())
                {
                    check_Box_Error.show();
                }
                else if(biCheck.isChecked()&&!scooterCheck.isChecked()) // choose only bicycles
                {
                        query =mDataBase.orderByChild("tool").equalTo(BICYCLE);
                }
                else if(!biCheck.isChecked()&&scooterCheck.isChecked())
                {
                        query =mDataBase.orderByChild("tool").equalTo(SCOOTER); //choose only scooters
                }
                else{                                                      //choose all vehicles
                        query =mDataBase.orderByChild("price");
                }
                // getting query to next map activity.
                try {
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            double startPriceDouble = MINIMUM_PRICE;
                            double endPriceDouble = MAXIMUM_PRICE;
                            if (!startPrice.getText().toString().isEmpty()) {
                                startPriceDouble = Double.parseDouble(startPrice.getText().toString());
                            }
                            if (!endPrice.getText().toString().isEmpty()) {
                                endPriceDouble = Double.parseDouble(endPrice.getText().toString());
                            }
                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                userObject = singleSnapshot.getValue(RegisterNewRentDataObject.class);
                                if(userObject.getprice()<=endPriceDouble&&userObject.getprice()>= startPriceDouble) //filter price range
                                {
                                    userObjectQueryList.add(userObject);
                                    System.out.println(userObject.getuserPhone());
                                }

                            }
                            Intent i = new Intent(getApplicationContext(), MapsRentalActivity.class);
                            i.putExtra("QUERY_USERS", userObjectQueryList);
                            startActivity(i);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e(TAG, "onCancelled", databaseError.toException());
                        }
                    });
                } catch (NullPointerException npe) {
                    System.out.println("QUERY IS NULL");
                    npe.printStackTrace();
                }
            }
        });

    }

}