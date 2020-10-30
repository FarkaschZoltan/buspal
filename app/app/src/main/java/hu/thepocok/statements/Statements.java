package hu.thepocok.statements;

public class Statements {
    public static String getScheduleByStop(String route, String stop, String direction, String date) {
        String statement = "SELECT stop_times.departure_time FROM routes " +
                "INNER JOIN trips on routes.route_id = trips.route_id " +
                "INNER JOIN stop_times on stop_times.trip_id = trips.trip_id " +
                "INNER JOIN stops on stops.stop_id = stop_times.stop_id " +
                "INNER JOIN calendar_dates on calendar_dates.service_id = trips.service_id " +
                "WHERE routes.route_short_name = '" + route + "' AND stops.stop_name = '" + stop +
                "' AND trips.direction_id ='" + direction + "' AND calendar_dates.date = '" + date +
                "' ORDER BY stop_times.departure_time";

        return statement;
    }

    public static String getScheduleByRoute(String route, String direction, String date) {
        String statement = "SELECT stop_times.departure_time FROM routes " +
                "INNER JOIN trips on routes.route_id = trips.route_id " +
                "INNER JOIN stop_times on stop_times.trip_id = trips.trip_id " +
                "INNER JOIN stops on stops.stop_id = stop_times.stop_id " +
                "INNER JOIN calendar_dates on calendar_dates.service_id = trips.service_id " +
                "WHERE routes.route_short_name = '" + route + "' AND stop_times.stop_sequence = '1' " +
                "' AND trips.direction_id ='" + direction + "' AND calendar_dates.date = '" + date +
                "' ORDER BY stop_times.departure_time";

        return statement;
    }

    public static String getRoutesByStop(String stop, String db_name) {
        String statement = "";
        if(db_name.equals("Budapest")){
            statement = "SELECT DISTINCT routes.route_id, routes.route_short_name, routes.route_type, routes.route_desc FROM routes " +
                    "INNER JOIN trips on routes.route_id = trips.route_id " +
                    "INNER JOIN stop_times on stop_times.trip_id = trips.trip_id " +
                    "INNER JOIN stops on stops.stop_id = stop_times.stop_id " +
                    "INNER JOIN calendar_dates on calendar_dates.service_id = trips.service_id " +
                    "WHERE stops.stop_name LIKE '" + stop + "%' ORDER BY routes.route_short_name";
        }
        else{
            statement = "SELECT DISTINCT routes.route_id, routes.route_short_name, routes.route_type FROM routes " +
                    "INNER JOIN trips on routes.route_id = trips.route_id " +
                    "INNER JOIN stop_times on stop_times.trip_id = trips.trip_id " +
                    "INNER JOIN stops on stops.stop_id = stop_times.stop_id " +
                    "INNER JOIN calendar_dates on calendar_dates.service_id = trips.service_id " +
                    "WHERE stops.stop_name LIKE '" + stop + "%' ORDER BY routes.route_short_name";
        }
        return statement;
    }

    /*PRO TIPP: If a bus with the same name has multiple types of trips, it will list the stops in the following way:
        trip1 - stop1
        trip2 - stop1
        trip1 - stop2
        trip2 - stop2
        ...
    */
    public static String getStopsByRouteShortName(String routeShortName, int direction){ //this statement sorts the stops in ascending order according to stop_sequence
        String statement = "SELECT DISTINCT stops.stop_name, stop_times.stop_sequence FROM routes " +
                "INNER JOIN trips ON routes.route_id = trips.route_id " +
                "INNER JOIN stop_times ON trips.trip_id = stop_times.trip_id " +
                "INNER JOIN stops ON stop_times.stop_id = stops.stop_id " +
                "WHERE routes.route_short_name = " + routeShortName + " AND trips.direction_id = " + direction +
                "ORDER BY CAST(stop_times.stop_sequence as int) ASC;";
        return statement;
    }

    public static String getRouteDeparturesFromStop(int stop_id){
        String statement = "SELECT stop_times.departure_time, routes.route_short_name\n" +
                "FROM routes\n" +
                "INNER JOIN trips ON routes.route_id = trips.route_id\n" +
                "INNER JOIN stop_times ON trips.trip_id = stop_times.trip_id\n" +
                "WHERE stop_times.stop_id = " + stop_id + "\n" +
                "GROUP BY stop_times.departure_time, routes.route_short_name\n" +
                "ORDER BY departure_time";
        return statement;
    }

    public static String routeFirstStop(int trip_id){
        String statement = "SELECT stops.stop_name\n" +
                "FROM stops\n" +
                "INNER JOIN stop_times on stop_times.stop_id = stops.stop_id\n" +
                "WHERE stop_times.stop_sequence = 0 AND stop_times.trip_id = " + trip_id;
        return statement;
    }

    public static String routeLastStop(int trip_id){
        String statement = "SELECT stops.stop_name\n" +
                "FROM stops\n" +
                "INNER JOIN stop_times on stop_times.stop_id = stops.stop_id\n" +
                "WHERE stop_times.stop_sequence IN (SELECT trip_lengths.line_length FROM trip_lengths " +
                "WHERE trip_lengths.trip_id = " + trip_id + ") AND stop_times.trip_id = " + trip_id;
        return statement;
    }

    public static String getDepartureFromStop(int stop_id, int date){
        String statement = "SELECT trips.trip_id, stop_times.departure_time, routes.route_short_name, trips.trip_headsign \n" +
                "FROM routes\n" +
                "INNER JOIN trips ON routes.route_id = trips.route_id\n" +
                "INNER JOIN stop_times ON trips.trip_id = stop_times.trip_id\n" +
                "INNER JOIN calendar_dates ON calendar_dates.service_id = trips.service_id\n" +
                "WHERE stop_times.stop_id = " + stop_id + " AND calendar_dates.date = "+ date + "\n" +
                "GROUP BY trips.trip_id, trips.trip_headsign, stop_times.departure_time, routes.route_short_name\n" +
                "ORDER BY departure_time";
        return statement;
    }

    public static String getNearbyStops(double lat, double lon, double distance){
        String statement = "SELECT\n" +
                "stops.stop_id, stops.stop_name, stops.stop_lat, stops.stop_lon, \n" +
                "(\n" +
                "  6371 * acos (\n" +
                "      cos ( radians(" + lat + ") )\n" +
                "      * cos( radians( stops.stop_lat::float ) )\n" +
                "      * cos( radians( stops.stop_lon::float ) - radians(" + lon + ") )\n" +
                "      + sin ( radians(" + lat + ") )\n" +
                "      * sin( radians( stops.stop_lat::float ) )\n" +
                "  )\n" +
                ") AS distance\n" +
                "FROM stops\n" +
                "GROUP BY stops.stop_id, stops.stop_name\n" +
                "HAVING (\n" +
                "  6371 * acos (\n" +
                "      cos ( radians(" + lat + ") )\n" +
                "      * cos( radians( stops.stop_lat::float ) )\n" +
                "      * cos( radians( stops.stop_lon::float ) - radians(" + lon + ") )\n" +
                "      + sin ( radians(" + lat + ") )\n" +
                "      * sin( radians( stops.stop_lat::float ) )\n" +
                "  )\n" +
                ") < "+ distance + "\n" +
                "ORDER BY distance";
        return statement;
    }
}
