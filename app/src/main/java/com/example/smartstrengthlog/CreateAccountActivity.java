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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import util.SmartStrengthLogAPI;

public class CreateAccountActivity extends AppCompatActivity {

    //Firebase Auth
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    //FireStore
    private FirebaseFirestore  db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");

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

        // Inicializamos Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                currentUser = firebaseAuth.getCurrentUser();

            }
        };

    }

    public void onStart() {
        super.onStart();
        // Comprobar si el usuario signed in (non-null) y actualizar UI.
        currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    private void updateUI(FirebaseUser currentUser) {
        Log.i("User:",""+currentUser);
    }

    public void createUserWithEmailAndPassword (String email, String password){
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SUCCESS", "createUserWithEmail:success");
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    updateUI(user);

                    //Firestore creation
                    assert user != null;
                    String userId = user.getUid(); //Obtenemos el Id del usuario

                    Map <String, String> userObj = new HashMap<>();
                    userObj.put("userId", userId);
                    userObj.put("username", email );

                    //save to FireStore db
                    collectionReference.add(userObj)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    documentReference.get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.getResult().exists()){
                                                        String name = task.getResult()
                                                                .getString("username");

                                                        SmartStrengthLogAPI smartStrengthLogAPI = SmartStrengthLogAPI.getInstance(); //Global API
                                                        smartStrengthLogAPI.setUserId(userId);
                                                        smartStrengthLogAPI.setUsername(email);

                                                        //Cambio de vista
                                                        Toast.makeText(CreateAccountActivity.this, "Welcome to Smart Strength Log!", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(CreateAccountActivity.this,
                                                                MainMenu.class);
                                                        intent.putExtra("username", email);
                                                        intent.putExtra("userId", userId);
                                                        intent.putExtra("fragmentToLoad", "Home");
                                                        startActivity(intent);
                                                        finish();


                                                    }else {

                                                    }
                                                }
                                            });

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });

                } else {
                    Log.w("ERROR", "createUserWithEmail:failure", task.getException());
                    Toast.makeText(CreateAccountActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }

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



