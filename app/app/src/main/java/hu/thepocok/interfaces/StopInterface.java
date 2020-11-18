package hu.thepocok.interfaces;

import java.util.List;

import hu.farkasch.buspalbackend.objects.BusStop;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface StopInterface {
    @GET("index.php?")
    Call<List<BusStop>> routeList(@Query("username") String username, @Query("password") String password,
                                  @Query("host") String host, @Query("database") String databaseName,
                                  @Query("statement") String statement);
}
