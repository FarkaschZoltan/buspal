package hu.farkasch.buspalbackend.objects;

import hu.farkasch.buspalbackend.datastructures.Coordinates;
import hu.farkasch.buspalbackend.datastructures.Time;

public class BusStop /*implements Comparable*/{
    private Coordinates cords;
    private String stopName;
    private int stopId;
    private Time arrivalTime;
    private Time departureTime;
    private int stopSequencePlace;

    public BusStop(){
        this.cords = new Coordinates();
        this.stopName = "";
        this.stopId = -1; //no id = -1
        this.arrivalTime = new Time();
        this.arrivalTime = new Time();
        this.stopSequencePlace = -1; //not in a sequence
    }

    public BusStop(int stopId, String stopName, Coordinates cords, String timeDataArrival, String timeDataDeparture, int stopSequencePlace){
        this.stopId = stopId;
        this.stopName = stopName;
        this.cords = cords;
        arrivalTime = new Time(timeDataArrival);
        departureTime = new Time(timeDataDeparture);
        this.stopSequencePlace = stopSequencePlace;
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

    public Time getArrivalTime(){
        return arrivalTime;
    }

    public Time getDepartureTime(){
        return departureTime;
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

    public void setArrivalTime(String timeDataArrival){ //String version
        arrivalTime = new Time(timeDataArrival);
    }

    public void setArrivalTime(Time arrivalTime){ //Time version
        this.arrivalTime = arrivalTime;
    }

    public void setDepartureTime(String timeDataDeparture){ //String version
        departureTime = new Time(timeDataDeparture);
    }

    public void setDepartureTime(Time departureTime){ //Time version
        this.departureTime = departureTime;
    }

    public void setStopSequencePlace(int stopSequencePlace){
        this.stopSequencePlace = stopSequencePlace;
    }

    @Override
    public String toString(){ //not finished yet(?)
        return String.format("id: " + stopId + ", name: " + stopName + ", coordinates: " + cords);
    }

    /*@Override
    public int compareTo(BusStop bs){  //for easier sorting
        if(stopSequencePlace < bs.getStopSequencePlace()){
            return -1;
        }
        else if (stopSequencePlace > bs.getStopSequencePlace()){
            return 1;
        }
        else {
            return 0 //do we need further comparing, if they are equal?
        }
    }
    */

}
