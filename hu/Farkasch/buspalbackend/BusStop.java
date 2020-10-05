package hu.Farkasch.buspalbackend.objects;

import hu.Farkasch.buspalbackend.datastructures.Coordinates;

public class BusStop {
    private Coordinates cords;
    private String stopName;
    private int stopId;

    public BusStop(){
        cords = new Coordinates();
        stopName = "";
        stopId = -1; //no id = -1
    }

    public BusStop(int stopId, String stopName, Coordinates cords){
        this.stopId = stopId;
        this.stopName = stopName;
        this.cords = cords;
    }

    //basic getters and setters

    public Coordinates getCords(){
        return cords;
    }

    public String getStopName(){
        return stopName;
    }

    public int getStopId(){
        return stopId;
    }

    public void setCords(Coordinates cords){
        this.cords = cords;
    }

    public void setStopName(String stopName){
        this.stopName= stopName;
    }

    public void setStopId(int stopId){
        this.stopId = stopId;
    }

    @Override
    public String toString(){
        return String.format("id: " + stopId + ", name: " + stopName + ", coordinates: " + cords);
    }
}
