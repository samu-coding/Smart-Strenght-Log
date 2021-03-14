package com.example.smartstrengthlog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.email_login);
        passwordEditText = findViewById(R.id.password_email_login);


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        Log.i("User",""+currentUser);
    }

    public void signWithEmailAndPassword(String email, String password){

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                            //Vamos a la vista principal
                            //-------------------------->
                            Toast.makeText(LoginActivity.this, "Welcome back!",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainMenu.class));
                            finish();


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                            // ...
                        }

                        // ...
                    }
                });

    }


    public void buttonPress (View view){

        String email = emailEditText.getText().toString();
        String pass = passwordEditText.getText().toString();

        if (!email.isEmpty() && !pass.isEmpty()){
            if(pass.length()>5){

                // Hacemos Log in
                signWithEmailAndPassword(email, pass);

            }else {
                Toast.makeText(this, "Password should be at least 6 characters long", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(this, "Please, fill all the fields.", Toast.LENGTH_LONG).show();
        }

    }

    public void createAccountButton (View view){

        startActivity(new Intent(this, CreateAccountActivity.class));
        finish();

    }


}