package hu.kristol.buspal;

import static hu.thepocok.serverlocation.ServerLocation.URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import hu.farkasch.buspalbackend.datastructures.Coordinates;
import hu.farkasch.buspalbackend.datastructures.Time;
import hu.farkasch.buspalbackend.objects.BusStop;
import hu.farkasch.buspalbackend.objects.BusTrip;
import hu.farkasch.buspalbackend.objects.Departure;
import hu.thepocok.adapters.RouteStopsAdapter;
import hu.thepocok.statements.Statements;

public class RouteStops extends AppCompatActivity {
    private RequestQueue mRequestQueue;

    private BusTrip trip;
    private JSONArray jsonArray;
    private int routeType;
    private String routeName;
    private Context context = RouteStops.this;

    private RecyclerView recyclerView;

    private String city;
    private float radius;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_stops);

        recyclerView = findViewById(R.id.route_stops_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(RouteStops.this));

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        city = sharedPreferences.getString("city", "budapest");
        radius = sharedPreferences.getFloat("radius", (float) 1.0);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Window window = this.getWindow();
        Log.d("Context", context.toString());

        Intent i = this.getIntent();
        Log.d("tripId", String.valueOf(i.getIntExtra("tripId", 0)));
        int tripId = (int)i.getIntExtra("tripId", 0);

        routeName = i.getStringExtra("routeName");
        TextView stopNameContainer = findViewById(R.id.route_name);
        stopNameContainer.setText(routeName);

        routeType = (int)i.getIntExtra("routeType", -1);
        Log.d("routeType", String.valueOf(routeType));
        switch (routeType){
            case 0:
                toolbar.setBackgroundColor(this.getResources().getColor(R.color.tram));
                window.setStatusBarColor(this.getResources().getColor(R.color.tram_dark));
                break;
            case 1:
                if(routeName.equals("M1")){
                    toolbar.setBackgroundColor(this.getResources().getColor(R.color.metro_1));
                    window.setStatusBarColor(this.getResources().getColor(R.color.metro_1_dark));
                }else if(routeName.equals("M2")){
                    toolbar.setBackgroundColor(this.getResources().getColor(R.color.metro_2));
                    window.setStatusBarColor(this.getResources().getColor(R.color.metro_2_dark));
                }else if(routeName.equals("M3")){
                    toolbar.setBackgroundColor(this.getResources().getColor(R.color.metro_3));
                    window.setStatusBarColor(this.getResources().getColor(R.color.metro_3_dark));
                }else if(routeName.equals("M4")){
                    toolbar.setBackgroundColor(this.getResources().getColor(R.color.metro_4));
                    window.setStatusBarColor(this.getResources().getColor(R.color.metro_4_dark));
                }
                break;
            case 3:
                if(routeName.charAt(0) == '9' && routeName.length() >= 3){
                    toolbar.setBackgroundColor(this.getResources().getColor(R.color.night_bus));
                    window.setStatusBarColor(this.getResources().getColor(R.color.night_bus_dark));
                } else{
                    toolbar.setBackgroundColor(this.getResources().getColor(R.color.bus));
                    window.setStatusBarColor(this.getResources().getColor(R.color.bus_dark));
                }

                break;
            case 11:
            case 800:
                toolbar.setBackgroundColor(this.getResources().getColor(R.color.trolley));
                window.setStatusBarColor(this.getResources().getColor(R.color.trolley_dark));
                break;
            case 109:
                if(routeName.equals("H5")){
                    toolbar.setBackgroundColor(this.getResources().getColor(R.color.suburban_5));
                    window.setStatusBarColor(this.getResources().getColor(R.color.suburban_5_dark));
                }else if(routeName.equals("H6")){
                    toolbar.setBackgroundColor(this.getResources().getColor(R.color.suburban_6));
                    window.setStatusBarColor(this.getResources().getColor(R.color.suburban_6_dark));
                }else if(routeName.equals("H7")){
                    toolbar.setBackgroundColor(this.getResources().getColor(R.color.suburban_7));
                    window.setStatusBarColor(this.getResources().getColor(R.color.suburban_7_dark));
                }else if(routeName.equals("H8") || routeName.equals("H9")){
                    toolbar.setBackgroundColor(this.getResources().getColor(R.color.suburban_8));
                    window.setStatusBarColor(this.getResources().getColor(R.color.suburban_8_dark));
                }
                break;
            default:
                toolbar.setBackgroundColor(this.getResources().getColor(R.color.bus));
                window.setStatusBarColor(this.getResources().getColor(R.color.bus_dark));
                break;
        }

        mRequestQueue = Volley.newRequestQueue(this);

        loadResources(URL, city, Statements.getScheduleByTripId(tripId));
    }

    private void loadResources(String url, String db, String statement){
        StringRequest sr = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RouteStops", response);
                        try {
                            //converting the string to json array object
                            jsonArray = new JSONArray(response);
                            ArrayList<Departure> resultArray = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                int tripId = Integer.parseInt(jsonArray.getJSONObject(i).get("trip_id").toString());
                                int stopId = Integer.parseInt(jsonArray.getJSONObject(i).get("stop_id").toString());
                                double stopLat = Double.parseDouble(jsonArray.getJSONObject(i)
                                        .get("stop_lat").toString());
                                double stopLon= Double.parseDouble(jsonArray.getJSONObject(i)
                                        .get("stop_lon").toString());
                                String stopName = jsonArray.getJSONObject(i).get("stop_name").toString();
                                Time departureTime = new Time(jsonArray.getJSONObject(i)
                                        .get("departure_time").toString());
                                int stop_sequence = Integer.parseInt(jsonArray.getJSONObject(i)
                                        .get("stop_sequence").toString());

                                if(i == 0){
                                    trip = new BusTrip(tripId);
                                    Button showOnMap = findViewById(R.id.show_on_map);
                                    showOnMap.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent i = new Intent(context, hu.kristol.buspal.Map.class);
                                            try{
                                                i.putExtra("shapeId", Integer.parseInt(jsonArray.getJSONObject(0)
                                                        .get("shape_id").toString()));
                                            } catch (JSONException e){
                                                Log.d("ShapeIdNotFound", e.getLocalizedMessage());
                                            }
                                            i.putExtra("routeType", routeType);
                                            i.putExtra("routeName", routeName);
                                            i.putExtra("stopLat", stopLat);
                                            i.putExtra("stopLon", stopLon);
                                            context.startActivity(i);
                                        }
                                    });
                                }
                                BusStop bs = new BusStop(stopId, stopName, new Coordinates(stopLat, stopLon),
                                        stop_sequence, departureTime);
                                Log.d("Result", bs.toString());
                                trip.addBusStop(bs);
                            }

                            //creating adapter object and setting it to recyclerview
                            RouteStopsAdapter adapter = new RouteStopsAdapter(RouteStops.this,
                                    trip.getBusStopList(), recyclerView);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("HttpClient", "error: " + error.toString());
                    }
                }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("database", db);
                params.put("statement", statement);
                return params;
            }
        };
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        sr.setRetryPolicy(policy);
        mRequestQueue.add(sr);
    }
}