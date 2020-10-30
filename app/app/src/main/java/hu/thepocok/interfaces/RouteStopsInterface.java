package hu.thepocok.interfaces;

import java.util.List;

import hu.farkasch.buspalbackend.objects.BusStop;
import hu.farkasch.buspalbackend.objects.Departure;

import retrofit2.Call;

import retrofit2.http.GET;

import retrofit2.http.Query;

public interface RouteStopsInterface {

    public static String BASE_URL = "http://80.98.90.176:9876/";

    @GET("index.php?")

    Call<List<BusStop>> routeStopsList(@Query("username") String username, @Query("password") String password, @Query("host") String host, @Query("database") String db_name, @Query("statement") String statement);

}