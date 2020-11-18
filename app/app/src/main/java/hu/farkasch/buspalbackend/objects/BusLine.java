package hu.farkasch.buspalbackend.objects;

import hu.farkasch.buspalbackend.datastructures.Time;
import java.util.ArrayList;

public class BusLine{
    private String lineName;
    private BusStop currentStop;
    private int direction;
    private ArrayList<BusStop> busStops;
    private ArrayList<Time> currentStopTimes;

    public BusLine(){
        lineName = "";
        direction = -1; //No direction
        currentStop= new BusStop();
        busStops = new ArrayList<BusStop>();
        currentStopTimes = new ArrayList<Time>();
    }

    public BusLine(String lineName, BusStop currentStop, int direction, ArrayList<BusStop> busStops,
                   ArrayList<Time> currentStopTimes) {
        //busStops will already be sorted with the help of getStopsByRouteShortName statement
        this.lineName = lineName;
        this.currentStop = currentStop;
        this.direction = direction;
        this.busStops = busStops; //we get this, with the help of getStopsByRouteShortName
        this.currentStopTimes = currentStopTimes; //we get this, with the help of getScheduleByStop
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public BusStop getCurrentStop() {
        return currentStop;
    }

    public void setCurrentStop(BusStop currentStop) {
        this.currentStop = currentStop;
    }

    public ArrayList<BusStop> getBusStops() {
        return busStops;
    }

    public void setBusStops(ArrayList<BusStop> busStops) {
        this.busStops = busStops;
    }

    public ArrayList<Time> getCurrentStopTimes() {
        return currentStopTimes;
    }

    public void setCurrentStopTimes(ArrayList<Time> currentStopTimes) {
        this.currentStopTimes = currentStopTimes;
    }

    public void newCurrentBusStop(BusStop currentStop){
        this.currentStop = currentStop;

    }


}