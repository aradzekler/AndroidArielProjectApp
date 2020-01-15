package com.example.androidarielprojectapp.test;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.androidarielprojectapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.annotation.Nullable;

public class UserProfileActivity extends AppCompatActivity {

    private final int IMAGE_GALLERY_REQUEST=20;
    private final int PERMISSION_REQUEST = 0;
    private final int RESULT_LOAD_IMAGE=1;
    ImageView userImage;
    TextView headLine, fullName, email, phoneNumber;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    Button goBack;
    Button addProfileImage;






    //todo: add a profile picture button and picture.
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_REQUEST);
        }

        userImage=findViewById(R.id.userPic);
        headLine = findViewById(R.id.headline);
        fullName = findViewById(R.id.userName);
        email = findViewById(R.id.userEmail);
        phoneNumber = findViewById(R.id.userPhone);
        //id = findViewById(R.id.userId);
        goBack = findViewById(R.id.goBack);
        addProfileImage = findViewById(R.id.add_profile_image_button);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        
        final RegisterNewRentDataObject user = new RegisterNewRentDataObject();
        user.setUserID(userID);

        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                headLine.setText(documentSnapshot.getString("full name"));
                fullName.setText("Full Name:  " + documentSnapshot.getString("full name"));
                email.setText("Email:  " + documentSnapshot.getString("email"));
                phoneNumber.setText("Phone Number:  " + documentSnapshot.getString("phone number"));
                //id.setText("ID: " + userID); no need to show the user the id.
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

        addProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i,RESULT_LOAD_IMAGE);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_REQUEST:{
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permission granted!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(this, "Permission not granted!", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    }



    protected void onActivityResult(int requestCode, int resultCode,Intent data) {

        if(resultCode== RESULT_OK){
            if(requestCode==IMAGE_GALLERY_REQUEST){
                Uri imageUri=data.getData();
                InputStream inputStream;
                try{
                    inputStream=getContentResolver().openInputStream(imageUri);
                    Bitmap image=BitmapFactory.decodeStream(inputStream);
                    userImage.setImageBitmap(image);
                }catch (FileNotFoundException e){
                    e.printStackTrace();
                    Toast.makeText(this, "Unable to open image", Toast.LENGTH_SHORT).show();
                }
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

