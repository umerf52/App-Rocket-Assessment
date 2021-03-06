package com.example.approcketassessment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .build();
        db.setFirestoreSettings(settings);

        final EditText username = findViewById(R.id.username);
        final EditText password = findViewById(R.id.password);
        Button signupButton = findViewById(R.id.button);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String usernameText = username.getText().toString().trim();
                final String passwordText = password.getText().toString().trim();

                final AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                builder.setCancelable(false);

                // Set up the input
                final ProgressBar progressBar = new ProgressBar(SignupActivity.this, null, android.R.attr.progressBarStyleLarge);
                progressBar.setIndeterminate(true);
                progressBar.isShown();
                builder.setView(progressBar);

                final AlertDialog alertDialog = builder.show();

                db.collection("accounts")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (usernameText.equals(document.getString("username"))) {
                                            returnError(alertDialog);
                                        }
                                    }
                                    addUser(usernameText, passwordText, alertDialog);
                                } else {
                                    Log.w("signup", "Error getting documents.", task.getException());
                                    returnError(alertDialog);
                                }
                            }
                        });
            }
        });
    }

    void addUser(final String un, String pw, final AlertDialog ad) {
        Map<String, Object> user = new HashMap<>();
        user.put("username", un);
        user.put("password", pw);

        // Add a new document with a generated ID
        db.collection("accounts")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Intent intent = new Intent();
                        intent.putExtra("username", un);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("signup", "Failed");
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                        returnError(ad);
                    }
                });
    }

    void returnError(AlertDialog ad) {
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED, intent);
        ad.dismiss();
        finish();
    }
}