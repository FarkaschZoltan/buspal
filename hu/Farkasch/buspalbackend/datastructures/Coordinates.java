package hu.farkasch.buspalbackend.objects;

public class Coordinates {
    private double lat; //lattitude of the coordinate
    private double lon; //longitude of the coordinate

    public Coordinates(){ //coordinate created without data is always 0, 0
        lat = 0;
        lon = 0;
    }

    public Coordinates(double lat, double lon){
        this.lat = lat;
        this.lon = lon;
    }

    //basic getters and setters

    public double getLat(){
        return lat;
    }

    public double getLon(){
        return lon;
    }

    public void setLat(double lat){
        this.lat = lat;
    }

    public void setLon(double lon){
        this.lon = lon;
    }

    public void setNewCoords(double lat, double lon){ //for the case if the coordinates need to be changed
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public String toString(){
        return String.format(lat + ", " + lon);
    }
}

