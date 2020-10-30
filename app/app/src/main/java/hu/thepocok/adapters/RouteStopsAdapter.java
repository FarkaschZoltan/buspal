package hu.thepocok.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hu.farkasch.buspalbackend.objects.BusStop;
import hu.farkasch.buspalbackend.objects.Departure;
import hu.kristol.buspal.R;
import hu.kristol.buspal.RouteStops;
import hu.kristol.buspal.StopDepartures;

public class RouteStopsAdapter extends RecyclerView.Adapter<RouteStopsAdapter.RouteStopViewHolder>{
    private Context mCtx;
    private List<BusStop> routeStopsList;
    private RecyclerView recyclerView;

    public RouteStopsAdapter(Context mCtx, List<BusStop> routeStopsList, RecyclerView recyclerView) {
        this.mCtx = mCtx;
        this.routeStopsList = routeStopsList;
        this.recyclerView = recyclerView;
    }

    @Override
    public RouteStopsAdapter.RouteStopViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.route_stops_list, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = recyclerView.getChildLayoutPosition(view);
                BusStop item = routeStopsList.get(itemPosition);

                Intent i = new Intent(mCtx, StopDepartures.class);
                i.putExtra("stop", item.getStopId());
                i.putExtra("stopName", item.getStopName());
                mCtx.startActivity(i);
            }
        });
        return new RouteStopsAdapter.RouteStopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RouteStopsAdapter.RouteStopViewHolder holder, int position) {
        BusStop bs = routeStopsList.get(position);

        holder.stopSequencePlace.setText(String.valueOf(bs.getStopSequencePlace() + 1));
        holder.stopName.setText(bs.getStopName());
        holder.departureTime.setText(bs.getDeparture().toString());
    }

    @Override
    public int getItemCount() {
        return routeStopsList.size();
    }

    class RouteStopViewHolder extends RecyclerView.ViewHolder {

        TextView stopSequencePlace, stopName, departureTime;

        public RouteStopViewHolder(View itemView) {
            super(itemView);

            stopSequencePlace = (TextView) itemView.findViewById(R.id.stop_sequence);
            stopName = itemView.findViewById(R.id.stop_name);
            departureTime = itemView.findViewById(R.id.departure_time);
        }
    }
}