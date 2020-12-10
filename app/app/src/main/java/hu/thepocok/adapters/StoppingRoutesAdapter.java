package hu.thepocok.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hu.farkasch.buspalbackend.objects.BusRoute;
import hu.kristol.buspal.R;
import hu.kristol.buspal.StopDepartures;

public class StoppingRoutesAdapter extends RecyclerView.Adapter<StoppingRoutesAdapter.StoppingRoutesViewHolder>{
    private Context mCtx;
    private List<BusRoute> routesList;
    private RecyclerView recyclerView;
    private int stopId;
    private String stopName;

    public StoppingRoutesAdapter(Context mCtx, List<BusRoute> routesList, RecyclerView recyclerView,
                                 int stopId, String stopName) {
        this.mCtx = mCtx;
        this.routesList = routesList;
        this.recyclerView = recyclerView;
        this.stopId = stopId;
        this.stopName = stopName;
    }

    @Override
    public StoppingRoutesAdapter.StoppingRoutesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.stopping_routes_list, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = recyclerView.getChildLayoutPosition(view);
                BusRoute item = routesList.get(itemPosition);

                Intent i = new Intent(mCtx, StopDepartures.class);
                Log.d("StopsAdapter", item.toString());
                i.putExtra("route_name", item.getName());
                i.putExtra("stop", stopId);
                i.putExtra("stopName", stopName);
                mCtx.startActivity(i);
            }
        });
        return new StoppingRoutesAdapter.StoppingRoutesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StoppingRoutesAdapter.StoppingRoutesViewHolder holder, int position) {
        BusRoute route = routesList.get(position);

        holder.routeData.setText(route.getName());

        switch (route.getType()){
            case BUS:
                if(route.getName().charAt(0) == '9' && route.getName().length() >= 3){
                    holder.cardView.setCardBackgroundColor(mCtx.getResources().getColor(R.color.night_bus));
                    holder.routeData.setTextColor(Color.parseColor("#ffffff"));
                } else{
                    holder.cardView.setCardBackgroundColor(mCtx.getResources().getColor(R.color.bus));
                }
                break;
            case TRAM:
                holder.cardView.setCardBackgroundColor(mCtx.getResources().getColor(R.color.tram));
                break;
            case TROLLEY:
                holder.cardView.setCardBackgroundColor(mCtx.getResources().getColor(R.color.trolley));
                break;
            case METRO:
                if(route.getName().equals("M1")){
                    holder.cardView.setCardBackgroundColor(mCtx.getResources().getColor(R.color.metro_1));
                }else if(route.getName().equals("M2")){
                    holder.cardView.setCardBackgroundColor(mCtx.getResources().getColor(R.color.metro_2));
                }else if(route.getName().equals("M3")){
                    holder.cardView.setCardBackgroundColor(mCtx.getResources().getColor(R.color.metro_3));
                }else if(route.getName().equals("M4")){
                    holder.cardView.setCardBackgroundColor(mCtx.getResources().getColor(R.color.metro_4));
                }
                break;
            case SUBURBAN_RAILWAY:
                if(route.getName().equals("H5")){
                    holder.cardView.setCardBackgroundColor(mCtx.getResources().getColor(R.color.suburban_5));
                }else if(route.getName().equals("H6")){
                    holder.cardView.setCardBackgroundColor(mCtx.getResources().getColor(R.color.suburban_6));
                }else if(route.getName().equals("H7")){
                    holder.cardView.setCardBackgroundColor(mCtx.getResources().getColor(R.color.suburban_7));
                }else if(route.getName().equals("H8") || route.getName().equals("H9")){
                    holder.cardView.setCardBackgroundColor(mCtx.getResources().getColor(R.color.suburban_8));
                }else{
                    Log.d("RouteType", "None of the above");
                }
            default:
                holder.cardView.setBackgroundColor(mCtx.getResources().getColor(R.color.bus));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return routesList.size();
    }

    class StoppingRoutesViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView routeData;

        public StoppingRoutesViewHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardview);
            routeData = itemView.findViewById(R.id.route_data);
        }
    }
}
