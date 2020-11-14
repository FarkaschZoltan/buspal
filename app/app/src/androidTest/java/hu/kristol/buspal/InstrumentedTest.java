package hu.kristol.buspal;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import hu.farkasch.buspalbackend.datastructures.Coordinates;
import hu.farkasch.buspalbackend.datastructures.Time;
import hu.farkasch.buspalbackend.objects.BusRoute;
import hu.farkasch.buspalbackend.objects.BusStop;
import hu.farkasch.buspalbackend.objects.Departure;
import hu.thepocok.interfaces.RouteInterface;
import hu.thepocok.interfaces.RouteStopsInterface;
import hu.thepocok.interfaces.StopDepartureInterface;
import hu.thepocok.interfaces.StopInterface;
import hu.thepocok.serverlocation.ServerLocation;
import hu.thepocok.statements.Statements;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class InstrumentedTest {
    @Test
    public void linesStoppingAtBudapestBlahaLujzaTer() throws IOException {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerLocation.URL).addConverterFactory(GsonConverterFactory.create()).build();
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
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerLocation.URL).addConverterFactory(GsonConverterFactory.create()).build();
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
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerLocation.URL).addConverterFactory(GsonConverterFactory.create()).build();
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
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerLocation.URL).addConverterFactory(GsonConverterFactory.create()).build();
        RouteInterface api = retrofit.create(RouteInterface.class);
        Call<List<BusRoute>> task = api.routeList("postgres", "buspal", "localhost", "budapest", Statements.getRoutesByStop("Mordor", "budapest"));
        List<BusRoute> routeList = task.execute().body();

        assertTrue(routeList.size() == 0);
    }

    @Test
    public void linesStoppingAtMiskolcZentaUtca() throws IOException {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerLocation.URL).addConverterFactory(GsonConverterFactory.create()).build();
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
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerLocation.URL).addConverterFactory(GsonConverterFactory.create()).build();
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
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerLocation.URL).addConverterFactory(GsonConverterFactory.create()).build();
        RouteInterface api = retrofit.create(RouteInterface.class);
        Call<List<BusRoute>> task = api.routeList("postgres", "buspal", "localhost", "budapest", Statements.getRoutesByStop("Mordor", "miskolc"));
        List<BusRoute> routeList = task.execute().body();

        assertTrue(routeList.size() == 0);
    }

    @Test
    public void stopsNearBudapestSzepvolgyiDulo() throws IOException{
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerLocation.URL).addConverterFactory(GsonConverterFactory.create()).build();
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
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerLocation.URL).addConverterFactory(GsonConverterFactory.create()).build();
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
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerLocation.URL).addConverterFactory(GsonConverterFactory.create()).build();
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
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerLocation.URL).addConverterFactory(GsonConverterFactory.create()).build();
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
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerLocation.URL).addConverterFactory(GsonConverterFactory.create()).build();
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
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerLocation.URL).addConverterFactory(GsonConverterFactory.create()).build();
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

    @Test
    public void stopWithOnlyOneBusInBudapest() throws IOException{
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerLocation.URL).addConverterFactory(GsonConverterFactory.create()).build();
        StopDepartureInterface api = retrofit.create(StopDepartureInterface.class);

        Call<List<Departure>> task = api.departureList("postgres", "buspal", "localhost", "budapest", Statements.getDepartureFromStop(4246, 20201030));
        List<Departure> departuresList = task.execute().body();
        System.out.println(departuresList.get(0));

        assertTrue(departuresList.contains(new Departure(0, "65", "Kolosy tér", new Time(6, 3, 0))));
        assertTrue(departuresList.contains(new Departure(0, "65", "Kolosy tér", new Time(6, 32, 0))));
        assertTrue(departuresList.contains(new Departure(0, "65", "Kolosy tér", new Time(7, 2, 0))));
        assertTrue(departuresList.contains(new Departure(0, "65", "Kolosy tér", new Time(7, 31, 0))));
        assertTrue(departuresList.contains(new Departure(0, "65", "Kolosy tér", new Time(8, 2, 0))));
        assertTrue(departuresList.contains(new Departure(0, "65", "Kolosy tér", new Time(8, 32, 0))));
        assertTrue(departuresList.contains(new Departure(0, "65", "Kolosy tér", new Time(9, 22, 0))));
        assertTrue(departuresList.contains(new Departure(0, "65", "Kolosy tér", new Time(10, 23, 0))));
        assertTrue(departuresList.contains(new Departure(0, "65", "Kolosy tér", new Time(11, 23, 0))));
        assertTrue(departuresList.contains(new Departure(0, "65", "Kolosy tér", new Time(12, 23, 0))));
        assertTrue(departuresList.contains(new Departure(0, "65", "Kolosy tér", new Time(13, 23, 0))));
        assertTrue(departuresList.contains(new Departure(0, "65", "Kolosy tér", new Time(14, 23, 0))));
        assertTrue(departuresList.contains(new Departure(0, "65", "Kolosy tér", new Time(15, 3, 0))));
        assertTrue(departuresList.contains(new Departure(0, "65", "Kolosy tér", new Time(15, 33, 0))));
        assertTrue(departuresList.contains(new Departure(0, "65", "Kolosy tér", new Time(16, 2, 0))));
        assertTrue(departuresList.contains(new Departure(0, "65", "Kolosy tér", new Time(16, 32, 0))));
        assertTrue(departuresList.contains(new Departure(0, "65", "Kolosy tér", new Time(17, 42, 0))));
        assertTrue(departuresList.contains(new Departure(0, "65", "Kolosy tér", new Time(18, 23, 0))));
        assertTrue(departuresList.contains(new Departure(0, "65", "Kolosy tér", new Time(19, 23, 0))));
        assertTrue(departuresList.contains(new Departure(0, "65", "Kolosy tér", new Time(20, 23, 0))));
    }

    @Test
    public void stopWithManyBusesInBudapest() throws IOException {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerLocation.URL).addConverterFactory(GsonConverterFactory.create()).build();
        StopDepartureInterface api = retrofit.create(StopDepartureInterface.class);

        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String currentDate = date.format(formatter);

        Call<List<Departure>> task = api.departureList("postgres", "buspal", "localhost", "budapest", Statements.getDepartureFromStop(832, 20201106));
        List<Departure> departuresList = task.execute().body();

        assertTrue(departuresList.contains(new Departure(0, "19", "Kelenföld vasútállomás M", new Time(4, 8, 0))));
        assertTrue(departuresList.contains(new Departure(0, "19", "Kelenföld vasútállomás M", new Time(23, 47, 0))));
        assertTrue(departuresList.contains(new Departure(0, "19", "Kelenföld vasútállomás M", new Time(24, 7, 0))));
        assertTrue(departuresList.contains(new Departure(0, "49", "Kelenföld vasútállomás M", new Time(4, 54, 0))));
        assertTrue(departuresList.contains(new Departure(0, "49", "Kelenföld vasútállomás M", new Time(13, 34, 0))));
        assertTrue(departuresList.contains(new Departure(0, "49", "Kelenföld vasútállomás M", new Time(23, 54, 0))));
        assertTrue(departuresList.contains(new Departure(0, "7", "Albertfalva vasútállomás", new Time(4, 49, 0))));
        assertTrue(departuresList.contains(new Departure(0, "7", "Albertfalva vasútállomás", new Time(23, 49, 0))));
        assertTrue(departuresList.contains(new Departure(0, "7", "Albertfalva vasútállomás", new Time(11, 49, 0))));
        assertTrue(departuresList.contains(new Departure(0, "114", "Baross Gábor-telep, Ispiláng utca", new Time(4, 50, 0))));
        assertTrue(departuresList.contains(new Departure(0, "114", "Baross Gábor-telep, Ispiláng utca", new Time(18, 32, 0))));
        assertTrue(departuresList.contains(new Departure(0, "114", "Baross Gábor-telep, Ispiláng utca", new Time(23, 50, 0))));
        assertTrue(departuresList.contains(new Departure(0, "213", "Donát-hegy", new Time(5, 10, 0))));
        assertTrue(departuresList.contains(new Departure(0, "213", "Donát-hegy", new Time(12, 18, 0))));
        assertTrue(departuresList.contains(new Departure(0, "213", "Donát-hegy", new Time(23, 40, 0))));
        assertTrue(departuresList.contains(new Departure(0, "214", "Baross Gábor-telep, Ispiláng utca", new Time(5, 30, 0))));
        assertTrue(departuresList.contains(new Departure(0, "214", "Baross Gábor-telep, Ispiláng utca", new Time(14, 8, 0))));
        assertTrue(departuresList.contains(new Departure(0, "214", "Baross Gábor-telep, Ispiláng utca", new Time(17, 8, 0))));
        assertTrue(departuresList.contains(new Departure(0, "907", "Kelenföld vasútállomás M", new Time(24, 0, 0))));
        assertTrue(departuresList.contains(new Departure(0, "907", "Kelenföld vasútállomás M", new Time(26, 23, 0))));
        assertTrue(departuresList.contains(new Departure(0, "907", "Kelenföld vasútállomás M", new Time(28, 23, 0))));
        assertTrue(departuresList.contains(new Departure(0, "918", "Kelenföld vasútállomás M", new Time(24, 31, 0))));
        assertTrue(departuresList.contains(new Departure(0, "918", "Kelenföld vasútállomás M", new Time(26, 31, 0))));
        assertTrue(departuresList.contains(new Departure(0, "918", "Kelenföld vasútállomás M", new Time(27, 1, 0))));
    }

    @Test
    public void nonExistingStopInBudapest() throws IOException {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerLocation.URL).addConverterFactory(GsonConverterFactory.create()).build();
        StopDepartureInterface api = retrofit.create(StopDepartureInterface.class);

        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String currentDate = date.format(formatter);

        Call<List<Departure>> task = api.departureList("postgres", "buspal", "localhost", "budapest", Statements.getDepartureFromStop(999999999, Integer.parseInt(currentDate)));
        List<Departure> departuresList = task.execute().body();

        assertTrue(departuresList.size() == 0);
    }

    @Test
    public void multipleKindOfVehiclesInBudapest() throws IOException {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerLocation.URL).addConverterFactory(GsonConverterFactory.create()).build();
        StopDepartureInterface api = retrofit.create(StopDepartureInterface.class);

        Call<List<Departure>> task = api.departureList("postgres", "buspal", "localhost", "budapest", Statements.getDepartureFromStop(4309, 20201106));
        List<Departure> departuresList = task.execute().body();

        assertTrue(departuresList.contains(new Departure(0, "75", "Puskás Ferenc Stadion M", new Time(4, 59, 0))));
        assertTrue(departuresList.contains(new Departure(0, "75", "Puskás Ferenc Stadion M", new Time(11, 30, 0))));
        assertTrue(departuresList.contains(new Departure(0, "75", "Puskás Ferenc Stadion M", new Time(24, 00, 0))));

        assertTrue(departuresList.contains(new Departure(0, "79M", "Keleti pályaudvar M", new Time(6, 14, 0))));
        assertTrue(departuresList.contains(new Departure(0, "79M", "Keleti pályaudvar M", new Time(13, 31, 0))));
        assertTrue(departuresList.contains(new Departure(0, "79M", "Keleti pályaudvar M", new Time(20, 15, 0))));

        assertTrue(departuresList.contains(new Departure(0, "30", "Keleti pályaudvar M", new Time(4, 48, 0))));
        assertTrue(departuresList.contains(new Departure(0, "30", "Keleti pályaudvar M", new Time(9, 53, 0))));
        assertTrue(departuresList.contains(new Departure(0, "30", "Keleti pályaudvar M", new Time(23, 45, 0))));

        assertTrue(departuresList.contains(new Departure(0, "30A", "Keleti pályaudvar M", new Time(6, 28, 0))));
        assertTrue(departuresList.contains(new Departure(0, "30A", "Keleti pályaudvar M", new Time(10, 24, 0))));
        assertTrue(departuresList.contains(new Departure(0, "30A", "Keleti pályaudvar M", new Time(22, 16, 0))));

        assertTrue(departuresList.contains(new Departure(0, "230", "Keleti pályaudvar M", new Time(10, 44, 0))));
        assertTrue(departuresList.contains(new Departure(0, "230", "Keleti pályaudvar M", new Time(11, 43, 0))));
        assertTrue(departuresList.contains(new Departure(0, "230", "Keleti pályaudvar M", new Time(16, 46, 0))));
        assertTrue(departuresList.contains(new Departure(0, "230", "Keleti pályaudvar M", new Time(22, 36, 0))));

        assertTrue(departuresList.contains(new Departure(0, "979", "Újpalota, Nyírpalota út", new Time(24, 21, 0))));
        assertTrue(departuresList.contains(new Departure(0, "979", "Újpalota, Nyírpalota út", new Time(26, 21, 0))));
        assertTrue(departuresList.contains(new Departure(0, "979", "Újpalota, Nyírpalota út", new Time(28, 21, 0))));

    }

    @Test
    public void tram1DepartureAt19_35() throws IOException {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerLocation.URL).addConverterFactory(GsonConverterFactory.create()).build();
        RouteStopsInterface api = retrofit.create(RouteStopsInterface.class);

        Call<List<BusStop>> task = api.routeStopsList("postgres", "buspal", "localhost", "budapest", Statements.getScheduleByTripId(39404));
        List<BusStop> routeStopsList = task.execute().body();

        System.out.println(routeStopsList.toString());
        System.out.println(new BusStop("Bécsi út / Vörösvári út", new Time(19, 35, 0)).toString());
        assertTrue(routeStopsList.contains(new BusStop("Bécsi út / Vörösvári út", new Time(19, 35, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Óbudai rendelőintézet", new Time(19, 37, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Flórián tér", new Time(19, 38, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Szentlélek tér H", new Time(19, 39, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Népfürdő utca / Árpád híd", new Time(19, 41, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Göncz Árpád városközpont M", new Time(19, 42, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Honvédkórház", new Time(19, 43, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Lehel utca / Róbert Károly körút", new Time(19, 45, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Vágány utca / Róbert Károly körút", new Time(19, 46, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Kacsóh Pongrác út", new Time(19, 48, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Erzsébet királyné útja, aluljáró", new Time(19, 49, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Ajtósi Dürer sor", new Time(19, 51, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Zugló vasútállomás", new Time(19, 52, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Egressy út / Hungária körút", new Time(19, 54, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Puskás Ferenc Stadion M", new Time(19, 57, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Hős utca", new Time(19, 58, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Hidegkuti Nándor Stadion", new Time(20, 00, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Kőbányai út / Könyves Kálmán körút", new Time(20, 02, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Vajda Péter utca", new Time(20, 3, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Népliget M", new Time(20, 5, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Albert Flórián út", new Time(20, 6, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Ferencváros vá. - Málenkij Robot Eh.", new Time(20, 8, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Mester utca / Könyves Kálmán körút", new Time(20, 10, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Közvágóhíd H", new Time(20, 12, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Infopark", new Time(20, 14, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Budafoki út / Dombóvári út", new Time(20, 15, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Hauszmann Alajos utca / Szerémi út", new Time(20, 17, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Hengermalom út / Szerémi út", new Time(20, 19, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Etele út / Fehérvári út", new Time(20, 21, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Bikás park M", new Time(20, 23, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Bártfai utca", new Time(20, 24, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Kelenföld vasútállomás M", new Time(20, 26, 0))));
    }

    @Test
    public void bus5DepartureAt20_04() throws IOException {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerLocation.URL).addConverterFactory(GsonConverterFactory.create()).build();
        RouteStopsInterface api = retrofit.create(RouteStopsInterface.class);

        Call<List<BusStop>> task = api.routeStopsList("postgres", "buspal", "localhost", "budapest", Statements.getScheduleByTripId(252150));
        List<BusStop> routeStopsList = task.execute().body();

        assertTrue(routeStopsList.contains(new BusStop("Pasaréti tér", new Time(20, 4, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Harangvirág utca", new Time(20, 4, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Virág árok", new Time(20, 5, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Gábor Áron utca / Pasaréti út", new Time(20, 6, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Júlia utca", new Time(20, 7, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Vasas sportpálya", new Time(20, 8, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Városmajor", new Time(20, 9, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Nyúl utca", new Time(20, 10, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Széll Kálmán tér M (Csaba utca)", new Time(20, 11, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Körmöci utca", new Time(20, 13, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Korlát utca", new Time(20, 14, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Mikó utca", new Time(20, 15, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Alagút utca", new Time(20, 16, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Dózsa György tér", new Time(20, 18, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Döbrentei tér", new Time(20, 19, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Március 15. tér", new Time(20, 20, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Ferenciek tere M", new Time(20, 23, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Astoria M", new Time(20, 25, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Uránia", new Time(20, 26, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Blaha Lujza tér M", new Time(20, 28, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Huszár utca", new Time(20, 29, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Keleti pályaudvar M", new Time(20, 31, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Reiner Frigyes park", new Time(20, 33, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Cházár András utca", new Time(20, 34, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Stefánia út / Thököly út", new Time(20, 35, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Zugló vasútállomás", new Time(20, 37, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Korong utca", new Time(20, 39, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Erzsébet királyné útja, aluljáró", new Time(20, 40, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Laky Adolf utca", new Time(20, 41, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Nagy Lajos király útja / Czobor utca", new Time(20, 42, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Fűrész utca", new Time(20, 44, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Rákospatak utca", new Time(20, 45, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Miskolci utca", new Time(20, 46, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Öv utca", new Time(20, 47, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Tóth István utca", new Time(20, 48, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Vág utca", new Time(20, 49, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Opál utca", new Time(20, 50, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Széchenyi út", new Time(20, 51, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Szent Korona útja", new Time(20, 52, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Wesselényi utca", new Time(20, 53, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Szerencs utca", new Time(20, 54, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Rákos úti szakrendelő", new Time(20, 55, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Illyés Gyula utca", new Time(20, 56, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Epres sor", new Time(20, 58, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Juhos utca", new Time(20, 59, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Kossuth utca, lakótelep", new Time(21, 0, 0))));
        assertTrue(routeStopsList.contains(new BusStop("Rákospalota, Kossuth utca", new Time(21, 1, 0))));

    }
}