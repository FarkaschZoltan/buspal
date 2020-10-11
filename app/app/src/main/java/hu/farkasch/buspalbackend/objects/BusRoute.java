package hu.farkasch.buspalbackend.objects;

import hu.thepocok.statements.Statements;
import java.util.ArrayList;

public class BusRoute {
    private ArrayList<BusTrip> busTripList;
    private int routeId;
    private boolean favourite = false;
    private String name;

    public BusRoute(int routeId){
        generate(routeId);
    }

    public BusRoute(int routeId, String name) {
        this.routeId = routeId;
        this.name = name;
    }

    public void generate(int routeId){ // to-do: generating the needed BusTrips and BusStops based
        //fuck jdbc
    }
}