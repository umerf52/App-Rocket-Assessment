package com.example.approcketassessment;

import android.os.Bundle;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextClock clock = findViewById(R.id.clock);
        clock.setFormat12Hour("hh:mm a");

        TextView dateView = findViewById(R.id.date);
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy");    //formating according to my need
        String date = formatter.format(Calendar.getInstance().getTime());
        dateView.setText(date);
    }
}