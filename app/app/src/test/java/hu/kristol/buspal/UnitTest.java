package hu.kristol.buspal;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hu.farkasch.buspalbackend.datastructures.Coordinates;
import hu.farkasch.buspalbackend.objects.BusRoute;
import hu.farkasch.buspalbackend.objects.BusStop;
import hu.thepocok.interfaces.RouteInterface;
import hu.thepocok.interfaces.StopInterface;
import hu.thepocok.statements.Statements;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTest {
    private String url = "http://80.98.90.176:9876/";

    @Test
    public void isInsideRadiusTestTrue() {
        assertTrue(Coordinates.isInsideRadius(new Coordinates(47.530207, 19.007196), new Coordinates(47.530185, 19.008280), 500));
    }

    @Test
    public void isInsideRadiusTestFalse() {
        assertFalse(Coordinates.isInsideRadius(new Coordinates(47.530207, 19.007196), new Coordinates(47.530185, 20.008280), 500));
    }

    @Test
    public void checkDistanceCalculation() {
        Coordinates p1 = new Coordinates(47.611002, 19.103699); //Budapest's northest point
        Coordinates p2 = new Coordinates(47.376773, 19.101796); //Budapest's southest point
        int distance = (int) Math.floor(Coordinates.getDistance(p1, p2) / 1000) * 1000;
        assertEquals(26000, distance);
    }

    @Test
    public void linesStoppingAtBudapestBlahaLujzaTer() throws IOException {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(RouteInterface.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        RouteInterface api = retrofit.create(RouteInterface.class);
        Call<List<BusRoute>> task = api.routeList("postgres", "buspal", "localhost", "budapest", Statements.getRoutesByStop("Blaha", "budapest"));
        List<BusRoute> routeList = task.execute().body();

        ArrayList<String> routes = new ArrayList<>();
        for (BusRoute r : routeList) {
            routes.add(r.getName());
        }
        assertTrue(routes.contains("M2"));
        assertTrue(routes.contains("5"));
        assertTrue(routes.contains("7"));
        assertTrue(routes.contains("7E"));
        assertTrue(routes.contains("110"));
        assertTrue(routes.contains("112"));
        assertTrue(routes.contains("178"));
        assertTrue(routes.contains("4"));
        assertTrue(routes.contains("6"));
        assertTrue(routes.contains("907"));
        assertTrue(routes.contains("908"));
        assertTrue(routes.contains("931"));
        assertTrue(routes.contains("956"));
        assertTrue(routes.contains("973"));
        assertTrue(routes.contains("990"));
        assertTrue(routes.contains("923"));
        assertTrue(routes.contains("99"));
        assertTrue(routes.contains("217E"));
        assertTrue(routes.contains("28"));
        assertTrue(routes.contains("28A"));
        assertTrue(routes.contains("37"));
        assertTrue(routes.contains("37A"));
        assertTrue(routes.contains("62"));
        assertTrue(routes.contains("74"));
        assertTrue(routes.contains("8E"));
        assertTrue(routes.contains("108E"));
        assertTrue(routes.contains("133E"));
    }

    @Test
    public void linesStoppingAtBudapestBorarosTer() throws IOException {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(RouteInterface.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        RouteInterface api = retrofit.create(RouteInterface.class);
        Call<List<BusRoute>> task = api.routeList("postgres", "buspal", "localhost", "budapest", Statements.getRoutesByStop("Boráros", "budapest"));
        List<BusRoute> routeList = task.execute().body();

        ArrayList<String> routes = new ArrayList<>();
        for (BusRoute r : routeList) {
            routes.add(r.getName());
        }
        assertTrue(routes.contains("H7"));
        assertTrue(routes.contains("2"));
        assertTrue(routes.contains("15"));
        assertTrue(routes.contains("115"));
        assertTrue(routes.contains("23"));
        assertTrue(routes.contains("23E"));
        assertTrue(routes.contains("223M"));
        assertTrue(routes.contains("54"));
        assertTrue(routes.contains("55"));
        assertTrue(routes.contains("212"));
        assertTrue(routes.contains("918"));
        assertTrue(routes.contains("923"));
        assertTrue(routes.contains("979"));
        assertTrue(routes.contains("979A"));
    }

    @Test
    public void linesStoppingAtBudapestVedgatUtca() throws IOException {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(RouteInterface.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        RouteInterface api = retrofit.create(RouteInterface.class);
        Call<List<BusRoute>> task = api.routeList("postgres", "buspal", "localhost", "budapest", Statements.getRoutesByStop("Védgát", "budapest"));
        List<BusRoute> routeList = task.execute().body();

        ArrayList<String> routes = new ArrayList<>();
        for (BusRoute r : routeList) {
            routes.add(r.getName());
        }
        assertTrue(routes.contains("35"));
        assertTrue(routes.contains("36"));
        assertTrue(routes.contains("148"));
        assertTrue(routes.contains("151"));
        assertTrue(routes.contains("948"));
    }

    @Test
    public void linesStoppingAtNonexistingStopInBudapest() throws IOException {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(RouteInterface.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        RouteInterface api = retrofit.create(RouteInterface.class);
        Call<List<BusRoute>> task = api.routeList("postgres", "buspal", "localhost", "budapest", Statements.getRoutesByStop("Mordor", "budapest"));
        List<BusRoute> routeList = task.execute().body();

        assertTrue(routeList.size() == 0);
    }

    @Test
    public void linesStoppingAtMiskolcZentaUtca() throws IOException {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(RouteInterface.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        RouteInterface api = retrofit.create(RouteInterface.class);
        Call<List<BusRoute>> task = api.routeList("postgres", "buspal", "localhost", "miskolc", Statements.getRoutesByStop("Zenta", "miskolc"));
        List<BusRoute> routeList = task.execute().body();

        ArrayList<String> routes = new ArrayList<>();
        for (BusRoute r : routeList) {
            routes.add(r.getName());
        }
        assertTrue(routes.contains("1V"));
    }

    @Test
    public void linesStoppingAtMiskolcBuzaTer() throws IOException {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(RouteInterface.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        RouteInterface api = retrofit.create(RouteInterface.class);
        Call<List<BusRoute>> task = api.routeList("postgres", "buspal", "localhost", "miskolc", Statements.getRoutesByStop("Búza tér", "miskolc"));
        List<BusRoute> routeList = task.execute().body();

        ArrayList<String> routes = new ArrayList<>();
        for (BusRoute r : routeList) {
            routes.add(r.getName());
        }
        assertTrue(routes.contains("1"));
        assertTrue(routes.contains("1B"));
        assertTrue(routes.contains("3"));
        assertTrue(routes.contains("3A"));
        assertTrue(routes.contains("4"));
        assertTrue(routes.contains("7"));
        assertTrue(routes.contains("11"));
        assertTrue(routes.contains("20"));
        assertTrue(routes.contains("24"));
        assertTrue(routes.contains("28"));
        assertTrue(routes.contains("280"));
        assertTrue(routes.contains("43"));
        assertTrue(routes.contains("20G"));
    }

    @Test
    public void linesStoppingAtNonexistingStopInMiskolc() throws IOException {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(RouteInterface.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        RouteInterface api = retrofit.create(RouteInterface.class);
        Call<List<BusRoute>> task = api.routeList("postgres", "buspal", "localhost", "budapest", Statements.getRoutesByStop("Mordor", "miskolc"));
        List<BusRoute> routeList = task.execute().body();

        assertTrue(routeList.size() == 0);
    }

    @Test
    public void stopsNearBudapestSzepvolgyiDulo() throws IOException{
        Retrofit retrofit = new Retrofit.Builder().baseUrl(RouteInterface.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        StopInterface api = retrofit.create(StopInterface.class);

        Call<List<BusStop>> task = api.routeList("postgres", "buspal", "localhost", "budapest", Statements.getNearbyStops(47.546265, 18.992273, 0.15));
        List<BusStop> stopList = task.execute().body();

        ArrayList<String> stopNames = new ArrayList<>();
        for (BusStop s : stopList) {
            stopNames.add(s.getStopName());
        }

        assertTrue(stopNames.contains("Szépvölgyi dűlő"));
    }

    @Test
    public void stopsNearBudapestUjlakiHegy() throws IOException{
        Retrofit retrofit = new Retrofit.Builder().baseUrl(RouteInterface.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        StopInterface api = retrofit.create(StopInterface.class);

        Call<List<BusStop>> task = api.routeList("postgres", "buspal", "localhost", "budapest", Statements.getNearbyStops(47.552445, 18.988842, 0.15));
        List<BusStop> stopList = task.execute().body();

        ArrayList<String> stopNames = new ArrayList<>();
        for (BusStop s : stopList) {
            stopNames.add(s.getStopName());
        }

        assertTrue(stopNames.size() == 0);
    }

    @Test
    public void stopsNearBudapestKosKarolyTer() throws IOException{
        Retrofit retrofit = new Retrofit.Builder().baseUrl(RouteInterface.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        StopInterface api = retrofit.create(StopInterface.class);

        Call<List<BusStop>> task = api.routeList("postgres", "buspal", "localhost", "budapest", Statements.getNearbyStops(47.455681, 19.126076, 0.15));
        List<BusStop> stopList = task.execute().body();

        ArrayList<String> stopNames = new ArrayList<>();
        for (BusStop s : stopList) {
            stopNames.add(s.getStopName());
        }

        assertTrue(stopNames.contains("Pannónia út"));
        assertTrue(stopNames.contains("Kós Károly tér"));
    }

    @Test
    public void stopsNearBudapestELTEIK() throws IOException{
        Retrofit retrofit = new Retrofit.Builder().baseUrl(RouteInterface.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        StopInterface api = retrofit.create(StopInterface.class);

        Call<List<BusStop>> task = api.routeList("postgres", "buspal", "localhost", "budapest", Statements.getNearbyStops(47.472207, 19.061671, 0.6));
        List<BusStop> stopList = task.execute().body();

        ArrayList<String> stopNames = new ArrayList<>();
        for (BusStop s : stopList) {
            stopNames.add(s.getStopName());
        }

        assertTrue(stopNames.contains("Petőfi híd, budai hídfő"));
        assertTrue(stopNames.contains("Infopark"));
        assertTrue(stopNames.contains("BudaPart"));
        assertTrue(stopNames.contains("Infopark (Pázmány Péter sétány)"));
        assertTrue(stopNames.contains("Magyar tudósok körútja"));
        assertTrue(stopNames.contains("Egyetemváros - A38 hajóállomás"));
    }

    @Test
    public void stopsNearMiskolcDVTKStadium() throws IOException{
        Retrofit retrofit = new Retrofit.Builder().baseUrl(RouteInterface.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        StopInterface api = retrofit.create(StopInterface.class);

        Call<List<BusStop>> task = api.routeList("postgres", "buspal", "localhost", "miskolc", Statements.getNearbyStops(48.100929, 20.717504, 0.3));
        List<BusStop> stopList = task.execute().body();

        ArrayList<String> stopNames = new ArrayList<>();
        for (BusStop s : stopList) {
            stopNames.add(s.getStopName());
        }

        assertTrue(stopNames.contains("DVTK Stadion"));
        assertTrue(stopNames.contains("Bulgárföld városrész"));
    }

    @Test
    public void stopsNearMiskolcBarlangfurdo() throws IOException{
        Retrofit retrofit = new Retrofit.Builder().baseUrl(RouteInterface.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        StopInterface api = retrofit.create(StopInterface.class);

        Call<List<BusStop>> task = api.routeList("postgres", "buspal", "localhost", "miskolc", Statements.getNearbyStops(48.060572, 20.745052, 0.5));
        List<BusStop> stopList = task.execute().body();

        ArrayList<String> stopNames = new ArrayList<>();
        for (BusStop s : stopList) {
            System.out.println(s.getStopName());
            stopNames.add(s.getStopName());
        }

        assertTrue(stopNames.contains("Miskolctapolca Barlangfürdő"));
    }
}