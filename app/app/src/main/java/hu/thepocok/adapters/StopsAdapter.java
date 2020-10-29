package hu.thepocok.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hu.farkasch.buspalbackend.objects.BusRoute;
import hu.farkasch.buspalbackend.objects.BusStop;
import hu.kristol.buspal.R;

public class StopsAdapter extends RecyclerView.Adapter<StopsAdapter.StopsViewHolder>{
    private Context mCtx;
    private List<BusStop> stopsList;

    public StopsAdapter(Context mCtx, List<BusStop> stopsList) {
        this.mCtx = mCtx;
        this.stopsList = stopsList;
    }

    @Override
    public StopsAdapter.StopsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.stops_list, null);
        return new StopsAdapter.StopsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StopsAdapter.StopsViewHolder holder, int position) {
        BusStop stop = stopsList.get(position);

        holder.stopName.setText(stop.getStopName());
        holder.stopDistance.setText(stop.getDistance() + " m");
    }

    @Override
    public int getItemCount() {
        return stopsList.size();
    }

    class StopsViewHolder extends RecyclerView.ViewHolder {

        TextView stopName, stopDistance;

        public StopsViewHolder(View itemView) {
            super(itemView);

            stopName = itemView.findViewById(R.id.stop_name);
            stopDistance = itemView.findViewById(R.id.stop_distance);
        }
    }
}