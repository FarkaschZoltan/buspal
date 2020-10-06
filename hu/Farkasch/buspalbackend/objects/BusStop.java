package hu.Farkasch.buspalbackend.objects;

import hu.Farkasch.buspalbackend.datastructures.Coordinates;
import java.time.LocalTime;

public class BusStop {
    private Coordinates cords;
    private String stopName;
    private int stopId;
    private LocalTime arrivalTime;
    private LocalTime departureTime;
    private int stopSequencePlace;

    public BusStop(){
        this.cords = new Coordinates();
        this.stopName = "";
        this.stopId = -1; //no id = -1
        this.arrivalTime = arrivalTime.of(0, 0, 0);
        this.arrivalTime = arrivalTime.of(0, 0, 0);
        this.stopSequencePlace = -1; //not in a sequence
    }

    public BusStop(int stopId, String stopName, Coordinates cords, String timeDataArrival, String timeDataDeparture, int stopSequencePlace){
        this.stopId = stopId;
        this.stopName = stopName;
        this.cords = cords;
        arrivalTime = arrivalTime.parse(timeDataArrival);
        departureTime = departureTime.parse(timeDataDeparture);
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

    public LocalTime getArrivalTime(){
        return arrivalTime;
    }

    public LocalTime getDepartureTime(){
        return departureTime;
    }

    public int getStopSequencePlace(){
        return getStopSequencePlace;
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
        arrivalTime = arrivalTime.parse(timeDataArrival);
    }

    public void setArrivalTime(LocalTime arrivalTime){ //LocalTime version
        this.arrivalTime = arrivalTime;
    }

    public void setDepartureTime(String timeDataDeparture){ //String version
        departureTime = departureTime.parse(timeDataDeparture);
    }

    public void setDepartureTime(LocalTime departureTime){ //LocalTime version
        this.departureTime = departureTime;
    }

    public void setStopSequencePlace(int stopSequencePlace){
        this.stopSequencePlace = stopSequencePlace;
    }

    @Override
    public String toString(){ //not finished
        return String.format("id: " + stopId + ", name: " + stopName + ", coordinates: " + cords);
    }
}
