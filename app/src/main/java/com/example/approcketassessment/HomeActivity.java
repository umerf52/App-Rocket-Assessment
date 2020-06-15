package com.example.approcketassessment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.androdocs.httprequest.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HomeActivity extends AppCompatActivity {

    private TextView currView, maxView, minView, statusView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new weatherTask().execute();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextClock clock = findViewById(R.id.clock);
        clock.setFormat12Hour("hh:mm a");

        TextView dateView = findViewById(R.id.date);
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy");    //formating according to my need
        String date = formatter.format(Calendar.getInstance().getTime());
        dateView.setText(date);

        statusView = findViewById(R.id.status_view);
        currView = findViewById(R.id.curr_temp);
        maxView = findViewById(R.id.max_temp);
        minView = findViewById(R.id.min_temp);
        Button reloadButton = findViewById(R.id.rld_button);

        reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new weatherTask().execute();
            }
        });

        CardView chatCard = findViewById(R.id.chat_card);
        chatCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChatScreen.class);
                startActivity(intent);
            }
        });
    }

    class weatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {
            String API_KEY = "de8e230f66ed1c3714f9e17d1f3173ee";
            String LOCATION = "Lahore,PK";
            String response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?q=" + LOCATION + "&units=metric&appid=" + API_KEY);
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObj = new JSONObject(result);
                JSONObject main = jsonObj.getJSONObject("main");
                JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);

                String currStatus = weather.getString("main");
                String temp = main.getString("temp") + "C";
                String tempMin = main.getString("temp_min");
                String tempMax = main.getString("temp_max");

                statusView.setText(currStatus);
                currView.setText(temp);
                minView.setText(tempMin);
                maxView.setText(tempMax);
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Error fetching data", Toast.LENGTH_LONG).show();
                Log.d("TAG", "exception");
            }
        }
    }
}