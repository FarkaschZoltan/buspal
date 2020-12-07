package hu.farkasch.buspalbackend.objects;

import static hu.thepocok.serverlocation.ServerLocation.URL;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import hu.thepocok.statements.Statements;


public class BusRoute implements Serializable {
    public String getDestinations() {
        return destinations;
    }

    public String getColor() {
        return color;
    }

    @SerializedName("route_id")
    private int routeId;
    private boolean favourite = false;
    @SerializedName("route_short_name")
    private String name;
    @SerializedName("route_type")
    private RouteType type;
    @SerializedName("route_desc")
    private String destinations;
    private String color;
    private ArrayList<BusStop> stops;

    private RequestQueue mRequestQueue;

    public RouteType getType() {
        return type;
    }

    public BusRoute(String routeId, String name, String type) {
        this.routeId = Integer.parseInt(routeId);
        this.name = name;
        int typeOf = Integer.parseInt(type);
        switch (typeOf){
            case 0:
                this.type = RouteType.TRAM;
                break;
            case 1:
                this.type = RouteType.METRO;
                break;
            case 3:
                this.type = RouteType.BUS;
                break;
            case 4:
                this.type = RouteType.FERRY;
                break;
            case 11:
                this.type = RouteType.TROLLEY;
                break;
            case 109:
                this.type = RouteType.SUBURBAN_RAILWAY;
                break;
            case 800:
                this.type = RouteType.TROLLEY;
                break;
            default:
                this.type = null;
                break;
        }
        this.destinations = "";
    }

    public BusRoute(String routeId, String name, String type, String destinations, Context c) {
        this.routeId = Integer.parseInt(routeId);
        this.name = name;
        int typeOf = Integer.parseInt(type);
        switch (typeOf){
            case 0:
                this.type = RouteType.TRAM;
                break;
            case 1:
                this.type = RouteType.METRO;
                break;
            case 3:
                this.type = RouteType.BUS;
                break;
            case 4:
                this.type = RouteType.FERRY;
                break;
            case 11:
                this.type = RouteType.TROLLEY;
                break;
            case 109:
                this.type = RouteType.SUBURBAN_RAILWAY;
                break;
            case 800:
                this.type = RouteType.TROLLEY;
                break;
            default:
                this.type = null;
                break;
        }
        this.destinations = destinations;
        mRequestQueue = Volley.newRequestQueue(c);
        stops = null;
        loadResources(URL, "budapest", Statements.getStopsByRouteShortName(name));
    }

    public BusRoute(String name, String type){
        this.name = name;
        int typeOf = Integer.parseInt(type);
        switch (typeOf){
            case 0:
                this.type = RouteType.TRAM;
                break;
            case 1:
                this.type = RouteType.METRO;
                break;
            case 3:
                this.type = RouteType.BUS;
                break;
            case 4:
                this.type = RouteType.FERRY;
                break;
            case 11:
                this.type = RouteType.TROLLEY;
                break;
            case 109:
                this.type = RouteType.SUBURBAN_RAILWAY;
                break;
            case 800:
                this.type = RouteType.TROLLEY;
                break;
            default:
                this.type = null;
                break;
        }
    }

    public int getRouteId() {
        return routeId;
    }

    public String getName() {
        return name;
    }

    public ArrayList<BusStop> getStops() {
        return stops;
    }

    @Override
    public String toString() {
        return "BusRoute{" +
                "routeId=" + routeId +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", destinations='" + destinations + '\'' +
                '}';
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

                            for (int i = 0; i < jsonArray.length(); i++) {
                                int stopId = Integer.parseInt(jsonArray.getJSONObject(i).get("stop_id").toString());
                                String stopName = jsonArray.getJSONObject(i).get("stop_name").toString();
                                int stopSequence = Integer.parseInt(jsonArray.getJSONObject(i).get("stop_sequence")
                                        .toString());
                                int directionId = Integer.parseInt(jsonArray.getJSONObject(i).get("direction_id")
                                        .toString());

                                BusStop b = new BusStop(stopId, stopName, stopSequence, directionId);
                                resultArray.add(b);
                                Log.d("Result", b.toString());
                            }

                            for(int i = 0; i < resultArray.size()-1; i++){
                                for(int j = i+1; j < resultArray.size(); j++){
                                    BusStop a = resultArray.get(i);
                                    BusStop b = resultArray.get(j);
                                    if(a.getStopId() == b.getStopId() && a.getDirection() == b.getDirection()
                                            && a.getStopSequencePlace() < b.getStopSequencePlace()){
                                        resultArray.remove(i);
                                        i--;
                                        break;
                                    }
                                }
                            }

                            stops = resultArray;

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