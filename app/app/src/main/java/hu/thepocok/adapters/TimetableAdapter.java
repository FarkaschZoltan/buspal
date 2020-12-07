package hu.thepocok.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hu.farkasch.buspalbackend.objects.Departure;
import hu.kristol.buspal.R;
import hu.kristol.buspal.StopDepartures;

public class TimetableAdapter extends RecyclerView.Adapter<TimetableAdapter.TimetableViewHolder>{
    private Context mCtx;
    private List<Departure> stopsList;
    private RecyclerView recyclerView;

    public TimetableAdapter(Context mCtx, List<Departure> stopsList, RecyclerView recyclerView) {
        this.mCtx = mCtx;
        this.stopsList = stopsList;
        this.recyclerView = recyclerView;
    }

    @Override
    public TimetableAdapter.TimetableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.stops_list, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = recyclerView.getChildLayoutPosition(view);
                Departure item = stopsList.get(itemPosition);

                Intent i = new Intent(mCtx, StopDepartures.class);
                Log.d("StopsAdapter", item.toString());
                /*i.putExtra("stop", item.departureTime);
                i.putExtra("stopName", item.getStopName());*/
                mCtx.startActivity(i);
            }
        });
        return new TimetableAdapter.TimetableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TimetableAdapter.TimetableViewHolder holder, int position) {
        /*Departure stop = stopsList.get(position);
        Log.d("OnBind", stop.toString());
        Log.d("OnBind", stop.stoppingRoutes.toString());

        holder.stopName.setText(stop.getStopName());
        if(stop.getDistance() < 0){
            holder.stopDistance.setText(" ");
        } else{
            holder.stopDistance.setText(stop.getDistance() + " m");
        }

        StoppingRoutesAdapter adapter = new StoppingRoutesAdapter(mCtx,
                stop.stoppingRoutes, holder.stoppingRoutes, stop.getStopId(), stop.getStopName());
        holder.stoppingRoutes.setLayoutManager(new LinearLayoutManager(mCtx, LinearLayoutManager.HORIZONTAL, false));

        holder.stoppingRoutes.setAdapter(adapter);*/
    }

    @Override
    public int getItemCount() {
        return stopsList.size();
    }

    class TimetableViewHolder extends RecyclerView.ViewHolder {

        TextView stopName, stopDistance;
        RecyclerView stoppingRoutes;

        public TimetableViewHolder(View itemView) {
            super(itemView);

            stopName = itemView.findViewById(R.id.stop_name);
            stopDistance = itemView.findViewById(R.id.stop_distance);

            stoppingRoutes = itemView.findViewById(R.id.stopping_routes);
        }
    }
}
