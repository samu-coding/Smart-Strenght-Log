package com.example.smartstrengthlog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

public class CreateAccountActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText rePasswordEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createaccount);

        emailEditText = findViewById(R.id.email_CreateAccount);
        passwordEditText = findViewById(R.id.password_CreateAccount);
        rePasswordEditText = findViewById(R.id.repassword_CreateAccount);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        Log.i("User:",""+currentUser);
    }

    public void createUserWithEmailAndPassword (String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SUCCESS", "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);

                    //Mandar a una nueva vista:
                    //------------->>!!
                    startActivity(new Intent(CreateAccountActivity.this, MainMenu.class));
                    finish();

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("ERROR", "createUserWithEmail:failure", task.getException());
                    Toast.makeText(CreateAccountActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }

                // ...
            }
        });

    }

    @SuppressLint("ShowToast")
    public void buttonPress (View view){

        Log.i("User:","CLICK en boton");

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String rePassword = rePasswordEditText.getText().toString();


        Log.i("User:",""+email);
        Log.i("User:",""+password);
        Log.i("User:",""+rePassword);

        if (!email.isEmpty() && !password.isEmpty() && !rePassword.isEmpty()){

            if (password.equals(rePassword)){
                if(password.length()>5){

                    //Creamos usuario
                    createUserWithEmailAndPassword(email, password);

                }else {
                    Toast.makeText(this, "Password should be at least 6 characters long", Toast.LENGTH_SHORT).show();
                }

            }else {
                Toast.makeText(this, "Passwords don´t match.", Toast.LENGTH_LONG).show();
            }

        }else {

            Toast.makeText(this, "Please, fill all the fields.", Toast.LENGTH_LONG).show();

        }

    }

    public void signInButton(View view){

        startActivity(new Intent(this, LoginActivity.class));
        finish();

    }

}



