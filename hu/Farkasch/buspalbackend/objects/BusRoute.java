package hu.farkasch.buspalbackend.objects;

import hu.Farkasch.buspalbackend.datastructures.Coordinates;

public class BusRoute{
    private ArrayList<BusTrip> busTripList;
    private int routeId;
    private boolean favourite = false;

    public BusRoute (int routeId){
        generate(routeId);
    }

    public void generate(int routeId){ //to-do: renerating the needed BusTrips and BusStops based on the given routeId

    }
}