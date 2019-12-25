package com.example.androidarielprojectapp.test;

import androidx.appcompat.app.AppCompatActivity;
import com.example.androidarielprojectapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import javax.annotation.Nullable;

public class UserProfileActivity extends AppCompatActivity {

    TextView headLine,fullName,email,phoneNumber,id;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    Button goBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        headLine=findViewById(R.id.headline);
        fullName=findViewById(R.id.userName);
        email=findViewById(R.id.userEmail);
        phoneNumber=findViewById(R.id.userPhone);
        id=findViewById(R.id.userId);
        goBack=findViewById(R.id.goBack);

        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        userID=fAuth.getCurrentUser().getUid();

        DocumentReference documentReference=fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                headLine.setText(documentSnapshot.getString("full name")+ " profile");
                fullName.setText("Full Name:  "+documentSnapshot.getString("full name"));
                email.setText("Email:  "+documentSnapshot.getString("email"));
                phoneNumber.setText("Phone Number:  "+documentSnapshot.getString("phone number"));
                id.setText("ID: "+userID);
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });



    }
}

