package hu.kristol.buspal;

import static hu.thepocok.serverlocation.ServerLocation.URL;

import android.app.DatePickerDialog;
import android.content.Intent;
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

import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hu.farkasch.buspalbackend.objects.BusStop;
import hu.farkasch.buspalbackend.objects.Departure;
import hu.thepocok.adapters.TimetableAdapter;
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
    private int selectedStopId;
    private String selectedRoute;
    private int selectedDirection;

    private ArrayList<BusStop> stopsOnwards = new ArrayList<>();
    private ArrayList<BusStop> stopsBackwards = new ArrayList<>();
    private ArrayList<String> onwardStopNames = new ArrayList<>();
    private ArrayList<String> backwardStopNames = new ArrayList<>();

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

        Intent i = this.getIntent();
        stopsList = (ArrayList<BusStop>) i.getSerializableExtra("stopsList");
        selectedRoute = i.getStringExtra("routeName");

        for(BusStop b : stopsList){
            if(b.getDirection() == 0){
                stopsOnwards.add(b);
                onwardStopNames.add(b.getStopName());
            } else{
                stopsBackwards.add(b);
                backwardStopNames.add(b.getStopName());
            }
        }

        date = findViewById(R.id.date);

        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy. MM. dd");
        String currentDateString = currentDate.format(formatter);
        date.setText(currentDateString);

        formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        selectedDate = Integer.parseInt(currentDate.format(formatter));
        selectedDirection = 0;

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
                                loadResources(URL, "budapest", Statements
                                        .getDepartureFromStopByRouteName(selectedStopId, selectedRoute, selectedDate, "budapest"));
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.stop_selection);

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
        });

        //loadResources(URL, "budapest", Statements.getRoutesByStop(stop, "budapest"));

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
                            ArrayList<Departure> resultArray = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                String departureTime = jsonArray.getJSONObject(i).get("departure_time").toString();
                                int tripId = Integer.parseInt(jsonArray.getJSONObject(i).get("trip_id").toString());
                                String name = jsonArray.getJSONObject(i).get("route_short_name").toString();
                                //String destination = jsonArray.getJSONObject(i).get("trip_headsign").toString();

                                Departure d = new Departure(tripId, name, "", departureTime);
                                resultArray.add(d);
                                Log.d("Result", d.toString());
                            }

                            //creating adapter object and setting it to recyclerview
                            TimetableAdapter adapter = new TimetableAdapter(Timetable.this, resultArray, recyclerView);
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