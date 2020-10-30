package hu.kristol.buspal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
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
import java.util.List;
import java.util.Map;

import hu.farkasch.buspalbackend.datastructures.Coordinates;
import hu.farkasch.buspalbackend.objects.BusStop;
import hu.thepocok.adapters.StopsAdapter;
import hu.thepocok.statements.Statements;

public class Stops extends AppCompatActivity implements LocationListener {
    private RequestQueue mRequestQueue;
    String url = "http://80.98.90.176:9876/";

    List<Stops> stopsList;
    LocationManager locationManager;
    boolean locationFound = false;

    String stopName = null;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stops);

        mRequestQueue = Volley.newRequestQueue(this);

        recyclerView = findViewById(R.id.stops_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent i = this.getIntent();
        stopName = i.getStringExtra("stopName");

        if(stopName != null){
            loadResources(url, "localhost", "postgres", "buspal", "budapest", Statements.getStopsByName(stopName));
        }

        if(ActivityCompat.checkSelfPermission(Stops.this,
        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            getLocation();
        }
        else{
            ActivityCompat.requestPermissions(Stops.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }

    private void loadResources(String url, String host, String username, String password, String db, String statement){
        StringRequest sr = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Stops", response);
                        try {
                            //converting the string to json array object
                            JSONArray jsonArray = new JSONArray(response);
                            ArrayList<BusStop> resultArray = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                int stopId = Integer.parseInt(jsonArray.getJSONObject(i).get("stop_id").toString());
                                String name = jsonArray.getJSONObject(i).get("stop_name").toString();
                                double lat = Double.parseDouble(jsonArray.getJSONObject(i).get("stop_lat").toString());
                                double lon = Double.parseDouble(jsonArray.getJSONObject(i).get("stop_lon").toString());
                                double distance;
                                try {
                                    distance = Double.parseDouble(jsonArray.getJSONObject(i).get("distance").toString());
                                } catch(JSONException e){
                                    distance = -1;
                                }

                                Coordinates c = new Coordinates(lat, lon);

                                BusStop b = new BusStop(stopId, name, c, distance);
                                resultArray.add(b);
                                Log.d("Result", b.toString());
                            }

                            //creating adapter object and setting it to recyclerview
                            StopsAdapter adapter = new StopsAdapter(Stops.this, resultArray, recyclerView);
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
                })
        {
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
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        sr.setRetryPolicy(policy);
        mRequestQueue.add(sr);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        if(locationFound == false && stopName == null){
            loadResources(url, "localhost", "postgres", "buspal", "budapest", Statements.getNearbyStops(location.getLatitude(), location.getLongitude(), 0.5));
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