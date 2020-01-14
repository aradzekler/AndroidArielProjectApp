package com.example.androidarielprojectapp.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.androidarielprojectapp.R;

public class ImageDialog extends Activity {

    private ImageView mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image);
        mDialog = (ImageView)findViewById(R.id.dialog_imageview);
        mDialog.setClickable(true);


        //finish the activity (dismiss the image dialog) if the user clicks
        //anywhere on the image
        mDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}