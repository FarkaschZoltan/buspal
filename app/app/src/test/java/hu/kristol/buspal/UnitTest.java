package hu.kristol.buspal;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hu.farkasch.buspalbackend.datastructures.Coordinates;
import hu.farkasch.buspalbackend.objects.BusRoute;
import hu.thepocok.interfaces.RoutesInterface;
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

    @Test
    public void isInsideRadiusTestTrue() {
        assertTrue(Coordinates.isInsideRadius(new Coordinates(47.530207,19.007196), new Coordinates(47.530185, 19.008280), 500));
    }

    @Test
    public void isInsideRadiusTestFalse() {
        assertFalse(Coordinates.isInsideRadius(new Coordinates(47.530207,19.007196), new Coordinates(47.530185, 20.008280), 500));
    }

    @Test
    public void checkDistanceCalculation(){
        Coordinates p1 = new Coordinates(47.611002, 19.103699); //Budapest's northest point
        Coordinates p2 = new Coordinates(47.376773, 19.101796); //Budapest's southest point
        int distance = (int)Math.floor(Coordinates.getDistance(p1, p2) / 1000) * 1000;
        System.out.println(distance);
        assertEquals( 26000, distance);
    }

    @Test
    public void linesStoppingAtBlahaLujzaTer() throws IOException {

        Retrofit retrofit = new Retrofit.Builder().baseUrl(RoutesInterface.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        RoutesInterface api = retrofit.create(RoutesInterface.class);
        Call<List<BusRoute>> task = api.routeList("postgres", "buspal", "localhost", "budapest", Statements.getRoutesByStop("Blaha"));
        List<BusRoute> routeList = task.execute().body();

        ArrayList<String> routes = new ArrayList<>();
        for(BusRoute r : routeList){
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
    public void linesStoppingAtBorarosTer() throws IOException {

        Retrofit retrofit = new Retrofit.Builder().baseUrl(RoutesInterface.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        RoutesInterface api = retrofit.create(RoutesInterface.class);
        Call<List<BusRoute>> task = api.routeList("postgres", "buspal", "localhost", "budapest", Statements.getRoutesByStop("Boráros"));
        List<BusRoute> routeList = task.execute().body();

        ArrayList<String> routes = new ArrayList<>();
        for(BusRoute r : routeList){
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
    public void linesStoppingAtVedgatUtca() throws IOException {

        Retrofit retrofit = new Retrofit.Builder().baseUrl(RoutesInterface.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        RoutesInterface api = retrofit.create(RoutesInterface.class);
        Call<List<BusRoute>> task = api.routeList("postgres", "buspal", "localhost", "budapest", Statements.getRoutesByStop("Védgát"));
        List<BusRoute> routeList = task.execute().body();

        ArrayList<String> routes = new ArrayList<>();
        for(BusRoute r : routeList){
            routes.add(r.getName());
        }
        assertTrue(routes.contains("35"));
        assertTrue(routes.contains("36"));
        assertTrue(routes.contains("148"));
        assertTrue(routes.contains("151"));
        assertTrue(routes.contains("948"));
    }

    @Test
    public void linesStoppingAtNonexistingStop() throws IOException {

        Retrofit retrofit = new Retrofit.Builder().baseUrl(RoutesInterface.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        RoutesInterface api = retrofit.create(RoutesInterface.class);
        Call<List<BusRoute>> task = api.routeList("postgres", "buspal", "localhost", "budapest", Statements.getRoutesByStop("Mordor"));
        List<BusRoute> routeList = task.execute().body();

        assertTrue(routeList.size() == 0);
    }
}