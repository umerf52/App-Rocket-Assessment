package com.example.approcketassessment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .build();
        db.setFirestoreSettings(settings);

        final EditText username = findViewById(R.id.username);
        final EditText password = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String usernameText = username.getText().toString().trim();
                final String passwordText = password.getText().toString().trim();

                final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setCancelable(false);

                // Set up the input
                final ProgressBar progressBar = new ProgressBar(LoginActivity.this, null, android.R.attr.progressBarStyleLarge);
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
                                        if ((usernameText.equals(document.getString("username"))) &&
                                                (passwordText.equals(document.getString("password")))) {
                                            Intent intent = new Intent();
                                            intent.putExtra("username", usernameText);
                                            setResult(Activity.RESULT_OK, intent);
                                            alertDialog.dismiss();
                                            finish();
                                        }
                                    }
                                    Intent intent = new Intent();
                                    intent.putExtra("msg", "Wrong username/password combo");
                                    setResult(Activity.RESULT_CANCELED, intent);
                                    alertDialog.dismiss();
                                    finish();
                                } else {
                                    Log.w("signup", "Error getting documents.", task.getException());
                                    Intent intent = new Intent();
                                    intent.putExtra("msg", "Some error occured");
                                    setResult(Activity.RESULT_CANCELED, intent);
                                    alertDialog.dismiss();
                                    finish();
                                }
                            }
                        });
            }
        });
    }
}