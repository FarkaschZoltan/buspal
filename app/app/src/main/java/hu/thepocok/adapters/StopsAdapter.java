package hu.thepocok.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hu.farkasch.buspalbackend.objects.BusStop;
import hu.kristol.buspal.R;
import hu.kristol.buspal.StopDepartures;
import hu.kristol.buspal.Stops;

public class StopsAdapter extends RecyclerView.Adapter<StopsAdapter.StopsViewHolder>{
    private Context mCtx;
    private List<BusStop> stopsList;
    private RecyclerView recyclerView;

    public StopsAdapter(Context mCtx, List<BusStop> stopsList, RecyclerView recyclerView) {
        this.mCtx = mCtx;
        this.stopsList = stopsList;
        this.recyclerView = recyclerView;
    }

    @Override
    public StopsAdapter.StopsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.stops_list, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = recyclerView.getChildLayoutPosition(view);
                BusStop item = stopsList.get(itemPosition);

                Intent i = new Intent(mCtx, StopDepartures.class);
                Log.d("StopsAdapter", item.toString());
                i.putExtra("stop", item.getStopId());
                i.putExtra("stopName", item.getStopName());
                mCtx.startActivity(i);
            }
        });
        return new StopsAdapter.StopsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StopsAdapter.StopsViewHolder holder, int position) {
        BusStop stop = stopsList.get(position);

        holder.stopName.setText(stop.getStopName());
        if(stop.getDistance() < 0){
            holder.stopDistance.setText(" ");
        }
        else{
            holder.stopDistance.setText(stop.getDistance() + " m");
        }
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
