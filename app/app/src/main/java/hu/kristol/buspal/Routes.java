package hu.kristol.buspal;


import static hu.thepocok.serverlocation.ServerLocation.URL;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hu.farkasch.buspalbackend.objects.BusRoute;
import hu.thepocok.adapters.RouteAdapter;
import hu.thepocok.statements.Statements;

public class Routes extends AppCompatActivity {
    private RequestQueue mRequestQueue;

    private String city;
    private float radius;

    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRequestQueue = Volley.newRequestQueue(this);

        recyclerView = findViewById(R.id.routes_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        city = sharedPreferences.getString("city", "budapest");
        radius = sharedPreferences.getFloat("radius", (float) 1.0);

        Intent i = this.getIntent();
        String name = i.getStringExtra("name");

        String title = "Routes stopping at \"" + name + "\"";
        toolbar.setTitle(title);

        loadResources(URL, city, Statements.getRouteByName(name));

    }

    private void loadResources(String url, String db, String statement){
        StringRequest sr = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Routes", response);
                        try {
                            //converting the string to json array object
                            JSONArray jsonArray = new JSONArray(response);
                            ArrayList<BusRoute> resultArray = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                String routeId = jsonArray.getJSONObject(i).get("route_id").toString();
                                String name = jsonArray.getJSONObject(i).get("route_short_name").toString();
                                String type = jsonArray.getJSONObject(i).get("route_type").toString();

                                String destination = null;
                                ArrayList<String> destinations = null;
                                Log.d("City", city);

                                if(city.equals("budapest")){
                                    destination = jsonArray.getJSONObject(i).get("route_desc").toString();
                                    destinations = new ArrayList<>(Arrays.asList(destination.split(" / ")));
                                } else if(city.equals("szeged")){
                                    destination = jsonArray.getJSONObject(i).get("route_long_name").toString();
                                    destinations = new ArrayList<>(Arrays.asList(destination.split(" / ")));
                                } else{
                                    destination = jsonArray.getJSONObject(i).get("route_long_name").toString();
                                    destinations = new ArrayList<>(Arrays.asList(destination.split(" - ")));
                                }

                                BusRoute b = new BusRoute(routeId, name, type, destinations.get(0), Routes.this, 0);
                                BusRoute bb = new BusRoute(routeId, name, type, destinations.get(1), Routes.this, 1);
                                resultArray.add(b);
                                resultArray.add(bb);
                                Log.d("Result", b.toString());
                                Log.d("Result", bb.toString());
                            }

                            //creating adapter object and setting it to recyclerview
                            RouteAdapter adapter = new RouteAdapter(Routes.this, resultArray, recyclerView);
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