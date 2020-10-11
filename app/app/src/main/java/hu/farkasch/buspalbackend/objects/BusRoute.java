package hu.farkasch.buspalbackend.objects;

import java.io.Serializable;
import java.util.ArrayList;

public class BusRoute implements Serializable {
    private ArrayList<BusTrip> busTripList;
    private int routeId;
    private boolean favourite = false;
    private String name;

    public BusRoute(int routeId, String name) {
        this.routeId = routeId;
        this.name = name;
    }

    public int getRouteId() {
        return routeId;
    }

    public String getName() {
        return name;
    }
}