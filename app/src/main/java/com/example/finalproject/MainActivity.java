package com.example.finalproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import java.util.ArrayList;
import android.content.Intent;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Final project: Main";
    private static RequestQueue requestQueue;
    private TextView locations;
    private List<String> locationList = new ArrayList<>();
    private String locationListToShow = "";
    //public final static String MESSAGE_LAT = "com.example.finalproject.lat";
    private ArrayList<Double> latList = new ArrayList<>();
    private ArrayList<Double> lngList = new ArrayList<>();
    //private ArrayList<List<Integer>> latLng = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        requestQueue = Volley.newRequestQueue(this);

        locations = findViewById(R.id.locations);
        TextInputEditText inputLocations = findViewById(R.id.inputLocations);

        final Button input = findViewById(R.id.input);
        input.setOnClickListener(v -> {
            Log.d(TAG, "input location one by one");
            String location = inputLocations.getText().toString();
            locationList.add(location);
            for (int i = 0; i < locationList.size(); i++) {
                locationListToShow += " " + locationList.get(i);
                locations.setText(locationListToShow);
                locationList = new ArrayList<>();
            }


            String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + location + "&key=AIzaSyBCb13EAk46_yoo6SSjmJ-sm27xmqe514w";
            StringRequest jsonObjectRequest = new StringRequest
                     (Request.Method.GET, url,  new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println(response);
                            //System.out.println(lngList);
                            JsonParser parser = new JsonParser();
                            JsonObject jsonResult = parser.parse(response).getAsJsonObject();
                            double lat = jsonResult.get("results").getAsJsonArray().get(0).getAsJsonObject().get("geometry").getAsJsonObject().get("location").getAsJsonObject().get("lat").getAsDouble();
                            double lng = jsonResult.get("results").getAsJsonArray().get(0).getAsJsonObject().get("geometry").getAsJsonObject().get("location").getAsJsonObject().get("lng").getAsDouble();
                            latList.add(0, lat);
                            lngList.add(0, lng);
                        }

                    },null);
            requestQueue.add(jsonObjectRequest);

        });

        final Button Mark = findViewById(R.id.Mark);
        Mark.setOnClickListener(v -> {
            Log.d(TAG, "mark locations on the map");
            Intent startMapActivity = new Intent(this, MapsActivity.class);
            startMapActivity.putExtra("latList", latList);
            startMapActivity.putExtra("lngList", lngList);
            startActivity(startMapActivity);
        });
    }






}
