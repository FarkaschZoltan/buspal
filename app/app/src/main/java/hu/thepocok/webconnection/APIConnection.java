package hu.thepocok.webconnection;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import hu.farkasch.buspalbackend.objects.BusRoute;
import hu.kristol.buspal.Routes;

public class APIConnection extends AsyncTask<String, Void, String> {
    private Context context;
    private final String url;
    private final String host;
    private final String username;
    private final String password;
    private final String database;

    public APIConnection(Context c, String url, String host, String username, String password, String database) {
        this.context = c;
        this.url = url;
        this.host = host;
        this.username = username;
        this.password = password;
        this.database = database;
    }

    //TODO timeout

    @Override
    protected String doInBackground(String... strings) {
        OutputStream out = null;

        try {
            URL url = new URL(this.url);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");

            out = new BufferedOutputStream(urlConnection.getOutputStream());

            StringBuilder sb = new StringBuilder();
            sb.append("host" + "=" + host + "&");
            sb.append("username" + "=" + username + "&");
            sb.append("password" + "=" + password + "&");
            sb.append("database" + "=" + database + "&");
            sb.append("statement" + "=" + strings[0]);

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(sb.toString());
            writer.flush();
            writer.close();
            out.close();

            urlConnection.connect();

            switch (strings[1]){
                case "route_activity":
                    InputStream inputStream = urlConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));

                    String line = "";
                    StringBuilder result = new StringBuilder();

                    while ((line = bufferedReader.readLine()) != null) {
                        result.append(line);
                    }

                    JSONArray jsonArray = new JSONArray(result.toString());
                    ArrayList<BusRoute> resultArray = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        int routeId = Integer.parseInt(jsonArray.getJSONObject(i).get("route_id").toString());
                        String name = jsonArray.getJSONObject(i).get("route_short_name").toString();
                        int type = Integer.parseInt(jsonArray.getJSONObject(i).get("route_type").toString());
                        String destination = jsonArray.getJSONObject(i).get("route_desc").toString();

                        BusRoute b = new BusRoute(routeId, name, type, destination);
                        resultArray.add(b);
                        Log.d("Result", b.toString());
                    }

                    Log.d("Result", String.valueOf(resultArray.size()));
                    bufferedReader.close();
                    inputStream.close();
                    urlConnection.disconnect();

                    Bundle extra = new Bundle();
                    extra.putSerializable("routes", resultArray);

                    Intent i = new Intent(context, Routes.class);
                    i.putExtra("extra", extra);
                    context.startActivity(i);
                    break;
                case "line_activity":
                    //TODO
                    break;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
