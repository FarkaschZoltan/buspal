package hu.farkasch.buspalbackend.objects;

import hu.farkasch.buspalbackend.datastructures.Coordinates;
import hu.farkasch.buspalbackend.datastructures.Time;
import java.util.ArrayList;

public class BusStop implements Comparable<BusStop>{
    private Coordinates cords;
    private String stopName;
    private int stopId;
    private int stopSequencePlace;
    private double distanceFromPosition;
    private Departures departures;

    public BusStop(){
        cords = new Coordinates();
        stopName = "";
        stopId = -1; //no id = -1
        stopSequencePlace = -1; //not in a sequence
        departures = new Departures();
    }

    public BusStop(int stopId, String stopName, Coordinates cords, int stopSequencePlace, Departures departures){
        this.stopId = stopId;
        this.stopName = stopName;
        this.cords = cords;
        this.stopSequencePlace = stopSequencePlace;
        this.distanceFromPosition = 999;
        this.departures = departures;

    }

    public BusStop(int stopId, String stopName, Coordinates cords, double distanceFromPosition){
        this.stopId = stopId;
        this.stopName = stopName;
        this.cords = cords;
        this.stopSequencePlace = -1;
        this.distanceFromPosition = distanceFromPosition * 1000;
        this.departures = new Departures();
    }

    public BusStop(String stopName, int stopId){
        this.stopName = stopName;
        this.stopId = stopId;
        this.cords = new Coordinates();
        this.stopSequencePlace = -1;
        this.distanceFromPosition = 999;
        this.departures = new Departures();
    }

    public Coordinates getCords() {
        return cords;
    }

    public void setCords(Coordinates cords) {
        this.cords = cords;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public int getStopId() {
        return stopId;
    }

    public void setStopId(int stopId) {
        this.stopId = stopId;
    }

    public int getStopSequencePlace() {
        return stopSequencePlace;
    }

    public void setStopSequencePlace(int stopSequencePlace) {
        this.stopSequencePlace = stopSequencePlace;
    }

    public double getDistanceFromPosition() {
        return distanceFromPosition;
    }

    public void setDistanceFromPosition(double distanceFromPosition) {
        this.distanceFromPosition = distanceFromPosition;
    }

    public Departures getDepartures() {
        return departures;
    }

    public void setDepartures(Departures departures) {
        this.departures = departures;
    }

    public int getDistance(){
        return (int)distanceFromPosition;
    }

    @Override
    public String toString(){ //not finished yet(?)
        return String.format("id: " + stopId + ", name: " + stopName + ", coordinates: " + cords + ", distance: " + distanceFromPosition + ", " + departures);
    }

    @Override
    public int compareTo(BusStop bs){  //for easier sorting
        return Integer.compare(this.stopSequencePlace, bs.stopSequencePlace);
    }

}
