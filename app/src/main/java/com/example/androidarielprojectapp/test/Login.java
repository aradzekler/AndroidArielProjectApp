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

public class Login extends AppCompatActivity {

    //declaring relevant variables
    private static String TAG;
    EditText myEmail,myPassword;
    Button myLoginBtn;
    TextView myCreateAccountBtn;
    FirebaseAuth myAuth;
    ProgressBar myProgressBar;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TAG="LoginActivity";
        //instantiate variables
        myEmail=findViewById(R.id.email);
        myPassword=findViewById(R.id.password);
        myPassword.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        myCreateAccountBtn=findViewById(R.id.create_account);
        myLoginBtn=findViewById(R.id.login_button);
        myAuth=FirebaseAuth.getInstance();
        myProgressBar=findViewById(R.id.progressBar);

        //if the user is already login, we adress him to the main activity.
        if(myAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
        //defining statements to be performed after user pressed on the login button
        myLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email,password;

                email=myEmail.getText().toString().trim();
                password=myPassword.getText().toString().trim();


                //if the user entered empty email adress , an error will appear.
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(Login.this, "email is required!", Toast.LENGTH_LONG).show();
                    myEmail.setError("email is required!");
                    return;
                }
                //if the user entered empty password , an error will appear.
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(Login.this, "password is required!", Toast.LENGTH_LONG).show();
                    myPassword.setError("password is required!");
                    return;
                }
                //if the user entered password containing less than 6 characters , an error will appear.
                if(password.length()<6){
                    Toast.makeText(Login.this, "password most be at least 6 characters!", Toast.LENGTH_LONG).show();
                    myPassword.setError("password most be at least 6 characters!");
                    return;
                }
                //set progress bar to be visible.
                myProgressBar.setVisibility(View.VISIBLE);

                //login with an email and a password .
                myAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(Login.this, "User logged in succesfuly!", Toast.LENGTH_LONG).show();
                                    Log.d(TAG, "createUserWithEmail:success");
                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Login.this, "User login failed!" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    myProgressBar.setVisibility(View.GONE);
                                }
                            }
                        });

            }
        });

        //in case user dont have an account yet, the activity of creating account will appear.
        myCreateAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
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
