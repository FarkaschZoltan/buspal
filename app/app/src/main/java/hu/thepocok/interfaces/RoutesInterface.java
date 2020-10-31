package hu.thepocok.interfaces;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import hu.farkasch.buspalbackend.objects.BusRoute;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RoutesInterface {
    public static String BASE_URL = "http://80.98.90.176:9876/";

    @GET("index.php?")
    Call<List<BusRoute>> routeList(@Query("username") String username, @Query("password") String password, @Query("host") String host, @Query("database") String db_name, @Query("statement") String statement);
}
