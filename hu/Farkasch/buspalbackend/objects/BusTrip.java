package hu.Farkasch.buspalbackend.objects;

import java.utils.ArrayList;

public class BusTrip{
    private ArrayList<BusStop> busStopList;
    private int tripId;

    public BusTrip(){
        busStopList = new ArrayList<BusStop>();
        tripId = -1; //no trip id
    }

    public BusTrip(int tripId, ArrayList<BusStop> busStopList){
        this.tripId = tripId;
        this.busStopList = busStopList;
    }

    //basic getters and setters

    public ArrayList<BusStop> getBusStopList(){
        return busStopList;
    }    

    public int getTripId(){
        return tripId;
    }

    public void setBusStopList(ArrayList<BusStop> busStopList){
        this.busStopList = busStopList;
    }

    public void setTripId(int tripId){
        this.tripId = tripId;
    }

    //would be usefull to sort busStopList in ascending order (stopSequencePlace)

    public void addBusStop(BusStop bs){ 
        busStopList.add(bs);           
    }

    public void removeBusStop(BusStop bs){ //removing a busStop object
        busStopList.remove(bs);            //to-do(?) handleing trying to remove a non-existent BusStop
    }

    public void removeBusStop(int i){ //removing a BusStop on a specific index (maybe even its stopSequencePlace???)
        busStopList.remove(i);        //to-do(?) handleing trying to remove a non-existent BusStop
    }

    public void clear(){
        busStopList = new ArrayList<BusStop>();
    }
}
