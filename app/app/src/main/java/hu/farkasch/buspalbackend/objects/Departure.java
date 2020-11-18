package hu.farkasch.buspalbackend.objects;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import hu.farkasch.buspalbackend.datastructures.Time;

public class Departure implements Serializable { //everything is public for easier access
    @SerializedName("trip_id")
    public int tripId;
    @SerializedName("route_short_name")
    public String routeName;
    @SerializedName("trip_headsign")
    public String destination;
    @SerializedName("departure_time")
    public String departureTime;

    public Departure(){
        tripId = -1;
        routeName = "";
        destination = "";
        departureTime = new Time().toString();
    }

    public Departure(int tripId, String routeName, String destination, String departureTime){
        this.tripId = tripId;
        this.routeName = routeName;
        this.destination = destination;
        this.departureTime = departureTime;
    }

    public Departure(int tripId, String routeName, String destination, Time departureTime){
        this.tripId = tripId;
        this.routeName = routeName;
        this.destination = destination;
        this.departureTime = departureTime.toString();
    }

    @Override
    public boolean equals(Object o){
        if (o == this){
            return true;
        }

        if(!(o instanceof Departure)){
            return false;
        }

        Departure d = (Departure) o;

        Time departureTimeDT = new Time(d.departureTime);
        Time departureTimeT = new Time(departureTime);

        /*
        System.out.print(routeName + " =? " + d.routeName + " "); // for debugging purposes
        System.out.println(routeName.equals(d.routeName));
        System.out.print(destination + " =? " + d.destination + " ");
        System.out.println(destination.equals(d.destination));
        System.out.print(departureTimeT + " =? " + departureTimeDT + " ");
        System.out.println(departureTimeT.equals(departureTimeDT));
        System.out.println("----------------------------------------");
        */

        return routeName.equals(d.routeName) && destination.equals(d.destination)
                && departureTimeT.equals(departureTimeDT);
    }

    @Override
    public String toString(){
        return String.format("trip id: " + tripId + ", route name: " + routeName
                + ", destination: " + destination + ", departure time: " + departureTime );
    }
}
