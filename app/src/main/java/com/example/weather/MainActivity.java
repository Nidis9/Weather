package com.example.weather;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {


    TextView dateTV;
    TextView cityTV;
    TextView tempTV;
    TextView weatherDescriptionTV;
    ImageView weatherIV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateTV = (TextView) findViewById(R.id.dateTV);
        cityTV = (TextView) findViewById(R.id.cityTV);
        tempTV = (TextView) findViewById(R.id.tempTV);
        weatherDescriptionTV = (TextView) findViewById(R.id.weatherDescriptionTV);

        dateTV.setText(getCurrentDate());
        weatherIV = (ImageView) findViewById(R.id.weatherIV);

        String url = "https://api.openweathermap.org/data/2.5/weather?q=Thessaloniki&appid=9cee8a2c29ff2f6075bcc104b285ce4a&units=metric";

        JsonObjectRequest jsObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject responseObject) {
                        //tempTV.setText("Response: " + response.toString());
                        Log.v("WEATHER","Response: " + responseObject.toString());

                        try {
                            JSONObject mainJSONObject = responseObject.getJSONObject("main");
                            JSONArray weatherArray = responseObject.getJSONArray("weather");
                            JSONObject firstWeatherObject = weatherArray.getJSONObject(0);

                            String temp = Integer.toString ((int) Math.round(mainJSONObject.getDouble("temp")));
                            String weatherDescription = firstWeatherObject.getString("description");
                            String city = responseObject.getString("name");

                            tempTV.setText(temp);
                            weatherDescriptionTV.setText(weatherDescription);
                            cityTV.setText(city);

                            int iconResourceId = getResources().getIdentifier("icon_" + weatherDescription.replace(" ", ""), "drawable", getPackageName());
                            weatherIV.setImageResource(iconResourceId);

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsObjectRequest);
    }
    private String getCurrentDate () {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd");
        String formattedDate = dateFormat.format(calendar.getTime());

        return formattedDate;
    }
}
