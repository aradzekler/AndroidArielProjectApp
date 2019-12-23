package com.example.androidarielprojectapp.test;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidarielprojectapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    //declaring relevant variables
    private static String TAG="RegisterActivity";
    EditText myFullName,myEmail,myPhoneNumber,myPassword;
    Button myRegisterBtn;
    TextView myLoginBtn;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //instantiate variables
        myFullName=findViewById(R.id.full_name);
        myEmail=findViewById(R.id.email);
        myPhoneNumber=findViewById(R.id.phone_number);
        myPassword=findViewById(R.id.password);
        myPassword.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        myRegisterBtn=findViewById(R.id.register_button);
        myLoginBtn=findViewById(R.id.already_registered);
        mAuth=FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progressBar);

        //if the user is already login, we adress him to the main activity.
        if(mAuth.getCurrentUser()!= null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
        //defining statements to be performed after user pressed on the register button
        myRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                String email,password;

                email=myEmail.getText().toString().trim();
                password=myPassword.getText().toString().trim();

                //if the user entered empty email adress , an error will appear.
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(Register.this, "email is required!", Toast.LENGTH_LONG).show();
                    myEmail.setError("email is required!");
                    return;
                }
                //if the user entered empty password , an error will appear.
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(Register.this, "password is required!", Toast.LENGTH_LONG).show();
                    myPassword.setError("password is required!");
                    return;
                }
                //if the user entered password containing less than 6 characters , an error will appear.
                if(password.length()<6){
                    Toast.makeText(Register.this, "password most be at least 6 characters!", Toast.LENGTH_LONG).show();
                    myPassword.setError("password most be at least 6 characters!");
                    return;
                }

                //set progress bar to be visible.
                progressBar.setVisibility(View.VISIBLE);
                //create account with a full name,email,phone number and password.
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(Register.this, "User created!", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "createUserWithEmail:success");
                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Register.this, "User creation failed!"+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    Log.w(TAG, "createUserWithEmail:failure"+task.getException().getMessage(), task.getException());
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        });

        //in case user  have an account , the activity of login will appear.
        myLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });


    }
    public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;
            public PasswordCharSequence(CharSequence source) {
                mSource = source; // Store char sequence
            }
            public char charAt(int index) {
                return '*'; // This is the important part
            }
            public int length() {
                return mSource.length(); // Return default
            }
            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end); // Return default
            }
        }
    };

}