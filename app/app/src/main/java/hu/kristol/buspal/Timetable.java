package hu.kristol.buspal;

import static hu.thepocok.serverlocation.ServerLocation.URL;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
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
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hu.farkasch.buspalbackend.datastructures.Time;
import hu.farkasch.buspalbackend.objects.BusRoute;
import hu.farkasch.buspalbackend.objects.BusStop;
import hu.farkasch.buspalbackend.objects.Departure;
import hu.thepocok.adapters.DepartureAdapter;
import hu.thepocok.statements.Statements;

public class Timetable extends AppCompatActivity {
    private RequestQueue mRequestQueue;

    private List<Departure> departureList;

    private DatePickerDialog picker;
    private RecyclerView recyclerView;
    private EditText date;
    private Spinner stopSelection;

    private ArrayList<BusStop> stopsList;
    private int selectedDate;
    private String selectedStop;
    private String selectedRoute;
    private int selectedDirection;
    private BusRoute route;

    private String city;
    private double radius;

    private boolean isSetForFirstTime = false;
    private DepartureAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRequestQueue = Volley.newRequestQueue(this);

        recyclerView = findViewById(R.id.timetable_list_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        city = sharedPreferences.getString("city", "budapest");
        radius = sharedPreferences.getFloat("radius", (float) 1.0);

        Intent i = this.getIntent();
        route = (BusRoute) i.getSerializableExtra("route");
        selectedStop = route.getFirstStop();
        selectedDirection = route.getDirection();
        selectedRoute = route.getName();

        date = findViewById(R.id.date);

        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy. MM. dd");
        String currentDateString = currentDate.format(formatter);
        date.setText(currentDateString);

        formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        selectedDate = Integer.parseInt(currentDate.format(formatter));

        date.setInputType(InputType.TYPE_NULL);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                picker = new DatePickerDialog(Timetable.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String monthWithPlaceholder = (monthOfYear+1 < 10) ? "0" + monthOfYear+1
                                        : String.valueOf(monthOfYear+1);
                                String dayWithPlaceholder = (dayOfMonth < 10) ? "0" + dayOfMonth
                                        : String.valueOf(dayOfMonth);
                                date.setText(year + ". " + monthWithPlaceholder + ". " + dayWithPlaceholder);
                                selectedDate = year*10000 + (monthOfYear+1)*100 + dayOfMonth;
                                Log.d("SelectedDate", String.valueOf(selectedDate));

                                mRequestQueue.cancelAll(new RequestQueue.RequestFilter() {
                                    @Override
                                    public boolean apply(Request<?> request) {
                                        return true;
                                    }
                                });
                                loadResources(URL, city, Statements
                                        .getScheduleByStop(selectedRoute, selectedStop,
                                                selectedDirection, selectedDate));
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        TextView direction = findViewById(R.id.destination_stop);
        direction.setText("In the direction of " + selectedStop);
        direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedDirection == 1){
                    selectedDirection = 0;
                } else{
                    selectedDirection = 1;
                }

                if(selectedStop.equals(route.getDestinations())){
                    selectedStop = route.getFirstStop();
                } else{
                    selectedStop = route.getDestinations();
                }

                direction.setText("In the direction of " + selectedStop);

                recyclerView = new RecyclerView(Timetable.this);
                Log.d("NewSelectedData", selectedRoute + " " + selectedStop + " " + String.valueOf(selectedDirection));

                loadResources(URL, city, Statements
                        .getScheduleByStop(selectedRoute, selectedStop, selectedDirection, selectedDate));
            }
        });

        /*Spinner spinner = (Spinner) findViewById(R.id.stop_selection);

        ArrayAdapter<String> stopNameAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, onwardStopNames);
        stopNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(stopNameAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(selectedDirection == 0){
                    selectedStopId = stopsOnwards.get(position).getStopId();
                } else{
                    selectedStopId = stopsOnwards.get(position).getStopId();
                }
                Log.d("SelectedStop", stopsOnwards.get(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        loadResources(URL, city, Statements
                .getScheduleByStop(selectedRoute, selectedStop, selectedDirection, selectedDate));

    }

    private void loadResources(String url, String db, String statement){
        StringRequest sr = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Query", statement);
                        Log.d("Timetable", response);
                        try {
                            //converting the string to json array object
                            JSONArray jsonArray = new JSONArray(response);
                            ArrayList<Departure> resultArray = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                String departureTime = jsonArray.getJSONObject(i).get("departure_time").toString();
                                int tripId = Integer.parseInt(jsonArray.getJSONObject(i).get("trip_id").toString());
                                String name = route.getName();
                                String destination = route.getDestinations();
                                int routeType = Integer.parseInt(jsonArray.getJSONObject(i)
                                        .get("route_type").toString());

                                Departure d = new Departure(tripId, name, destination,
                                        new Time(departureTime), routeType);
                                resultArray.add(d);
                                Log.d("Result", d.toString());
                            }

                            //creating adapter object and setting it to recyclerview
                            if(!isSetForFirstTime){
                                adapter = new DepartureAdapter(Timetable.this, resultArray, recyclerView);
                                recyclerView.setAdapter(adapter);
                            } else{
                                adapter.updateData(resultArray);
                            }

                            isSetForFirstTime = true;
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