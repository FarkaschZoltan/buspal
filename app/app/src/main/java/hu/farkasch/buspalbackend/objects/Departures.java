package hu.farkasch.buspalbackend.objects;

import hu.farkasch.buspalbackend.datastructures.Time;

public class Departures { //everything is public for easier access
    public int routeId;
    public String routeName;
    public String destination;
    public Time departureTime;

    public Departures(){
        routeId = -1;
        routeName = "";
        destination = "";
        departureTime = new Time();
    }

    public Departures(int routeId, String routeName, String destination, Time departureTime){
        this.routeId = routeId;
        this.routeName = routeName;
        this. destination = destination;
        this.departureTime = departureTime;
    }

    @Override
    public String toString(){
        return String.format("route id: " + routeId + ", route name: " + routeName + ", destination: " + destination + ", departure time: " + departureTime );
    }
}
