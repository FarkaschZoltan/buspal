package hu.farkasch.buspalbackend.datastructures;

import java.io.Serializable;

public class Coordinates implements Serializable {
    private static final int EARTH_RADIUS = 6_371_000; //in metres
    private double lat; //latitude of the coordinate
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

    public static boolean isInsideRadius(Coordinates center, Coordinates point, float radius){
        //using Haversine-formula
        double phi1 = center.lat * Math.PI/180; // φ, λ in radians
        double phi2 = point.lat * Math.PI/180;
        double phi = (point.lat-center.lat) * Math.PI/180;
        double lambda = (point.lon-center.lon) * Math.PI/180;

        double a = Math.sin(phi/2) * Math.sin(phi/2) + Math.cos(phi1) * Math.cos(phi2) *
                Math.sin(lambda/2) * Math.sin(lambda/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double d = Coordinates.EARTH_RADIUS * c;
        return d < radius;
    }

    public static double getDistance(Coordinates center, Coordinates point){
        double phi1 = center.lat * Math.PI/180; // φ, λ in radians
        double phi2 = point.lat * Math.PI/180;
        double phi = (point.lat-center.lat) * Math.PI/180;
        double lambda = (point.lon-center.lon) * Math.PI/180;

        double a = Math.sin(phi/2) * Math.sin(phi/2) + Math.cos(phi1) * Math.cos(phi2) *
                Math.sin(lambda/2) * Math.sin(lambda/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double d = Coordinates.EARTH_RADIUS * c;
        return d; //metres
    }

    @Override
    public String toString(){
        return String.format(lat + ", " + lon);
    }
}

