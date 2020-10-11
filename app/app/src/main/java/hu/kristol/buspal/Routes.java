package hu.kristol.buspal;

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
            TextView tv = new TextView(this);
            tv.setText(routes.getName());
            ll.addView(tv);
        }

        // Add the LinearLayout element to the ScrollView
        sv.addView(ll);

        // Display the view
        //setContentView(v);
    }
}