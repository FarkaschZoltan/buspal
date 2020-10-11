package hu.kristol.buspal;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import hu.farkasch.buspalbackend.objects.BusRoute;

public class Routes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extra = getIntent().getBundleExtra("extra");
        ArrayList<BusRoute> routesList = (ArrayList<BusRoute>) extra.getSerializable("routes");

        //ArrayList<BusRoute> routesList = (ArrayList<BusRoute>) getIntent().getSerializableExtra("routes");
        Log.d("Routes", String.valueOf(routesList.size()));
        Log.d("Routes", routesList.toString());

        ScrollView sv = (ScrollView) findViewById(R.id.routeList);

        LayoutInflater inflater =  (LayoutInflater)getSystemService(this.LAYOUT_INFLATER_SERVICE);

        // Create a LinearLayout element
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);

        // Add text
        for(BusRoute routes : routesList){
            LinearLayout innerLayout = new LinearLayout(this);
            innerLayout.setOrientation(LinearLayout.HORIZONTAL);
            innerLayout.setMinimumHeight(100);

            TextView color = new TextView(this);
            switch (routes.getType()){
                case BUS:
                    color.setBackgroundColor(getResources().getColor(R.color.bus));
                    break;
                case TRAM:
                    color.setBackgroundColor(getResources().getColor(R.color.tram));
                    break;
                case TROLLEY:
                    color.setBackgroundColor(getResources().getColor(R.color.trolley));
                    break;
                case METRO:
                    if(routes.getName().equals("M1")){
                        color.setBackgroundColor(getResources().getColor(R.color.metro_1));
                    }else if(routes.getName().equals("M2")){
                        color.setBackgroundColor(getResources().getColor(R.color.metro_2));
                    }else if(routes.getName().equals("M3")){
                        color.setBackgroundColor(getResources().getColor(R.color.metro_3));
                    }else if(routes.getName().equals("M4")){
                        color.setBackgroundColor(getResources().getColor(R.color.metro_4));
                    }
                    break;
                case SUBURBAN_RAILWAY:
                    if(routes.getName().equals("H5")){
                        color.setBackgroundColor(getResources().getColor(R.color.suburban_5));
                    }else if(routes.getName().equals("H6")){
                        color.setBackgroundColor(getResources().getColor(R.color.suburban_6));
                    }else if(routes.getName().equals("H7")){
                        color.setBackgroundColor(getResources().getColor(R.color.suburban_7));
                    }else if(routes.getName().equals("H8") || routes.getName().equals("H9")){
                        color.setBackgroundColor(getResources().getColor(R.color.suburban_8));
                    }
            }
            innerLayout.addView(color, 100, 100);

            TextView routeName = new TextView(this);
            routeName.setText(routes.getName());
            innerLayout.addView(routeName);

            TextView routeDest = new TextView(this);
            routeDest.setText(routes.getDestinations());
            innerLayout.addView(routeDest);

            ll.addView(innerLayout);
        }

        // Add the LinearLayout element to the ScrollView
        sv.addView(ll);

        // Display the view
        //setContentView(v);
    }
}