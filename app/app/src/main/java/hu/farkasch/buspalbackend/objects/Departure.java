package hu.farkasch.buspalbackend.objects;

import java.io.Serializable;

import hu.farkasch.buspalbackend.datastructures.Time;

public class Departure implements Serializable { //everything is public for easier access
    public int tripId;
    public String routeName;
    public String destination;
    public Time departureTime;

    public Departure(){
        tripId = -1;
        routeName = "";
        destination = "";
        departureTime = new Time();
    }

    public Departure(int tripId, String routeName, String destination, Time departureTime){
        this.tripId = tripId;
        this.routeName = routeName;
        this.destination = destination;
        this.departureTime = departureTime;
    }

    @Override
    public String toString(){
        return String.format("trip id: " + tripId + ", route name: " + routeName + ", destination: " + destination + ", departure time: " + departureTime );
    }
}
