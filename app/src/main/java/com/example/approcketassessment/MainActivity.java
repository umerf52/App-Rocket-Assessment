package com.example.approcketassessment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    public static final int LOGIN_REQUEST_CODE = 1;
    public static final int SIGNUP_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        restoreLogin();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Button loginButton = findViewById(R.id.login_button);
        Button signupButton = findViewById(R.id.signup_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivityForResult(intent, LOGIN_REQUEST_CODE);
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, SIGNUP_REQUEST_CODE);
            }
        });
    }

    // Call Back method  to get the Message form other Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == SIGNUP_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(getApplicationContext(), "You can now login", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Some error occurred during sign up", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == LOGIN_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String un = data.getStringExtra("username");

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("username", un);
                editor.putBoolean("status", true);
                editor.apply();

                launchHome(un);
            } else {
                Toast.makeText(getApplicationContext(), data.getStringExtra("msg"), Toast.LENGTH_LONG).show();
            }
        }
    }

    void restoreLogin() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String un = preferences.getString("username", "");
        boolean status = preferences.getBoolean("status", false);

        if (!un.equals("") && (status)) {
            launchHome(un);
        }
    }

    private void launchHome(String un) {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.putExtra("username", un);
        Toast.makeText(getApplicationContext(), "Logged In", Toast.LENGTH_LONG).show();
        startActivity(intent);
    }
}