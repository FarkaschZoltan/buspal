package hu.farkasch.buspalbackend.objects;

import hu.farkasch.buspalbackend.objects.RouteType;
import hu.thepocok.statements.Statements;

import java.io.Serializable;

public class BusRoute implements Serializable {
    public String getDestinations() {
        return destinations;
    }

    public String getColor() {
        return color;
    }

    private int routeId;
    private boolean favourite = false;
    private String name;
    private RouteType type;
    private String destinations;
    private String color;

    public RouteType getType() {
        return type;
    }

    public BusRoute(int routeId, String name, int type, String destinations) {
        this.routeId = routeId;
        this.name = name;
        switch (type){
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
                ", color='" + color + '\'' +
                '}';
    }
}