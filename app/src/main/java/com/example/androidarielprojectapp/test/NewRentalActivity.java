package com.example.androidarielprojectapp.test;

import android.content.Intent;
import android.graphics.Bitmap;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidarielprojectapp.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

/**
 * new rental activity
 */
public class NewRentalActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private final int PICK_IMAGE_REQUEST = 1;
    private final int SCOOTER = 10;
    private final int BICYCLE = 20;

    final File myDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/SmartPhoto");
    boolean success = false;
    int tool = 00; // storing vehicle id.
    protected RegisterNewRentDataObject nrd = new RegisterNewRentDataObject(); // building our data object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_rental_activity);

        Button selectImage = (Button) findViewById(R.id.add_img_btn);
        Button registerVe = (Button) findViewById(R.id.reg_btn);
        final EditText notesText = (EditText) findViewById(R.id.notes_text_input);
        final EditText priceText = (EditText) findViewById(R.id.price_text_input);
        final RadioGroup rg = findViewById(R.id.type_radio_group);
        final RadioButton scooterBtn = (RadioButton) findViewById(R.id.scooter_radio_btn);
        final RadioButton biBtn = (RadioButton) findViewById(R.id.bicycle_radio_btn);


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
                //TODO: db link
                String notes = notesText.getText().toString();
                String price = priceText.getText().toString();

                if (rg.getCheckedRadioButtonId() == -1) {
                    // no radio buttons are checked
                } else if (scooterBtn.isChecked()) {
                    tool = SCOOTER;
                } else if (biBtn.isChecked()) {
                    tool = BICYCLE;
                }
                nrd.setNotes(notes);
                nrd.setPrice(price);
            }
        });
    }

    // intent for choosing an image, launching onActivityResult.
    public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    // activity for getting image from gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

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
        String imageFileName =  "image" + ".jpg";


        final File storageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/Pics");

        boolean success = true;
        if(!storageDir.exists()){
            success = storageDir.mkdirs();
        }

        if(success){
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
            nrd.setImagePath(savedImagePath);
            Toast.makeText(this, "IMAGE SAVED", Toast.LENGTH_LONG).show();
        }
    }
}