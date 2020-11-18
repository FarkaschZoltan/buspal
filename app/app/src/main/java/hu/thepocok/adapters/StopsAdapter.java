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
import hu.farkasch.buspalbackend.objects.RouteType;
import hu.kristol.buspal.R;
import hu.kristol.buspal.StopDepartures;

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
        } else{
            holder.stopDistance.setText(stop.getDistance() + " m");
        }

        for(int i = 1; i < 7; i++){
            if(i <= stop.stoppingRoutes.size()) {
                switch (i) {
                    case 1:
                        setProperties(holder.route1, stop.stoppingRoutes.get(0).getType(),
                                stop.stoppingRoutes.get(0).getName());
                        break;
                    case 2:
                        setProperties(holder.route2, stop.stoppingRoutes.get(1).getType(),
                                stop.stoppingRoutes.get(1).getName());
                        break;
                    case 3:
                        setProperties(holder.route3, stop.stoppingRoutes.get(2).getType(),
                                stop.stoppingRoutes.get(2).getName());
                        break;
                    case 4:
                        setProperties(holder.route4, stop.stoppingRoutes.get(3).getType(),
                                stop.stoppingRoutes.get(3).getName());
                        break;
                    case 5:
                        setProperties(holder.route5, stop.stoppingRoutes.get(4).getType(),
                                stop.stoppingRoutes.get(4).getName());
                        break;
                    case 6:
                        setProperties(holder.route6, stop.stoppingRoutes.get(5).getType(),
                                stop.stoppingRoutes.get(5).getName());
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public void setProperties(TextView tv, RouteType type, String name){
        tv.setText(name);
        switch (type){
            case BUS:
                tv.setBackgroundColor(mCtx.getResources().getColor(R.color.bus));
                break;
            case TRAM:
                tv.setBackgroundColor(mCtx.getResources().getColor(R.color.tram));
                break;
            case TROLLEY:
                tv.setBackgroundColor(mCtx.getResources().getColor(R.color.trolley));
                break;
            case METRO:
                if(name.equals("M1")){
                    tv.setBackgroundColor(mCtx.getResources().getColor(R.color.metro_1));
                }else if(name.equals("M2")){
                    tv.setBackgroundColor(mCtx.getResources().getColor(R.color.metro_2));
                }else if(name.equals("M3")){
                    tv.setBackgroundColor(mCtx.getResources().getColor(R.color.metro_3));
                }else if(name.equals("M4")){
                    tv.setBackgroundColor(mCtx.getResources().getColor(R.color.metro_4));
                }
                break;
            case SUBURBAN_RAILWAY:
                if(name.equals("H5")){
                    tv.setBackgroundColor(mCtx.getResources().getColor(R.color.suburban_5));
                }else if(name.equals("H6")){
                    tv.setBackgroundColor(mCtx.getResources().getColor(R.color.suburban_6));
                }else if(name.equals("H7")){
                    tv.setBackgroundColor(mCtx.getResources().getColor(R.color.suburban_7));
                }else if(name.equals("H8") || name.equals("H9")){
                    tv.setBackgroundColor(mCtx.getResources().getColor(R.color.suburban_8));
                }
            default:
                tv.setBackgroundColor(mCtx.getResources().getColor(R.color.bus));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return stopsList.size();
    }

    class StopsViewHolder extends RecyclerView.ViewHolder {

        TextView stopName, stopDistance;
        TextView route1, route2, route3, route4, route5, route6;

        public StopsViewHolder(View itemView) {
            super(itemView);

            stopName = itemView.findViewById(R.id.stop_name);
            stopDistance = itemView.findViewById(R.id.stop_distance);

            route1 = itemView.findViewById(R.id.route1);
            route2 = itemView.findViewById(R.id.route2);
            route3 = itemView.findViewById(R.id.route3);
            route4 = itemView.findViewById(R.id.route4);
            route5 = itemView.findViewById(R.id.route5);
            route6 = itemView.findViewById(R.id.route6);
        }
    }
}
