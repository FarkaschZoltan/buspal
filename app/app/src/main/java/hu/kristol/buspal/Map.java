package hu.kristol.buspal;

import static hu.thepocok.serverlocation.ServerLocation.URL;

import hu.farkasch.buspalbackend.datastructures.Coordinates;
import hu.farkasch.buspalbackend.objects.BusStop;
import hu.farkasch.buspalbackend.objects.BusRoute;
import hu.thepocok.statements.Statements;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;

import org.osmdroid.config.Configuration;
import org.osmdroid.api.IMapController;
import org.osmdroid.events.DelayedMapListener;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.HashMap;


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
    private double currentLat = 0;
    private double currentLon = 0;
    ItemizedOverlayWithFocus<OverlayItem> mOverlay;

    private int routeType;
    private String routeName;
    private boolean showingPath;

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            if(!locationFound && !showingPath){
                map.getOverlays().clear();
                mapController.setCenter(new GeoPoint(location.getLatitude(), location.getLongitude()));
                loadResources(URL, "budapest",
                        Statements.getNearbyStops(location.getLatitude(), location.getLongitude(),
                                1));
                locationFound = true;
            }
            currentLat = location.getLatitude();
            currentLon = location.getLongitude();
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
        mapController.setCenter(BUDAPEST);

        Intent i = this.getIntent();
        int shapeId = i.getIntExtra("shapeId", -1);
        routeType = i.getIntExtra("routeType", -1);
        routeName = i.getStringExtra("routeName");

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1,
                1, mLocationListener);

        FloatingActionButton myLocation = findViewById(R.id.my_location);
        myLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapController.setCenter(new GeoPoint(currentLat, currentLon));
                mapController.setZoom(16.0);
            }
        });

        FloatingActionButton nearbyStopsList = findViewById(R.id.nearby_stops_list);
        nearbyStopsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mCtx, Stops.class);
                mCtx.startActivity(i);
            }
        });

        map.setMapListener(new DelayedMapListener(new MapListener() {
            public boolean onZoom(final ZoomEvent e) {
                if(e.getZoomLevel() < 16){
                    map.getOverlays().clear();
                } else{
                    map.getOverlays().add(mOverlay);
                }
                return true;
            }

            public boolean onScroll(final ScrollEvent e) {
                Log.i("zoom", e.toString());
                return true;
            }
        }, 1000 ));

        if(shapeId != -1){
            loadRouteShape(URL, "budapest",
                    Statements.getShape(shapeId));
        }
    }

    private void loadRouteShape(String url, String db, String statement){
        StringRequest sr = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Stops", response);
                        try {
                            //converting the string to json array object
                            JSONArray jsonArray = new JSONArray(response);
                            ArrayList<GeoPoint> shapePoints = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                shapePoints.add(new GeoPoint(0, 0));
                            }

                            for (int i = 0; i < jsonArray.length(); i++) {
                                double lat = Double.parseDouble(jsonArray.getJSONObject(i).get("shape_pt_lat")
                                        .toString());
                                double lon = Double.parseDouble(jsonArray.getJSONObject(i).get("shape_pt_lon")
                                        .toString());
                                int sequencePlace = Integer.parseInt(jsonArray.getJSONObject(i).get("shape_pt_sequence")
                                        .toString());
                                shapePoints.set(sequencePlace, new GeoPoint(lat, lon));
                            }

                            int c = 0;
                            switch (routeType){
                                case 0:
                                    c = mCtx.getResources().getColor(R.color.tram);
                                    break;
                                case 1:
                                    if(routeName.equals("M1")){
                                        c = mCtx.getResources().getColor(R.color.metro_1);
                                    }else if(routeName.equals("M2")){
                                        c = mCtx.getResources().getColor(R.color.metro_2);
                                    }else if(routeName.equals("M3")){
                                        c = mCtx.getResources().getColor(R.color.metro_3);
                                    }else if(routeName.equals("M4")){
                                        c = mCtx.getResources().getColor(R.color.metro_4);
                                    }
                                    break;
                                case 3:
                                    if(routeName.charAt(0) == '9' && routeName.length() >= 3){
                                        c = mCtx.getResources().getColor(R.color.night_bus);
                                    } else{
                                        c = mCtx.getResources().getColor(R.color.bus);
                                    }

                                    break;
                                case 11:
                                case 800:
                                    c = mCtx.getResources().getColor(R.color.trolley);
                                    break;
                                case 109:
                                    if(routeName.equals("H5")){
                                        c = mCtx.getResources().getColor(R.color.suburban_5);
                                    }else if(routeName.equals("H6")){
                                        c = mCtx.getResources().getColor(R.color.suburban_6);
                                    }else if(routeName.equals("H7")){
                                        c = mCtx.getResources().getColor(R.color.suburban_7);
                                    }else if(routeName.equals("H8") || routeName.equals("H9")){
                                        c = mCtx.getResources().getColor(R.color.suburban_8);
                                    }
                                    break;
                                default:
                                    c = mCtx.getResources().getColor(R.color.bus);
                                    break;
                            }

                            Polyline shape = new Polyline();
                            shape.setPoints(shapePoints);
                            shape.setColor(c);
                            shape.setWidth((float) 15.0);
                            shape.setOnClickListener(new Polyline.OnClickListener() {
                                @Override
                                public boolean onClick(Polyline polyline, MapView mapView, GeoPoint eventPos) {
                                    return false;
                                }
                            });

                            map.getOverlayManager().add(shape);
                            showingPath = true;

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
            protected java.util.Map<String,String> getParams(){
                java.util.Map<String,String> params = new HashMap<String, String>();
                params.put("database", db);
                params.put("statement", statement);
                return params;
            }
        };
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        sr.setRetryPolicy(policy);
        mRequestQueue.add(sr);
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


                            for(BusStop s : resultArray){
                                Log.d("Stop", s.toString());
                                OverlayItem olItem = new OverlayItem(s.getStopName(), "Buszmegálló",
                                        new GeoPoint(s.getCords().getLat(), s.getCords().getLon()));
                                Log.d("Point", olItem.getPoint().toString());
                                stopsOnMap.add(olItem);
                            }

                            mOverlay =
                                    new ItemizedOverlayWithFocus<OverlayItem>(stopsOnMap,
                                    mCtx.getResources().getDrawable(R.drawable.bus_stop),
                                    mCtx.getResources().getDrawable(R.drawable.bus_stop),
                                    0,  new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                                @Override
                                public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                                    Intent i = new Intent(mCtx, StopDepartures.class);
                                    i.putExtra("stop", resultArray.get(index).getStopId());
                                    i.putExtra("stopName", resultArray.get(index).getStopName());
                                    mCtx.startActivity(i);
                                    return true;
                                }

                                @Override
                                public boolean onItemLongPress(final int index, final OverlayItem item) {
                                    Intent i = new Intent(mCtx, StopDepartures.class);
                                    i.putExtra("stop", resultArray.get(index).getStopId());
                                    i.putExtra("stopName", resultArray.get(index).getStopName());
                                    mCtx.startActivity(i);
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
                }) {
            @Override
            protected java.util.Map<String,String> getParams(){
                java.util.Map<String,String> params = new HashMap<String, String>();
                params.put("database", db);
                params.put("statement", statement);
                return params;
            }
        };
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
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
        if(!locationFound && !showingPath){
            loadResources(URL, "budapest", Statements.getNearbyStops(location.getLatitude(),
                            location.getLongitude(), 1));
            locationFound = true;
        }
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }
}