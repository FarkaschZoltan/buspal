package hu.kristol.buspal;

import static hu.thepocok.serverlocation.ServerLocation.URL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

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
import java.util.Locale;
import java.util.Map;

import hu.farkasch.buspalbackend.datastructures.Coordinates;
import hu.farkasch.buspalbackend.objects.BusRoute;
import hu.farkasch.buspalbackend.objects.BusStop;
import hu.thepocok.adapters.StopsAdapter;
import hu.thepocok.statements.Statements;

public class Stops extends AppCompatActivity implements LocationListener {
    private RequestQueue mRequestQueue;

    private LocationManager locationManager;
    private boolean locationFound = false;

    private String stopName = null;
    private RecyclerView recyclerView;

    private String city;
    private float radius;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stops);

        mRequestQueue = Volley.newRequestQueue(this);

        recyclerView = findViewById(R.id.stops_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        city = sharedPreferences.getString("city", "budapest");
        radius = sharedPreferences.getFloat("radius", (float) 1.0);

        Intent i = this.getIntent();
        stopName = i.getStringExtra("stopName");

        if(stopName != null){
            loadResources(URL, city,
                    Statements.getStopsByNameWithRoutes(stopName.toLowerCase(Locale.getDefault())));
        }

        if(ActivityCompat.checkSelfPermission(Stops.this,
        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            getLocation();
        } else{
            ActivityCompat.requestPermissions(Stops.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }

    private void loadResources(String url, String db, String statement){
        StringRequest sr = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Stops", response);
                        try {
                            //converting the string to json array object
                            JSONArray jsonArray = new JSONArray(response);
                            ArrayList<BusStop> resultArray = new ArrayList<>();
                            ArrayList<BusRoute> busRoutes = new ArrayList<>();
                            int stopId = -1;
                            int lastStopId = -1;
                            String name = null;
                            double lat = 0;
                            double lon = 0;
                            double distance = 999;
                            Coordinates c = null;

                            for (int i = 0; i < jsonArray.length(); i++) {
                                stopId = Integer.parseInt(jsonArray.getJSONObject(i).get("stop_id").toString());

                                if(lastStopId == stopId){
                                    String routeName = jsonArray.getJSONObject(i).get("route_short_name").toString();
                                    String routeType = jsonArray.getJSONObject(i).get("route_type").toString();
                                    BusRoute r = new BusRoute(routeName, routeType);
                                    busRoutes.add(r);
                                    Log.d("RoutesArray", busRoutes.toString());
                                    continue;
                                } else {
                                    if(i != 0) {
                                        BusStop b = new BusStop(lastStopId, name, c, distance, busRoutes);
                                        resultArray.add(b);
                                        Log.d("Result", b.toString());
                                    }

                                    lastStopId = stopId;
                                    busRoutes = new ArrayList<>();
                                    name = jsonArray.getJSONObject(i).get("stop_name").toString();
                                    lat = Double.parseDouble(jsonArray.getJSONObject(i).get("stop_lat").toString());
                                    lon = Double.parseDouble(jsonArray.getJSONObject(i).get("stop_lon").toString());
                                    try {
                                        distance = Double.parseDouble(jsonArray.getJSONObject(i)
                                                .get("distance").toString());
                                    } catch (JSONException e) {
                                        distance = -1;
                                    }

                                    String routeName = jsonArray.getJSONObject(i).get("route_short_name").toString();
                                    String routeType = jsonArray.getJSONObject(i).get("route_type").toString();
                                    BusRoute r = new BusRoute(routeName, routeType);
                                    busRoutes.add(r);

                                    c = new Coordinates(lat, lon);
                                }
                                Log.d("RoutesArray", busRoutes.toString());
                            }

                            BusStop b = new BusStop(stopId, name, c, distance, busRoutes);
                            resultArray.add(b);
                            Log.d("Result", b.toString());

                            //creating adapter object and setting it to recyclerview
                            StopsAdapter adapter = new StopsAdapter(Stops.this, resultArray, recyclerView);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setNestedScrollingEnabled(false);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if(requestCode == 44){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLocation();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,Stops.this);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }catch (SecurityException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        if(!locationFound && stopName == null){
            loadResources(URL, city,
                    Statements.getNearbyStops(location.getLatitude(), location.getLongitude(), radius));
            locationFound = true;
        }

        /*Toast.makeText(this, ""+location.getLatitude()+","+location.getLongitude(), Toast.LENGTH_SHORT).show();
        try {
            Geocoder geocoder = new Geocoder(Stops.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            String address = addresses.get(0).getAddressLine(0);

        }catch (Exception e){
            e.printStackTrace();
        }*/

    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }
}