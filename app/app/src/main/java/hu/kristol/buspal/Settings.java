package hu.kristol.buspal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class Settings extends AppCompatActivity {
    private String selectedCity;
    private float selectedRadius;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        selectedCity = sharedPreferences.getString("city", "budapest");
        selectedRadius = sharedPreferences.getFloat("radius", (float) 1.0);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Settings");

        Spinner spinner = (Spinner) findViewById(R.id.city_selection);

        ArrayList<String> cities = new ArrayList<>();
        cities.add("Budapest");
        cities.add("Miskolc");
        cities.add("Szeged");
        cities.add("Pécs");

        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, cities);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(cityAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCity = cities.get(position).toLowerCase();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        EditText in = findViewById(R.id.radius);
        in.setText(String.valueOf(selectedRadius*1000));

        Button save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float newRadius = 0;
                try{
                    newRadius = Float.parseFloat(in.getText().toString());
                } catch (NumberFormatException e){
                    Toast toast = Toast.makeText(Settings.this, "Given radius is not a number!", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                if(newRadius != selectedRadius){
                    selectedRadius = newRadius;
                }

                if(selectedCity.equals("pécs")){
                    selectedCity = "pecs";
                }

                sharedPreferences.edit().putFloat("radius", selectedRadius / 1000).apply();
                sharedPreferences.edit().putString("city", selectedCity).apply();

                Log.d("SelectedCity", selectedCity);
                Log.d("SelectedCityInSharedPref", sharedPreferences.getString("city", "Nothing is saved"));

                Intent i = new Intent(Settings.this, MainActivity.class);
                Settings.this.startActivity(i);
            }
        });
    }
}