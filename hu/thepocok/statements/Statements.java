package hu.thepocok.statements;

public class Statements {
    public static String getScheduleByStop(String route, String stop, String direction, String date){
        String statement = "SELECT stop_times.departure_time FROM routes " +
                "INNER JOIN trips on routes.route_id = trips.route_id " +
                "INNER JOIN stop_times on stop_times.trip_id = trips.trip_id " +
                "INNER JOIN stops on stops.stop_id = stop_times.stop_id " +
                "INNER JOIN calendar_dates on calendar_dates.service_id = trips.service_id " +
                "WHERE routes.route_short_name = '" + route +"' AND stops.stop_name = '" + stop +
                "' AND trips.direction_id ='" + direction + "' AND calendar_dates.date = '" + date  +
                "' ORDER BY stop_times.departure_time";

        return statement;
    }

    public static String getScheduleByRoute(String route, String direction, String date){
        String statement = "SELECT stop_times.departure_time FROM routes " +
                "INNER JOIN trips on routes.route_id = trips.route_id " +
                "INNER JOIN stop_times on stop_times.trip_id = trips.trip_id " +
                "INNER JOIN stops on stops.stop_id = stop_times.stop_id " +
                "INNER JOIN calendar_dates on calendar_dates.service_id = trips.service_id " +
                "WHERE routes.route_short_name = '" + route +"' AND stop_times.stop_sequence = '1' "+
                "' AND trips.direction_id ='" + direction + "' AND calendar_dates.date = '" + date  +
                "' ORDER BY stop_times.departure_time";

        return statement;
    }

    public static String getRoutesByStop(String stop){
        String statement = "SELECT DISTINCT routes.route_short_name FROM routes " +
                "INNER JOIN trips on routes.route_id = trips.route_id " +
                "INNER JOIN stop_times on stop_times.trip_id = trips.trip_id " +
                "INNER JOIN stops on stops.stop_id = stop_times.stop_id " +
                "INNER JOIN calendar_dates on calendar_dates.service_id = trips.service_id " +
                "WHERE stops.stop_name = " + stop;
        return statement;
    }

    public static String getTripIdByRouteId(int routeId){ //TO_DO
        String statement = "temp";
        return statement;
    }

    public static String getStopIdByTripId(int tripId){ //TO-DO
        String statement = "temp";
        return statement;
    }

    public static String getTripData(int tripId){ //TO-DO
        String statement = "temp";
        return statement;
    }

    public static String getBusStopData(int stopId){ //TO-DO
        String statement = "temp";
        return statement;
    }


}
