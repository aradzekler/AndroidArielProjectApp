package com.example.androidarielprojectapp.test;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidarielprojectapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * activity for renting out and registering the rent.
 */
public class NewRentalActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private final int PICK_IMAGE_REQUEST = 1;
    private final int SCOOTER = 10;
    private final int BICYCLE = 20;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseStorage storage;
    StorageReference storageReference;
    String userID;
    String userPhone;
    int tool = 0; // storing vehicle id.
    int price = 0;
    private String imagePath = "";
    private Uri filePath;


    //TODO: image server for handling images.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_rental_activity);

        Button selectImage = (Button) findViewById(R.id.add_img_btn);
        Button registerVe = (Button) findViewById(R.id.reg_btn);
        final EditText phoneText = (EditText) findViewById(R.id.phone_text_input);
        final EditText priceText = (EditText) findViewById(R.id.price_text_input);
        final RadioButton scooterBtn = (RadioButton) findViewById(R.id.scooter_radio_btn);
        final RadioButton biBtn = (RadioButton) findViewById(R.id.bicycle_radio_btn);
        final DatabaseReference mDatabase;
        Intent intent = getIntent();
        final double[] loc = intent.getDoubleArrayExtra("RENTAL_LOCATION");
        final Toast rentRegisterToast = Toast.makeText(this, "Your rent has been registered.", Toast.LENGTH_LONG);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        //DocumentReference documentReference = fStore.collection("users").document(userID);
        userID = fAuth.getCurrentUser().getUid();
        userPhone = fAuth.getCurrentUser().getPhoneNumber();

        // when select image button is clicked.
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });


        // register the information and pass to database
        registerVe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NewRentalActivity.this);
                builder.setMessage(R.string.dialog_rent);

                // Set click listener for alert dialog buttons
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                String userPhone = phoneText.getText().toString();

                                if (!priceText.getText().toString().isEmpty()) { // if price is not an empty string.
                                    price = Integer.parseInt(priceText.getText().toString());
                                }
                                if (scooterBtn.isChecked()) {
                                    tool = SCOOTER;
                                } else if (biBtn.isChecked()) {
                                    tool = BICYCLE;
                                }
                                RegisterNewRentDataObject newRegisterObj = new RegisterNewRentDataObject(tool, price, loc[0],
                                        loc[1], userPhone, imagePath, userID); // creating data object and filling with data.
                                mDatabase.child("rents").child(newRegisterObj.getrentID()).setValue(newRegisterObj);
                                uploadImage(newRegisterObj);
                                rentRegisterToast.show();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                // User clicked the No button
                                break;
                        }
                    }
                };
                // Set the alert dialog yes button click listener
                builder.setPositiveButton(R.string.dialog_accept, dialogClickListener);
                builder.setNegativeButton(R.string.dialog_cancel, dialogClickListener);
                AlertDialog dialog = builder.create();
                dialog.show();


                // in order to read to data
                ValueEventListener postListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get Post object and use the values to update the UI
                        RegisterNewRentDataObject user = dataSnapshot.getValue(RegisterNewRentDataObject.class);
                        // not working yet.
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                        // ...
                    }
                };
                mDatabase.addValueEventListener(postListener);
            }

        });

    }

    // intent for choosing an image, launching onActivityResult.
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void uploadImage(RegisterNewRentDataObject object) {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/" + object.getrentID());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss(); // we are using cloud functions (Resize Images) to resize to 150x150
                            Toast.makeText(NewRentalActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(NewRentalActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }

    // activity for getting image from gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ImageView imageView = findViewById(R.id.imgView);
                imageView.setImageBitmap(bitmap);
                saveImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // saving the image to data object used inside onActivityResult
    private void saveImage(Bitmap resource) {
        String savedImagePath = null;
        String imageFileName = "image" + ".jpg";
        final File storageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/Pics");

        File imageFile = new File(storageDir, imageFileName);
        savedImagePath = imageFile.getAbsolutePath();
        try {
            OutputStream fOut = new FileOutputStream(imageFile);
            resource.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Add the image path to object
        imagePath = savedImagePath;
        Toast.makeText(this, "Image Saved", Toast.LENGTH_LONG).show();
    }


}