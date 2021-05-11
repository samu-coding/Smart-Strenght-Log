package com.example.smartstrengthlog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smartstrengthlog.ui.dashboard.DashboardFragment;
import com.example.smartstrengthlog.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import util.SmartStrengthLogAPI;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;

    private FirebaseAuth firebaseAuth;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.email_login);
        passwordEditText = findViewById(R.id.password_email_login);

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        Log.i("User",""+currentUser);
    }

    public void signWithEmailAndPassword(String email, String password){

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in con éxito
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI(user);

                            assert user != null;
                            String currentUserId = user.getUid(); //Obtenemos el Id del usuario

                            //Buscamos el Id del usuario que toque
                            collectionReference.whereEqualTo("userId",currentUserId)
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                                            if (error != null){

                                            }
                                            assert value != null;
                                            if(!value.isEmpty()){
                                                //Bucle para escoger el usuario correcto
                                                for (QueryDocumentSnapshot snapshot : value){
                                                    SmartStrengthLogAPI smartStrengthLogAPI = SmartStrengthLogAPI.getInstance();
                                                    smartStrengthLogAPI.setUsername(snapshot.getString("username"));
                                                    smartStrengthLogAPI.setUserId(currentUserId);
                                                }
                                            }
                                        }

                                    });

                            //Cambio de vista
                            Intent intent = new Intent(LoginActivity.this,
                                    MainMenu.class);
                            intent.putExtra("username", email);
                            intent.putExtra("userId", currentUserId);
                            intent.putExtra("fragmentToLoad", "Home");
                            startActivity(intent);

                            Toast.makeText(LoginActivity.this, "Welcome back!", Toast.LENGTH_SHORT).show();

                            finish();


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Incorrect credentials, try again.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

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
                Toast.makeText(this, "Password should be at least 6 characters long.", Toast.LENGTH_LONG).show();
            }

        }else{
            Toast.makeText(this, "Please, fill all the fields.", Toast.LENGTH_LONG).show();
        }

    }

    public void createAccountButton (View view){

        startActivity(new Intent(this, CreateAccountActivity.class));
        finish();

    }

    @Override
    public void onBackPressed() {
        //Cambio de vista
        Intent intent = new Intent(this,
                MainActivity.class);
        SmartStrengthLogAPI smartStrengthLogAPI = new SmartStrengthLogAPI();
        startActivity(intent);
    }


}