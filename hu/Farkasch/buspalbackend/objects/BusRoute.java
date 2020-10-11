package hu.farkasch.buspalbackend.objects;

import hu.thepocok.statements.Statements;
import java.util.ArrayList;

public class BusRoute {
    private ArrayList<BusTrip> busTripList;
    private int routeId;
    private boolean favourite = false;

    public BusRoute(int routeId){
        generate(routeId);
    }

    public void generate(int routeId){ // to-do: generating the needed BusTrips and BusStops based
        //fuck jdbc
    }
}