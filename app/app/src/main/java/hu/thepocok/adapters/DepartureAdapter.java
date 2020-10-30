package hu.thepocok.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import hu.farkasch.buspalbackend.datastructures.Time;
import hu.farkasch.buspalbackend.objects.Departure;
import hu.kristol.buspal.R;
import hu.kristol.buspal.RouteStops;

public class DepartureAdapter extends RecyclerView.Adapter<DepartureAdapter.DepartureViewHolder>{
    private Context mCtx;
    private List<Departure> departureList;
    private RecyclerView recyclerView;

    public DepartureAdapter(Context mCtx, List<Departure> departureList, RecyclerView recyclerView) {
        this.mCtx = mCtx;
        this.departureList = departureList;
        this.recyclerView = recyclerView;
    }

    @Override
    public DepartureAdapter.DepartureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.departure_list, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = recyclerView.getChildLayoutPosition(view);
                Departure item = departureList.get(itemPosition);

                Intent i = new Intent(mCtx, RouteStops.class);
                i.putExtra("tripId", item.tripId);
                i.putExtra("routeName", item.routeName);
                mCtx.startActivity(i);
            }
        });
        return new DepartureAdapter.DepartureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DepartureAdapter.DepartureViewHolder holder, int position) {
        Departure departure = departureList.get(position);

        holder.routeName.setText(departure.routeName);
        holder.destination.setText(departure.destination);
        holder.departureTime.setText(new Time(departure.departureTime).toString(true));

    }

    @Override
    public int getItemCount() {
        return departureList.size();
    }

    class DepartureViewHolder extends RecyclerView.ViewHolder {

        TextView routeName, destination, departureTime;

        public DepartureViewHolder(View itemView) {
            super(itemView);

            routeName = itemView.findViewById(R.id.route_name);
            destination = itemView.findViewById(R.id.destination);
            departureTime = itemView.findViewById(R.id.departure_time);
        }
    }
}
