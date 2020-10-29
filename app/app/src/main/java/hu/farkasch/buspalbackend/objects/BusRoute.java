package hu.farkasch.buspalbackend.objects;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BusRoute implements Serializable {
    public String getDestinations() {
        return destinations;
    }

    public String getColor() {
        return color;
    }

    @SerializedName("route_id")
    private int routeId;
    private boolean favourite = false;
    @SerializedName("route_short_name")
    private String name;
    @SerializedName("route_type")
    private RouteType type;
    @SerializedName("route_desc")
    private String destinations;
    private String color;

    public RouteType getType() {
        return type;
    }

    public BusRoute(String routeId, String name, String type) {
        this.routeId = Integer.parseInt(routeId);
        this.name = name;
        int typeOf = Integer.parseInt(type);
        switch (typeOf){
            case 0:
                this.type = RouteType.TRAM;
                break;
            case 1:
                this.type = RouteType.METRO;
                break;
            case 3:
                this.type = RouteType.BUS;
                break;
            case 4:
                this.type = RouteType.FERRY;
                break;
            case 11:
                this.type = RouteType.TROLLEY;
                break;
            case 109:
                this.type = RouteType.SUBURBAN_RAILWAY;
                break;
            case 800:
                this.type = RouteType.TROLLEY;
                break;
        }
        this.destinations = "";
    }

    public BusRoute(String routeId, String name, String type, String destinations) {
        this.routeId = Integer.parseInt(routeId);
        this.name = name;
        int typeOf = Integer.parseInt(type);
        switch (typeOf){
            case 0:
                this.type = RouteType.TRAM;
                break;
            case 1:
                this.type = RouteType.METRO;
                break;
            case 3:
                this.type = RouteType.BUS;
                break;
            case 4:
                this.type = RouteType.FERRY;
                break;
            case 11:
                this.type = RouteType.TROLLEY;
                break;
            case 109:
                this.type = RouteType.SUBURBAN_RAILWAY;
                break;
            case 800:
                this.type = RouteType.TROLLEY;
                break;
        }
        this.destinations = destinations;
    }

    public int getRouteId() {
        return routeId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "BusRoute{" +
                "routeId=" + routeId +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", destinations='" + destinations + '\'' +
                '}';
    }
}