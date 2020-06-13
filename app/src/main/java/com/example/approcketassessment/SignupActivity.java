package com.example.approcketassessment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        final EditText username = findViewById(R.id.username);
        final EditText password = findViewById(R.id.password);
        Button signupButton = findViewById(R.id.button);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameText = username.getText().toString().trim();
                String passwordText = password.getText().toString().trim();
//                Toast.makeText(getApplicationContext(), usernameText + " " + passwordText, Toast.LENGTH_LONG).show();
            }
        });
    }
}