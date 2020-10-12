package hu.farkasch.buspalbackend.objects;

import hu.farkasch.buspalbackend.datastructures.Coordinates;
import hu.farkasch.buspalbackend.datastructures.Time;
import java.util.ArrayList;

public class BusStop implements Comparable<BusStop>{
    private Coordinates cords;
    private String stopName;
    private int stopId;
    private int stopSequencePlace;

    public BusStop(){
        this.cords = new Coordinates();
        this.stopName = "";
        this.stopId = -1; //no id = -1
        this.stopSequencePlace = 0; //not in a sequence
    }

    public BusStop(int stopId, String stopName, Coordinates cords, int stopSequencePlace){
        this.stopId = stopId;
        this.stopName = stopName;
        this.cords = cords;
        this.stopSequencePlace = stopSequencePlace;
    }

    public BusStop(String stopName, int stopId){
        this.stopName = stopName;
        this.stopId = stopId;
        this.cords = new Coordinates();
        this.stopSequencePlace = 0;
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

    public int getStopSequencePlace(){
        return stopSequencePlace;
    }

    public void setCords(Coordinates cords){
        this.cords = cords;
    }

    public void setStopName(String stopName){
        this.stopName = stopName;
    }

    public void setStopId(int stopId){
        this.stopId = stopId;
    }

    public void setStopSequencePlace(int stopSequencePlace){
        this.stopSequencePlace = stopSequencePlace;
    }

    @Override
    public String toString(){ //not finished yet(?)
        return String.format("id: " + stopId + ", name: " + stopName + ", coordinates: " + cords);
    }

    @Override
    public int compareTo(BusStop bs){  //for easier sorting
        return Integer.compare(this.stopSequencePlace, bs.stopSequencePlace);
    }

}
