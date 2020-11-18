package hu.kristol.buspal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    private String url = hu.thepocok.serverlocation.ServerLocation.URL;

    private BusTrip trip;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_stops);

        recyclerView = findViewById(R.id.route_stops_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent i = this.getIntent();
        Log.d("tripId", String.valueOf(i.getIntExtra("tripId", 0)));
        int tripId = (int)i.getIntExtra("tripId", 0);

        String routeName = i.getStringExtra("routeName");

        TextView stopNameContainer = findViewById(R.id.route_name);
        stopNameContainer.setText(routeName);

        mRequestQueue = Volley.newRequestQueue(this);

        loadResources(url, "localhost", "postgres", "buspal", "budapest", Statements.getScheduleByTripId(tripId));
    }

    private void loadResources(String url, String host, String username, String password, String db, String statement){
        StringRequest sr = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RouteStops", response);
                        try {
                            //converting the string to json array object
                            JSONArray jsonArray = new JSONArray(response);
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
                params.put("host", host);
                params.put("username", username);
                params.put("password", password);
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