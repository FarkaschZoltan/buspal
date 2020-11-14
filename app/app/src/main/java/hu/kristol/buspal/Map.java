package hu.kristol.buspal;

import hu.farkasch.buspalbackend.datastructures.Coordinates;
import hu.farkasch.buspalbackend.datastructures.Time;
import hu.farkasch.buspalbackend.objects.BusStop;
import hu.farkasch.buspalbackend.objects.BusRoute;
import hu.thepocok.statements.Statements;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
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
import org.osmdroid.config.Configuration;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hu.thepocok.statements.Statements;

public class Map extends AppCompatActivity implements LocationListener {
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    public static final GeoPoint MISKOLC = new GeoPoint(48.10367, 20.79933);
    public static final GeoPoint BUDAPEST = new GeoPoint(47.497913, 19.040236);
    private MapView map = null;
    private RequestQueue mRequestQueue;
    private Context mCtx = this;
    private ArrayList<OverlayItem> stopsOnMap = new ArrayList<OverlayItem>();
    private boolean locationFound = false;
    private LocationManager mLocationManager;
    private IMapController mapController = null;
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            if(locationFound == false){
                map.getOverlays().clear();
                mapController.setCenter(new GeoPoint(location.getLatitude(), location.getLongitude()));
                loadResources(hu.thepocok.serverlocation.ServerLocation.URL, "localhost", "postgres", "buspal", "budapest", Statements.getNearbyStops(location.getLatitude(), location.getLongitude(), 1));
                locationFound = true;
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration.getInstance().load(mCtx, PreferenceManager.getDefaultSharedPreferences(mCtx));
        mRequestQueue = Volley.newRequestQueue(mCtx);

        setContentView(R.layout.activity_map);

        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setClickable(true);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        mapController = map.getController();
        mapController.setZoom(16.0);
        //GeoPoint startPoint = new GeoPoint( 47.497913, 19.040236);
        mapController.setCenter(BUDAPEST);

        /*this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getApplicationContext()),map);
        this.mLocationOverlay.enableMyLocation();
        map.getOverlays().add(this.mLocationOverlay);*/

        /*Marker marker = new Marker(map);
        marker.setPosition(busStop);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

        map.getOverlays().clear();
        map.getOverlays().add(marker);
        map.invalidate();*/

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, mLocationListener);
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
                                }
                                else {
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
                                        distance = Double.parseDouble(jsonArray.getJSONObject(i).get("distance").toString());
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


                            for(BusStop s : resultArray){
                                Log.d("Stop", s.toString());
                                OverlayItem olItem = new OverlayItem(s.getStopName(), "Buszmegálló", new GeoPoint(s.getCords().getLat(), s.getCords().getLon()));
                                Log.d("Point", olItem.getPoint().toString());
                                stopsOnMap.add(olItem);
                            }

                            ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(stopsOnMap,
                                    mCtx.getResources().getDrawable(R.drawable.bus_stop_icon),
                                    mCtx.getResources().getDrawable(R.drawable.bus_stop_icon),
                                    0,  new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                                @Override
                                public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                                    // TODO dialog
                                    return true;
                                }

                                @Override
                                public boolean onItemLongPress(final int index, final OverlayItem item) {

                                    return false;
                                }
                            }, mCtx);
                            map.getOverlays().add(mOverlay);
                            locationFound = false;
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
            protected java.util.Map<String,String> getParams(){
                java.util.Map<String,String> params = new HashMap<String, String>();
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
    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onPause() {
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if(locationFound == false){
            loadResources(hu.thepocok.serverlocation.ServerLocation.URL, "localhost", "postgres", "buspal", "budapest", Statements.getNearbyStops(location.getLatitude(), location.getLongitude(), 1));
            locationFound = true;
        }
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }
}