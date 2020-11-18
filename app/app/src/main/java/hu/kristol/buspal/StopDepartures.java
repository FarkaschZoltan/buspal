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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hu.farkasch.buspalbackend.datastructures.Time;
import hu.farkasch.buspalbackend.objects.BusRoute;
import hu.farkasch.buspalbackend.objects.Departure;
import hu.thepocok.adapters.DepartureAdapter;
import hu.thepocok.statements.Statements;

public class StopDepartures extends AppCompatActivity {
    private RequestQueue mRequestQueue;
    String url = hu.thepocok.serverlocation.ServerLocation.URL;

    List<BusRoute> routesList;

    //the recyclerview
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_departures);

        recyclerView = findViewById(R.id.departure_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent i = this.getIntent();
        Log.d("StopId", String.valueOf(i.getIntExtra("stop", 0)));
        int stopId = (int)i.getIntExtra("stop", 0);

        String stopName = i.getStringExtra("stopName");
        Log.d("StopName", stopName);

        TextView stopNameContainer = findViewById(R.id.stop_name);
        stopNameContainer.setText(stopName);

        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String currentDate = date.format(formatter);

        mRequestQueue = Volley.newRequestQueue(this);

        loadResources(url, "localhost", "postgres", "buspal", "budapest",
                Statements.getDepartureFromStop(stopId, Integer.parseInt(currentDate)));
        //Log.d("Routes", mRequestQueue.getCache().get(url).toString());
    }

    private void loadResources(String url, String host, String username, String password, String db, String statement){
        StringRequest sr = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Departures", response);
                        try {
                            LocalDateTime date = LocalDateTime.now();
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                            String currentTimeString = date.format(formatter);
                            Time currentTime = new Time(currentTimeString);
                            Log.d("Time", currentTime.toString());

                            //converting the string to json array object
                            JSONArray jsonArray = new JSONArray(response);
                            ArrayList<Departure> resultArray = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                int tripId = Integer.parseInt(jsonArray.getJSONObject(i).get("trip_id").toString());
                                String name = jsonArray.getJSONObject(i).get("route_short_name").toString();
                                Time departureTime = new Time(jsonArray.getJSONObject(i)
                                        .get("departure_time").toString());
                                String destination = jsonArray.getJSONObject(i).get("trip_headsign").toString();

                                Departure d = new Departure(tripId, name, destination, departureTime);

                                if(currentTime.isInsideInterval(new Time(d.departureTime), 3)) {
                                    resultArray.add(d);
                                    Log.d("Result", d.toString());
                                }
                            }

                            //creating adapter object and setting it to recyclerview
                            DepartureAdapter adapter = new DepartureAdapter(StopDepartures.this,
                                    resultArray, recyclerView);
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